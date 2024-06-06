package com.example.lion_nav_barhomepage.doctors

import android.content.Context
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.context_activity
import com.example.lion_nav_barhomepage.patientdashboard.appointment.PatientAppointmentsFragment

class DoctorsAdapter(
    val context: DoctorsFragment, var doc_list: ArrayList<data>, private val int: Click, private val rep: replace
) : RecyclerView.Adapter<DoctorsAdapter.ViewHolder>() {
     //var doc_list:List<data> = doctors().doc_list()

         override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0?.doc_name?.text =  doc_list[p1].name
        p0?.doc_spec?.text = doc_list[p1].spec
             p0.doc_img.load(doc_list[p1].img_url?.toUri()){
                 placeholder(R.drawable.loading_animation)
                 error(R.drawable.ic_broken_image)
             }
        //p0?.doc_img?.setImageResource=""
         p0.card.setOnClickListener{
             int.go_to_main(p1)
             rep.replaceFragment(DoctorsProfileFragment())


         }
             p0.details.setOnClickListener{
                 int.go_to_main(p1)
                 rep.replaceFragment(DoctorsProfileFragment())


             }
        Log.e("on","array:${doc_list}")
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.doctor_card, p0, false)
        return ViewHolder(v);
    }

    override fun getItemCount(): Int {
        return doc_list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doc_name = itemView.findViewById<TextView>(R.id.doctorname)
        val doc_spec = itemView.findViewById<TextView>(R.id.doctorspec)
        val doc_img = itemView.findViewById<ImageView>(R.id.doctorimg)
        val card =  itemView.findViewById<CardView>(R.id.card)
        val details = itemView.findViewById<Button>(R.id.detailsbutton)
    }
    interface Click{
        fun go_to_main(R_id : Int)
    }

 interface replace{
    fun replaceFragment(fragment: Fragment)


    }

}
    // To get the data to search Category
//    fun filterList(filteredDoctorList: ArrayList<data>) {
//        this.doc_list = filteredDoctorList;
//        notifyDataSetChanged();
//        Log.e("on","array:${doc_list}")
//    }
