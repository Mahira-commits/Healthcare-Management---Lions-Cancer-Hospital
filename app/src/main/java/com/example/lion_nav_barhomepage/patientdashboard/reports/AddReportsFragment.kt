package com.example.lion_nav_barhomepage.patientdashboard.reports

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.FragmentAddReportsBinding
import com.example.lion_nav_barhomepage.databinding.FragmentReportsBinding
import com.example.lion_nav_barhomepage.patient_main_data
import com.example.lion_nav_barhomepage.patientdashboard.patientdata
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class AddReportsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentAddReportsBinding? = null
    var genurl : String =""
    val db = FirebaseFirestore.getInstance()
    private val binding get() = _binding!!
    var pdfuri : Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {_binding = FragmentAddReportsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as AppCompatActivity).supportActionBar?.title="Reports"
        super.onViewCreated(view, savedInstanceState)
        binding.selectpdf.setOnClickListener {
            selectpdf()

        }
        binding.upload.setOnClickListener {
            if(pdfuri!=null){
                upload(pdfuri)
                pdfuri=null
            }

        }
    }

    private fun upload(pdfuri: Uri?) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.setMessage("Uploading....")
        progressDialog.setProgress(0)
        progressDialog.show()
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val filename = patient_main_data.id+"_"+currentDate.subSequence(0,2)+currentDate.subSequence(3,5)+currentDate.subSequence(6,10)+currentDate.subSequence(11,13)+currentDate.subSequence(14,16)+currentDate.subSequence(17,18).toString()
        val ref = FirebaseStorage.getInstance().getReference("/reports/${filename}")
           ref.putFile(pdfuri!!).addOnSuccessListener {
               ref.downloadUrl.addOnSuccessListener {
                   genurl = it.toString()
                   val data = reports(
                       filename,
                       patient_main_data.email,
                       patient_main_data.name,
                       null,
                       currentDate,
                       null,
                       genurl
                   )
                   db.collection("Reports").document(filename).set(data)
               }
                   .addOnFailureListener {

                       Log.e("pdfurl:::", "FAILED")
                   }
           }.addOnFailureListener{
               Log.e("pdfurl:::", "FAILED")

           }.addOnProgressListener {
               val p : Long = (100 * it.bytesTransferred / it.totalByteCount)
               progressDialog.setProgress(p.toInt())
           }
               .addOnCompleteListener { progressDialog.dismiss() }

    }


    private fun selectpdf() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type="application/pdf"
        startActivityForResult(intent,86)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==86 && resultCode==RESULT_OK && data!=null){
        pdfuri= data.data
            binding.file.setText(data.data!!.lastPathSegment)
            Log.e("pdfurl:::","${data.data!!.path}")
        }
        else{
            Toast.makeText(this.requireActivity(),"Pleaes select a file",Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}