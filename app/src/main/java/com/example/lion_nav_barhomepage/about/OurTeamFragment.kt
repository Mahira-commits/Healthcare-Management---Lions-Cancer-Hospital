package com.example.lion_nav_barhomepage.about

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.FragmentDiagnosisBinding
import com.example.lion_nav_barhomepage.databinding.FragmentOurTeamBinding
import com.example.lion_nav_barhomepage.doctors.DoctorsFragment
import com.example.lion_nav_barhomepage.patient_main_data
import com.example.lion_nav_barhomepage.patientdashboard.diagnosis.*
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
var teamlist : ArrayList<team> =  arrayListOf<team>()
var urlimg : String = ""
var r_id : String = ""
class OurTeamFragment : Fragment(), TeamAdapter.Click{
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentOurTeamBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        db = FirebaseFirestore.getInstance()
        _binding = FragmentOurTeamBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title = "Our Team"
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.RecyclerView
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        teamlist = arrayListOf<team>()
        if (!DoctorsFragment().isConnected(requireContext())) {
            Toast.makeText(requireContext(), "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        db.collection("Team")
            .get()
            .addOnSuccessListener { datas ->

                for (i in datas) {
                    Log.e("list", "${i}")
                    val obj = team(
                        i.data["name"].toString(),
                        i.data["designation"].toString(),
                        i.data["number"].toString(),
                        i.data["about"].toString(),
                        i.data["image"].toString(),

                    )
                    teamlist.add(obj)
                }
                recyclerView.adapter = TeamAdapter(
                    this@OurTeamFragment,
                    teamlist, this@OurTeamFragment
                )
                progressDialog.dismiss()

            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }
    }

    override fun go_to_main_view(url: String,id:String) {
        urlimg =url
        r_id =id

    }
}