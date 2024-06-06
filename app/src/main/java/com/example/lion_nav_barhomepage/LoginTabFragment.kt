package com.example.lion_nav_barhomepage

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import androidx.fragment.app.activityViewModels
import com.example.lion_nav_barhomepage.Home.HomeFragment
import com.example.lion_nav_barhomepage.databinding.FragmentLoginTabBinding
import com.example.lion_nav_barhomepage.databinding.FragmentRegisterTabBinding
import com.example.lion_nav_barhomepage.patientdashboard.PatientProfileFragment
import com.example.lion_nav_barhomepage.patientdashboard.patientdata
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginTabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginTabFragment : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
    var db_patient = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentLoginTabBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginTabBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signup.setOnClickListener {
            if(checking()){
                val email=binding.email.text.toString()
                val password= binding.signPass.text.toString()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this.requireActivity()) { task ->
                        if (task.isSuccessful) {
                            load()
                            Toast.makeText(this.context, "Logged in", Toast.LENGTH_LONG).show()
                            sharedPreferences=this.requireActivity().getSharedPreferences("login",
                                AppCompatActivity.MODE_PRIVATE
                            )
                            sharedPreferences.edit().putBoolean("logged",true).apply()

                            //-------------------------------
                            var patient_info = patientdata()
                            val docRef = db_patient.collection("USERS").document(email)
                            docRef.get()
                                .addOnSuccessListener {
                                        tasks->
                                    Log.e("patient -->email: ","${tasks.get("Email").toString()}")
                                    patient_info = patientdata(
                                        tasks.get("id").toString(),
                                        tasks.get("name").toString(),
                                        tasks.get("email").toString(),
                                        tasks.get("phone").toString(),
                                        tasks.get("img_url").toString(),
                                        tasks.get("gender").toString(),
                                        tasks.get("dob").toString(),
                                        tasks.get("weight").toString(),
                                        tasks.get("height").toString())
                                    Log.e(" data object ",">>>>> ${patient_info}")
                                    patient_main_data=patient_info

                                    val intent = Intent(this.context, MainActivity::class.java)
                                    startActivity(intent)

                                    sharedPreferences.edit().putString("logged_email",patient_info.email).apply()
                                    sharedPreferences.edit().putString("logged_id",patient_info.id).apply()
                                    sharedPreferences.edit().putString("logged_name",patient_info.name).apply()
                                    sharedPreferences.edit().putString("logged_phone",patient_info.phone).apply()
                                    sharedPreferences.edit().putString("logged_img",patient_info.img_url).apply()
                                    sharedPreferences.edit().putString("logged_gender",patient_info.gender).apply()
                                    sharedPreferences.edit().putString("logged_dob",patient_info.DOB).apply()
                                    sharedPreferences.edit().putString("logged_height",patient_info.height).apply()
                                    sharedPreferences.edit().putString("logged_weight",patient_info.weight).apply()
                                }
                                .addOnFailureListener{
                                    Toast.makeText(this.context, "Unable to fetch data", Toast.LENGTH_LONG).show()
                                    Log.e("patient data ",">>>>> FAIlED")
                                }

                            //-------------------------------

                        } else {
                            Toast.makeText(this.context, "Wrong Details", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            else{
                Toast.makeText(this.context,"Enter the Details",Toast.LENGTH_LONG).show()
            }
        }
        binding.forgot.setOnClickListener{
            val builder = AlertDialog.Builder(this.requireActivity())
            builder.setTitle("Forgot Password")
            val view = layoutInflater.inflate(R.layout.dialog_forgot_password,null)

            val useremail=view.findViewById<EditText>(R.id.user_email)
            builder.setView(view)
            builder.setPositiveButton("Reset",DialogInterface.OnClickListener{_,_->
                forgotpassword(useremail)
            })
            builder.setNegativeButton("Close",DialogInterface.OnClickListener{_,_->})
            builder.show()
        }
    }

    private fun forgotpassword(useremail: EditText) {
        if (useremail.text.toString().isEmpty()) {

            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(useremail.text.toString()).matches()) {

            return
        }
        Firebase.auth.sendPasswordResetEmail(useremail.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this.context,"Email Sent",Toast.LENGTH_SHORT).show()

                }
            }


    }





    private fun load() {

        val progressDialog = ProgressDialog(this.context)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)
        progressDialog.show()


    }
    fun checking():Boolean
    {
        if(binding.email.text.toString().trim{it<=' '}.isNotEmpty()
            && binding.signPass.text.toString().trim{it<=' '}.isNotEmpty())
        {
            return true
        }
        return false
    }
}