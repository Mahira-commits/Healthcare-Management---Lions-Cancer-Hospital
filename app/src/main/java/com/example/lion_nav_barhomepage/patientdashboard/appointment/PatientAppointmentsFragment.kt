package com.example.lion_nav_barhomepage.patientdashboard.appointment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
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
import com.example.lion_nav_barhomepage.Appointment.appointment
import com.example.lion_nav_barhomepage.databinding.FragmentPatientAppointmentsBinding
import com.example.lion_nav_barhomepage.doctors.DoctorList
import com.example.lion_nav_barhomepage.doctors.DoctorsAdapter
import com.example.lion_nav_barhomepage.doctors.DoctorsFragment
import com.example.lion_nav_barhomepage.doctors.data
import com.example.lion_nav_barhomepage.patient_main_data
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import kotlin.reflect.typeOf
 lateinit  var appointmentlist : ArrayList<appointment>
class PatientAppointmentsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentPatientAppointmentsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        db = FirebaseFirestore.getInstance()
        _binding = FragmentPatientAppointmentsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.RecyclerView
        (activity as AppCompatActivity).supportActionBar?.title="Appointments"
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        appointmentlist = arrayListOf<appointment>()
        if (!DoctorsFragment().isConnected(requireContext())) {
            Toast.makeText(requireContext(), "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
           db.collection("Appointment").whereEqualTo("patient_emial", patient_main_data.email.toString())
               .get()
               .addOnSuccessListener {
               datas->

               for ( i in datas){
                       Log.e("list","${i}")
                       val obj = appointment(
                           i.data["appointment_id"].toString(),
                           i.data["name"].toString(),
                           i.data["spec"].toString(),
                           i.data["patient_emial"].toString(),
                           i.data["date"].toString(),
                           i.data["timeslot"].toString(),
                           i.data["status"].toString()
                       )
                       appointmentlist.add(obj)
               }
                   recyclerView.adapter = AppointmentAdapter(this@PatientAppointmentsFragment, appointmentlist)
                   progressDialog.dismiss()
                   Log.e("list","$appointmentlist")
           }
               .addOnFailureListener { exception ->
               Log.e("---->","Error getting documents: ")
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
        val filtered: ArrayList<appointment> = ArrayList()

        val courseAry: ArrayList<appointment> = appointmentlist

        for (eachCourse in courseAry) {
            if (eachCourse.date!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            }
            else if( eachCourse.name!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
                    .contains(text.toLowerCase())){
                filtered.add(eachCourse)
            }
            else if( eachCourse.status!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
                    .contains(text.toLowerCase())){
                filtered.add(eachCourse)
            }
            else if( eachCourse.timeslot!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
                    .contains(text.toLowerCase())){
                filtered.add(eachCourse)
            }
            else if( eachCourse.appointment_id!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
                    .contains(text.toLowerCase())){
                filtered.add(eachCourse)
            }
        }

        //calling a method of the adapter class and passing the filtered list
//        DoctorsAdapter(this,filtered)
        recyclerView.adapter = AppointmentAdapter(this@PatientAppointmentsFragment, filtered)
    }
    fun cancelAppointment(appointment: appointment) {
        // Show confirmation dialog before canceling
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to cancel this appointment?")
            .setPositiveButton("Yes") { _, _ ->
                val index = appointmentlist.indexOf(appointment)
                if (index != -1) {
                    appointmentlist.removeAt(index)
                    recyclerView.adapter?.notifyItemRemoved(index)
                    // Proceed to delete from the database
                    appointment.appointment_id?.let {
                        db.collection("Appointment").document(it)
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Appointment Cancelled", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error deleting appointment: ${e.message}", Toast.LENGTH_SHORT).show()
                                // Re-insert the item in case of failure
                                appointmentlist.add(index, appointment)
                                recyclerView.adapter?.notifyItemInserted(index)
                            }
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }


    fun rescheduleAppointment(appointment: appointment) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${selectedMonth + 1}/$selectedYear"
            showTimePicker(appointment, selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePicker(appointment: appointment, selectedDate: String) {
        val timePickerDialog = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
            val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            confirmReschedule(appointment, selectedDate, selectedTime)
        }, 12, 0, false)  // Use 'true' for 24-hour time format if desired

        timePickerDialog.show()
    }

    private fun confirmReschedule(appointment: appointment, newDate: String, newTime: String) {
        // Show a dialog to confirm the reschedule
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Reschedule to $newDate at $newTime?")
            .setCancelable(false)
            .setPositiveButton("Confirm") { dialog, id ->
                // Update the appointment in the local list
                val index = appointmentlist.indexOf(appointment)
                if (index != -1) {
                    val updatedAppointment = appointment.copy(date = newDate, timeslot = newTime)
                    appointmentlist[index] = updatedAppointment
                    recyclerView.adapter?.notifyItemChanged(index)
                    // Update in database
                    updateAppointmentInDatabase(appointment, newDate, newTime, index)
                }
            }
            .setNegativeButton("Cancel", { dialog, id ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Reschedule Appointment")
        alert.show()
    }

    private fun updateAppointmentInDatabase(appointment: appointment, newDate: String, newTime: String, index: Int) {
        val updatedAppointment = appointment.copy(date = newDate, timeslot = newTime)
        appointment.appointment_id?.let {
            db.collection("Appointment").document(it)
                .set(updatedAppointment)
                .addOnSuccessListener {
                    Toast.makeText(context, "Appointment rescheduled to $newDate at $newTime", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to reschedule appointment: ${e.message}", Toast.LENGTH_LONG).show()
                    // Revert changes in the case of failure
                    recyclerView.adapter?.notifyItemChanged(index)
                }
        }
    }






}