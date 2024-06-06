package com.example.lion_nav_barhomepage.Facilities

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.lion_nav_barhomepage.databinding.FragmentFacilitiesBinding
import com.example.lion_nav_barhomepage.doctors.DoctorsFragment
import com.example.lion_nav_barhomepage.patientdashboard.diagnosis.diagnosislist
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
lateinit  var imageList : ArrayList<SlideModel>
var faclist : ArrayList<facilities> =  arrayListOf<facilities>()
class FacilitiesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentFacilitiesBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFacilitiesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title="Facilities"
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.RecyclerView
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        faclist =arrayListOf<facilities>()
        if (!DoctorsFragment().isConnected(requireContext())) {
            Toast.makeText(requireContext(), "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        db.collection("facilities").orderBy("date", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    Log.e("list", "${i}")
                    val obj = facilities(
                        i.data["fac_id"].toString(),
                        i.data["text"].toString(),
                        i.data["date"].toString(),
                    )

                    faclist.add(obj)

                }
                recyclerView.adapter = FacilitiesAdapter(faclist,this@FacilitiesFragment)

                progressDialog.dismiss()
                Log.e("list", "$faclist")
            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }
        imageList = arrayListOf<SlideModel>()

        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        db.collection("facimage").orderBy("date", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    Log.e("list", "${i}")
                    val obj = com.denzcoskun.imageslider.models.SlideModel(
                        i.data["image"].toString(),
                        i.data["date"].toString()
                    )

                    imageList.add(obj)
                    binding.imageslider.setImageList(imageList, ScaleTypes.FIT)

                }

                progressDialog.dismiss()
            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }
    }
    }

