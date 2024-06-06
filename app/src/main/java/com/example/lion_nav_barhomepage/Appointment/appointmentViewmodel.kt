package com.example.lion_nav_barhomepage.Appointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lion_nav_barhomepage.doctors.data

class appointmentViewmodel : ViewModel() {
    private val _doctor = MutableLiveData<data>()
    val doctor: LiveData<data> = _doctor
    private var doctorname : String=""
    private var a_date : String=""
    private var a_time : String=""


    fun set_data(name: String, date: String, slot: String) {
        doctorname= name
        a_date= date
        a_time = slot
    }

    fun get_date(): List<String> {

        return listOf(doctorname, a_date, a_time)

    }
}