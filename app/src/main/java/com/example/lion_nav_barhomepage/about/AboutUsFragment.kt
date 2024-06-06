package com.example.lion_nav_barhomepage.about

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lion_nav_barhomepage.AboutAdapter
import com.example.lion_nav_barhomepage.aboutmodel
import com.example.lion_nav_barhomepage.databinding.FragmentAboutUsBinding
import com.example.lion_nav_barhomepage.doctors.DoctorsFragment
import com.example.lion_nav_barhomepage.patientdashboard.diagnosis.diagnosislist
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AboutUsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
var aboutlist : ArrayList<aboutmodel> =  arrayListOf<aboutmodel>()
class AboutUsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentAboutUsBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.RecyclerView
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        aboutlist =arrayListOf<aboutmodel>()
        if (!DoctorsFragment().isConnected(requireContext())) {
            Toast.makeText(requireContext(), "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        db.collection("about").orderBy("date", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    Log.e("list", "${i}")
                    val obj = aboutmodel(
                        i.data["about_id"].toString(),
                        i.data["text"].toString(),
                        i.data["date"].toString(),
                    )

                    aboutlist.add(obj)

                }
                recyclerView.adapter = AboutAdapter(aboutlist,this@AboutUsFragment)

                progressDialog.dismiss()
                Log.e("list", "$diagnosislist")
            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }
    }



}