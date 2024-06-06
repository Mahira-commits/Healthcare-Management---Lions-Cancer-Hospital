package com.example.lion_nav_barhomepage


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
import com.example.lion_nav_barhomepage.databinding.FragmentVaccineCertificateBinding
import com.example.lion_nav_barhomepage.doctors.DoctorsFragment
import com.example.lion_nav_barhomepage.patientdashboard.reports.pdfactivity
import com.example.lion_nav_barhomepage.patientdashboard.vaccines.AddVaccineFragment
import com.example.lion_nav_barhomepage.patientdashboard.vaccines.VaccineAdapter
import com.example.lion_nav_barhomepage.patientdashboard.vaccines.vaccine
import com.google.firebase.firestore.FirebaseFirestore


var vaccine_list : ArrayList<vaccine> =  arrayListOf<vaccine>()
var vaccine_id : String = ""
class VaccineCertficatesFragment :Fragment() , VaccineAdapter.Click, VaccineAdapter.replace{

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentVaccineCertificateBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { db = FirebaseFirestore.getInstance()
        _binding = FragmentVaccineCertificateBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title="Vaccines"
        super.onViewCreated(view, savedInstanceState)

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
        vaccine_list =arrayListOf<vaccine>()
        db.collection("Vaccine").whereEqualTo("pemial", patient_main_data.email.toString())
            .get()
            .addOnSuccessListener {
                    datas->
                for ( i in datas){
                    Log.e("list","${i}")
                    val obj = vaccine(
                        i.data["vaccine_id"].toString(),
                        i.data["pemial"].toString(),
                        i.data["pname"].toString(),
                        i.data["date"].toString(),
                        i.data["vname"].toString(),
                        i.data["pdf"].toString()
                    )
                    vaccine_list.add(obj)
                }
                recyclerView.adapter = VaccineAdapter(this@VaccineCertficatesFragment,
                    vaccine_list,this@VaccineCertficatesFragment,this@VaccineCertficatesFragment)
                progressDialog.dismiss()
                Log.e("list", "$vaccine_list")
            }.addOnFailureListener { exception ->
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
        val filtered: ArrayList<vaccine> = ArrayList()

        val courseAry: ArrayList<vaccine> = vaccine_list

        for (eachCourse in courseAry) {
            if (eachCourse.date!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.date!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            } else if (eachCourse.vname!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.vname!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            }
            else if (eachCourse.vaccine_id!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.vaccine_id!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
//
            }

            //calling a method of the adapter class and passing the filtered list
//        DoctorsAdapter(this,filtered)
            recyclerView.adapter = VaccineAdapter(
                this@VaccineCertficatesFragment,
                filtered, this@VaccineCertficatesFragment, this@VaccineCertficatesFragment
            )
        }
    }
    override fun replaceFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        if (fragmentTransaction != null) {
            fragmentTransaction.replace(R.id.framelayout, fragment)
        }
        if (fragmentTransaction != null) {
            fragmentTransaction.commit()
        }
    }

    override fun go_to_main_view(url: String, r_id: String) {
        vaccine_id =r_id
        pdfactivity().seturl(url,2)
    }
}