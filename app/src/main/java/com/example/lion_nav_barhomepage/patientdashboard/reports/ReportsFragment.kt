package com.example.lion_nav_barhomepage.patientdashboard.reports

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
import com.example.lion_nav_barhomepage.databinding.FragmentReportsBinding
import com.example.lion_nav_barhomepage.doctors.DoctorsFragment
import com.example.lion_nav_barhomepage.patient_main_data
import com.example.lion_nav_barhomepage.patientdashboard.diagnosis.DiagnosisAdapter
import com.example.lion_nav_barhomepage.patientdashboard.diagnosis.diagnosis
import com.google.firebase.firestore.FirebaseFirestore

var reportslist : ArrayList<reports> =  arrayListOf<reports>()
var report_id : String = ""
class ReportsFragment : Fragment() , ReportsAdapter.Click,ReportsAdapter.replace{
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        db = FirebaseFirestore.getInstance()
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title="Reports"
        super.onViewCreated(view, savedInstanceState)
        //---------------------------------
        binding.addpres.setOnClickListener {
            replaceFragment(AddReportsFragment())
        }
        //---------------------------------
        recyclerView = binding.RecyclerView
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        if (!DoctorsFragment().isConnected(requireContext())) {
            Toast.makeText(requireContext(), "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        reportslist=arrayListOf<reports>()
        db.collection("Reports").whereEqualTo("pemial", patient_main_data.email.toString())
            .get()
            .addOnSuccessListener {
                    datas->

                for ( i in datas){
                    Log.e("list","${i}")
                    val obj = reports(
                        i.data["report_id"].toString(),
                        i.data["pemial"].toString(),
                        i.data["pname"].toString(),
                        i.data["dname"].toString(),
                        i.data["date"].toString(),
                        i.data["did"].toString(),
                        i.data["pdf"].toString()
                    )
                    reportslist.add(obj)
                }
                recyclerView.adapter = ReportsAdapter(this@ReportsFragment,
                    reportslist,this@ReportsFragment,this@ReportsFragment)
                progressDialog.dismiss()
                Log.e("list", "$reportslist")
            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    fun filter(text: String) {
        val filtered: ArrayList<reports> = ArrayList()

        val courseAry: ArrayList<reports> = reportslist

        for (eachCourse in courseAry) {
            if (eachCourse.date!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.report_id!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            } else if (eachCourse.report_id!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.date!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            } else if (eachCourse.did!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.did!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            } else if (eachCourse.pname!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.pname!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
//            } else if (eachCourse.appointment_id!!.toLowerCase()
//                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
//                    .contains(text.toLowerCase())
//            ) {
//                filtered.add(eachCourse)
//            }
            }

            //calling a method of the adapter class and passing the filtered list
//        DoctorsAdapter(this,filtered)
            recyclerView.adapter = ReportsAdapter(this@ReportsFragment,
                filtered,this@ReportsFragment,this@ReportsFragment) }
    }
    override fun go_to_main_view(url: String,id:String) {
        report_id=id
        pdfactivity().seturl(url,1)
    }

    override fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout, fragment)
        fragmentTransaction.commit()
    }

}