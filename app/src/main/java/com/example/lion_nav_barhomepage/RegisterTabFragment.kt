package com.example.lion_nav_barhomepage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lion_nav_barhomepage.Home.HomeFragment
import com.example.lion_nav_barhomepage.databinding.FragmentDoctorsBinding
import com.example.lion_nav_barhomepage.databinding.FragmentRegisterTabBinding
import com.example.lion_nav_barhomepage.patientdashboard.patientdata
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.perf.FirebasePerformance

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterTabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterTabFragment : Fragment() {

    private lateinit var auth: FirebaseAuth


        private lateinit var db: FirebaseFirestore
    private var _binding: FragmentRegisterTabBinding? = null
    private val binding get() = _binding!!
    var email = ""
    var password = ""
    var name = ""
    var phone = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        super.onCreate(savedInstanceState)

        FirebasePerformance.getInstance().isPerformanceCollectionEnabled = true
        val trace = FirebasePerformance.getInstance().newTrace("TEST-TRACE")
        Log.d("TAG", "Starting trace")
        trace.start()
        trace.putAttribute("experiment", "A");
        trace.stop()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterTabBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signup.setOnClickListener { if(checking())
        {
            var email=binding.signEmail.text.toString()
            var password= binding.signPass.text.toString()
            var name=binding.signName.text.toString()
            var phone=binding.signPhone.text.toString()

            Log.e("Check::::>","${email}")

            val data = patientdata(
                "0",
                name,
                email,
                phone,
                "",
                "",
                "",
                ""
            )
            val Users=db.collection("USERS")
            val query =Users.whereEqualTo("email",email).get()
                .addOnSuccessListener {
                        tasks->
                    if(tasks.isEmpty)
                    {
                        auth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener{
                                    task->
                                if(task.isSuccessful)
                                {
                                    Users.document(email).set(data)
                                    view_Pager.setCurrentItem(1)
//                                    Toast.makeText(this.context,"Registartion Successful", Toast.LENGTH_LONG).show()


                                }
                                else
                                {
                                    Toast.makeText(this.context,"Authentication Failed", Toast.LENGTH_LONG).show()
                                }
                            }
                    }
                    else
                    {
                        Toast.makeText(this.context,"User Already Registered", Toast.LENGTH_LONG).show()
                        view_Pager.setCurrentItem(1)
                    }
                }
        }
        else{
            Toast.makeText(this.context,"Enter the Details", Toast.LENGTH_LONG).show()
        }
        }
    }


    private fun checking():Boolean{
        if(binding.signName.text.toString().trim{it<=' '}.isNotEmpty()
            && binding.signPhone.text.toString().trim{it<=' '}.isNotEmpty()
            && binding.signEmail.text.toString().trim{it<=' '}.isNotEmpty()
            && binding.signPass.text.toString().trim{it<=' '}.isNotEmpty()
        )
        {
            return true
        }
        return false
    }

        }







//    private fun getdata() {
//
//        if (binding.signName.text.toString().isEmpty()) {
//            binding.signName.error = "Please enter email"
//            binding.signName.requestFocus()
//            return
//        }
//        if (binding.signPhone.text.toString().isEmpty()) {
//            binding.signPhone.error = "Please enter email"
//            binding.signPhone.requestFocus()
//            return
//        }
//        if (binding.signEmail.text.toString().isEmpty()) {
//            binding.signEmail.error = "Please enter email"
//            binding.signEmail.requestFocus()
//            return
//        }
//
//        if (!Patterns.EMAIL_ADDRESS.matcher(binding.signEmail.text.toString()).matches()) {
//            binding.signEmail.error = "Please enter valid email"
//            binding.signEmail.requestFocus()
//            return
//        }
//
//        if (binding.signPass.text.toString().isEmpty()) {
//            binding.signPass.error = "Please enter password"
//            binding.signPass.requestFocus()
//            return
//        }
//
//        email = binding.signEmail.text.toString()
//        password = binding.signPass.text.toString()
//        name = binding.signName.text.toString()
//        phone = binding.signPhone.text.toString()
//        Log.e("check","$email--->$password--->$name--->$phone")
//    }
//
//    private fun signUpUser() {
//
//        var user = hashMapOf(
//            "Name" to name,
//            "Phone" to phone,
//            "Email" to email,
//            "Id" to "0",
//            "Dob" to "",
//            "Img_url" to "",
//            "Gender" to "",
//            "Weight" to "0",
//            "Height" to "0"
//        )
//        val users = db.collection("USERS")
//        val query = users.whereEqualTo("Email",email).get()
//        Log.e("check","$user")
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnSuccessListener {
//                users.document(email).set(user)
//                Toast.makeText(
//                                   this.context, "Registration Successful", Toast.LENGTH_SHORT).show()
//
//                view_Pager.setCurrentItem(1)
//            }
//            .addOnFailureListener {Toast.makeText(
//                        this.context, "Sign Up failed.Email already exists.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//            }
//
//    }



