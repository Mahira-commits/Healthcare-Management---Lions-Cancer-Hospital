package com.example.lion_nav_barhomepage.patientdashboard.vaccines

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.VaccineCertficatesFragment
import com.example.lion_nav_barhomepage.patientdashboard.diagnosis.DiagnosisAdapter
import com.example.lion_nav_barhomepage.patientdashboard.reports.pdfactivity


class VaccineAdapter (val context: VaccineCertficatesFragment, var vaccine_list: ArrayList<vaccine>,
                      private val view: VaccineAdapter.Click, private val rep: VaccineAdapter.replace):
    RecyclerView.Adapter<VaccineAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        val date : TextView = itemView.findViewById(R.id.ddate)
        val vaccineid : TextView = itemView.findViewById(R.id.vaccineid)
        val vname: TextView =  itemView.findViewById(R.id.vaccinename)
        val card : CardView = itemView.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.e("adapter","oncreate")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.vaccine_list, parent, false)
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("adapter","onbind")
        holder.vaccineid.text =  vaccine_list[position].vaccine_id
        holder.date.text = vaccine_list[position].date
        holder.vname.text = vaccine_list[position].vname

        holder.card.setOnClickListener {
            view.go_to_main_view(vaccine_list[position].pdf!!.toString(),vaccine_list[position].vaccine_id.toString())
            rep.replaceFragment(pdfactivity())
        }
        Log.e("on","array:${vaccine_list}")

    }

    override fun getItemCount(): Int {
        return vaccine_list.size
    }
    interface Click{
        fun go_to_main_view(url : String,r_id : String)

    }
    interface replace{
        fun replaceFragment(fragment: Fragment)


    }

}
