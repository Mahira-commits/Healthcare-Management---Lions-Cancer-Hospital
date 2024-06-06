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
import androidx.core.net.toUri
import coil.load
import com.example.lion_nav_barhomepage.databinding.ActivityEditVaccineBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class EditVaccineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditVaccineBinding
    var db = Firebase.firestore
    var selectedPhotoUri: Uri? = null
    var img_url = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        db.collection("Newvaccine").whereEqualTo("text", r_id).get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    val obj = newvaccines(
                        i.data["image"].toString(),
                        i.data["text"].toString(),
                        i.data["distributor"].toString(),
                        i.data["dose"].toString(),
                        i.data["symptoms"].toString()
                    )
                    binding.vacimage.load(i.data["image"].toString().toUri())
                    binding.vacname.setText(i.data["text"].toString())
                    binding.vacname.setEnabled(false)  // Make the vaccine name EditText non-editable
                    binding.vacdistributor.setText(i.data["distributor"].toString())
                    binding.vacdose.setText(i.data["dose"].toString())
                    binding.vacsymptoms.setText(i.data["symptoms"].toString())
                }
            }
        super.onCreate(savedInstanceState)
        binding = ActivityEditVaccineBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.selectvacpic.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        binding.vacsubmit.setOnClickListener {
            var vacname = binding.vacname.text.toString()
            var vacdistributor = binding.vacdistributor.text.toString()
            var vacdose = binding.vacdose.text.toString()
            var vacsymptoms = binding.vacsymptoms.text.toString()

            if (selectedPhotoUri == null)
             {
            img_url = "android.resource://com.example.lion_nav_barhomepage/drawable/default_placeholder"
            saveVaccineToFirestore(vacname, vacdistributor, vacdose, vacsymptoms, img_url)
            }

            if (selectedPhotoUri != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Uploading....")
                progressDialog.setCancelable(false)
                progressDialog.show()

                val ref = FirebaseStorage.getInstance().getReference("/Newvaccine/$vacname")
                val bitmap =
                    MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedPhotoUri)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
                val reducedImage: ByteArray = byteArrayOutputStream.toByteArray()

                ref.putBytes(reducedImage)
                    .addOnSuccessListener {

                        ref.downloadUrl.addOnSuccessListener {
                            img_url = it.toString()

                            val data = newvaccines(
                                img_url,
                                vacname,
                                vacdistributor,
                                vacdose,
                                vacsymptoms

                            )
                            val diagnosis = db.collection("Newvaccine").document(vacname).set(data)
                                .addOnSuccessListener {
                                    Log.e("success", "${data}")
                                    val toast =
                                        Toast.makeText(
                                            this,
                                            " Sucessfully added",
                                            Toast.LENGTH_SHORT
                                        )
                                    toast.show()
                                    val intent = Intent(this, VaccineActivity::class.java)
                                    startActivity(intent)
                                }

                        }.addOnFailureListener { e ->
                            val toast = Toast.makeText(this, "failed", Toast.LENGTH_SHORT)
                            toast.show()
                            Log.d("Uploading", "Failed ")
                        }


                    }
            }
        }
    }



    override fun onBackPressed() {
        val intent= Intent(this,VaccineActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
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
//            binding.img.clear()
            selectedPhotoUri = data.data
//            selectedPhotoUri?.let { doCrop(it) }
            val bitmap =
                MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            binding.vacimage.setBackgroundDrawable(bitmapDrawable)
        }
    }
}
