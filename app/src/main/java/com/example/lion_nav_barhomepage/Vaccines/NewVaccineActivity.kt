package com.example.lion_nav_barhomepage.Vaccines

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.lion_nav_barhomepage.databinding.ActivityEditVaccineBinding
import com.example.lion_nav_barhomepage.databinding.ActivityNewVaccineBinding

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class NewVaccineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditVaccineBinding
    var db = Firebase.firestore
    var selectedPhotoUri: Uri? = null
    var img_url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditVaccineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectvacpic.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        binding.vacsubmit.setOnClickListener {
            uploadImageAndSaveVaccine()
        }
    }

    private fun uploadImageAndSaveVaccine() {
        val vacname = binding.vacname.text.toString()
        val vacdistributor = binding.vacdistributor.text.toString()
        val vacdose = binding.vacdose.text.toString()
        val vacsymptoms = binding.vacsymptoms.text.toString()

        if (selectedPhotoUri != null) {
            uploadImageToFirebase(vacname, vacdistributor, vacdose, vacsymptoms)
        } else {
            img_url = "android.resource://com.example.lion_nav_barhomepage/drawable/default_placeholder"
            saveVaccineToFirestore(vacname, vacdistributor, vacdose, vacsymptoms, img_url)
        }
    }

    private fun uploadImageToFirebase(vacname: String, vacdistributor: String, vacdose: String, vacsymptoms: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val ref = FirebaseStorage.getInstance().getReference("/Newvaccine/$vacname")
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
        val reducedImage: ByteArray = byteArrayOutputStream.toByteArray()

        ref.putBytes(reducedImage).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                img_url = it.toString()
                saveVaccineToFirestore(vacname, vacdistributor, vacdose, vacsymptoms, img_url)
                progressDialog.dismiss()
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveVaccineToFirestore(vacname: String, vacdistributor: String, vacdose: String, vacsymptoms: String, imageUrl: String) {
        val data = newvaccines(imageUrl, vacname, vacdistributor, vacdose, vacsymptoms)
        db.collection("Newvaccine").document(vacname).set(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, VaccineActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save vaccine", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            binding.vacimage.setBackgroundDrawable(bitmapDrawable)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, VaccineActivity::class.java))
        super.onBackPressed()
    }
}