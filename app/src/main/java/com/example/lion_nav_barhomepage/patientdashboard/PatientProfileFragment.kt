package com.example.lion_nav_barhomepage.patientdashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import coil.load
import com.example.lion_nav_barhomepage.IntroActivity
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.FragmentPatientProfileBinding
import com.example.lion_nav_barhomepage.patient_main_data
import com.example.lion_nav_barhomepage.patientdashboard.appointment.PatientAppointmentsFragment
import com.example.lion_nav_barhomepage.patientdashboard.diagnosis.DiagnosisFragment
import com.example.lion_nav_barhomepage.patientdashboard.reports.ReportsFragment
import com.example.lion_nav_barhomepage.patientdashboard.vaccines.VaccinesFragment
import com.example.lion_nav_barhomepage.sharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PatientProfileFragment : Fragment() {
    private var _binding: FragmentPatientProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPatientProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title = "Patient Profile"
        binding.app.setOnClickListener {
            replaceFragment(PatientAppointmentsFragment())
        }
        binding.vaccine.setOnClickListener {
            replaceFragment(VaccinesFragment())
        }
        binding.results.setOnClickListener {
            replaceFragment(ReportsFragment())
        }
        binding.diagnosis.setOnClickListener {
            replaceFragment(DiagnosisFragment())
        }
        binding.editButton.setOnClickListener {
            replaceFragment(EditProfileFragment())
        }
        binding.logoutButton.setOnClickListener {
            logout()
        }
        binding.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
        binding.pname.text = patient_main_data.name
        binding.pemail.text = patient_main_data.email
        binding.pid.text = patient_main_data.id
        val img_url = patient_main_data.img_url.toString()
        if (img_url == "") {
            binding.pimg.setImageResource(R.drawable.user_icon)
        } else {
            binding.pimg.load(img_url.toUri()) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout, fragment)
        fragmentTransaction.commit()
    }

    private fun logout() {
        sharedPreferences = this.requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("logged", false).apply()
        Toast.makeText(context, "Logging out from account", Toast.LENGTH_SHORT).show()
        val intent = Intent(this.context, IntroActivity::class.java)
        startActivity(intent)
    }

    private fun showDeleteConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Are you sure you want to delete your profile? This cannot be undone.")
            .setCancelable(false)
            .setPositiveButton("Delete") { dialog, id ->
                deleteUserFromDatabase()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Confirm Delete")
        alert.show()
    }

    private fun deleteUserFromDatabase() {
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        // Delete user data from Firestore
        patient_main_data.email?.let {
            db.collection("USERS").document(it)
                .delete()
                .addOnSuccessListener {
                    // After successfully deleting the user data from Firestore, delete the user from Authentication
                    user?.delete()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Profile and account deleted successfully.", Toast.LENGTH_LONG).show()
                            logout()  // Log out the user after deletion
                        } else {
                            Toast.makeText(context, "Failed to delete account: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error deleting profile: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

}