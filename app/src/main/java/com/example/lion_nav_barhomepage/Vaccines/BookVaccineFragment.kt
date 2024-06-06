package com.example.lion_nav_barhomepage.Vaccines

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import coil.load
import com.example.lion_nav_barhomepage.Appointment.appointment
import com.example.lion_nav_barhomepage.LoginActivity
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.FragmentAppointmentBinding
import com.example.lion_nav_barhomepage.databinding.FragmentBookVaccineBinding
import com.example.lion_nav_barhomepage.patient_main_data
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalTime

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookVaccineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookVaccineFragment : Fragment() {
    var requestvacList = bookvaccine()
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentBookVaccineBinding? = null
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBookVaccineBinding.inflate(inflater, container, false)
        val view = binding.root
        db = FirebaseFirestore.getInstance()
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPreferences = this.requireActivity().getSharedPreferences(
            "login",
            AppCompatActivity.MODE_PRIVATE
        )
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
//        when(requestvacList[position].dose){
//            "Other"-> binding.radiogroup.check(R.id.other)
//        }
        var vlist = arrayListOf<newvaccines>()

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

                    binding.vacname.setText(i.data["text"].toString())
                    binding.vacdist.setText(i.data["distributor"].toString())

                    val number = i.data["dose"].toString().trim { it <= ' ' }
                        .toInt()
                    addRadioButtons(number)

                    progressDialog.dismiss()


                }


            }
        binding.book.setOnClickListener {
            var distname = binding.distname.text.toString()
            var other = binding.othername.text.toString()
            val checklogin = sharedPreferences.getBoolean("logged", false)
            if (checklogin == false) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setMessage("Please Register")
                    .setCancelable(true)
                dialogBuilder.setPositiveButton("Cancel ") { dialog, id ->
                }
                dialogBuilder.setNegativeButton("Register") { dialog, id ->
                    val intent = Intent(this.context, LoginActivity::class.java)
                    startActivity(intent)

                }

                val alert = dialogBuilder.create()
                alert.show()
            } else {
                val progressDialog = ProgressDialog(this.context)
                progressDialog.setMessage("Uploading....")
                progressDialog.setCancelable(false)
                progressDialog.show()
                val currentTime = LocalTime.now()
                val currentDate = LocalDate.now()

                val filename =
                    patient_main_data.id.toString() + "-" + currentDate + "-" + currentTime
                val data = bookvaccine(
                    filename,
                    r_id,
                    distname,
                    patient_main_data.email,
                    requestvacList.dose,
                    other,
                    "Pending"
                )
                val diagnosis = db.collection("VaccineRequests").document(filename).set(data)
                    .addOnSuccessListener {
                        Log.e("success", "${data}")
                        val toast =
                            Toast.makeText(
                                this.context,
                                " Sucessfully added",
                                Toast.LENGTH_SHORT
                            )
                        toast.show()
                        progressDialog.dismiss()
                        val view = View.inflate(this.context, R.layout.confirm_vac_dialog, null)
                        val button = view.findViewById<Button>(R.id.ok)
                        val builder = AlertDialog.Builder(this.requireContext())
                        builder.setView(view)

                        val dialog = builder.create()
                        dialog.show()
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        dialog.setCancelable(false)
                        button.setOnClickListener {
                            dialog.dismiss()


                        }
                    }

                    .addOnFailureListener { e ->
                        val toast = Toast.makeText(this.context, "failed", Toast.LENGTH_SHORT)
                        toast.show()
                        Log.d("Uploading", "Failed ")
                    }


            }
        }

            (activity as AppCompatActivity).supportActionBar?.title = "Vaccine Info"
            super.onViewCreated(view, savedInstanceState)
        }


    fun addRadioButtons(number: Int) {
        binding.radiogroup!!.orientation = LinearLayout.HORIZONTAL
        //
        for (i in 1..number) {
            val rdbtn = RadioButton(this.context)

            rdbtn.id = View.generateViewId()

            rdbtn.text = "Dose " + i.toString()
            rdbtn.setOnClickListener(View.OnClickListener {
                when (i) {
                    rdbtn.id -> requestvacList.dose = rdbtn.text.toString()
                }
            })
            binding.radiogroup!!.addView(rdbtn)
            binding.radiogroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.other -> requestvacList.dose = "Other"
                }


            })
        }
    }
}


