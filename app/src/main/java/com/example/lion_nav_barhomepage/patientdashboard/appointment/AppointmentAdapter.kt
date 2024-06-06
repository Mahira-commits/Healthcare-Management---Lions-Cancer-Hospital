package com.example.lion_nav_barhomepage.patientdashboard.appointment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.lion_nav_barhomepage.Appointment.appointment
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.doctors.DoctorsFragment
import com.example.lion_nav_barhomepage.doctors.DoctorsProfileFragment
import com.example.lion_nav_barhomepage.doctors.data
import org.w3c.dom.Text

class AppointmentAdapter(val context: PatientAppointmentsFragment, var app_list: ArrayList<appointment>) :
    RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id = itemView.findViewById<TextView>(R.id.aid)
        val doc_name = itemView.findViewById<TextView>(R.id.dname)
        val date = itemView.findViewById<TextView>(R.id.date)
        val timeslot = itemView.findViewById<TextView>(R.id.time)
        val status = itemView.findViewById<TextView>(R.id.status)
        val btnReschedule = itemView.findViewById<Button>(R.id.btnReschedule)
        val btnCancel = itemView.findViewById<Button>(R.id.btnCancel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = app_list[position]
        holder.id.text = appointment.appointment_id
        holder.doc_name.text = appointment.name
        holder.date.text = appointment.date
        holder.timeslot.text = appointment.timeslot
        holder.status.text = appointment.status

        holder.btnReschedule.setOnClickListener {
            context.rescheduleAppointment(appointment)
        }

        holder.btnCancel.setOnClickListener {
            context.cancelAppointment(appointment)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.appointment_listcard, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return app_list.size
    }
}
