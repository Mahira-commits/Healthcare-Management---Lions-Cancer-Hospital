package com.example.lion_nav_barhomepage.about

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.patientdashboard.diagnosis.DiagnosisAdapter
import com.example.lion_nav_barhomepage.patientdashboard.diagnosis.PresFragment
import com.example.lion_nav_barhomepage.patientdashboard.diagnosis.diagnosis

class TeamAdapter(val context: OurTeamFragment,var team_list: ArrayList<team>,  private val view: TeamAdapter.Click):
RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        val name : TextView = itemView.findViewById(R.id.memname)
        val designation : TextView = itemView.findViewById(R.id.memdesignation)
        val image: ImageView = itemView.findViewById(R.id.memimage)
        val number : TextView = itemView.findViewById(R.id.teamnumber)
        val about: TextView =  itemView.findViewById(R.id.teamabout)
        val card : CardView = itemView.findViewById(R.id.card)
        val linear : ImageView = itemView.findViewById(R.id.view)
        val relative: RelativeLayout = itemView.findViewById(R.id.relative)
        val relativeLayout: RelativeLayout = itemView.findViewById(R.id.click)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.e("adapter","oncreate")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.aboutcard, parent, false)
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("adapter","onbind")
        holder.name.text =  team_list[position].name
        holder.designation.text = team_list[position].designation
        holder.number.text = "Phone number :  " + team_list[position].number
        holder.about.text = team_list[position].about
        holder.image.load(team_list[position].image?.toUri()){
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
        val isVisible: Boolean = team_list[position].visibility


        holder.relative.visibility = if(isVisible) View.VISIBLE else View.GONE

        holder.card.setOnClickListener{
            team_list[position].visibility = !team_list[position].visibility
            notifyItemChanged(position)
        }



    }

    override fun getItemCount(): Int {
        return team_list.size
    }
    interface Click{
        fun go_to_main_view(url : String,r_id : String)

    }


}