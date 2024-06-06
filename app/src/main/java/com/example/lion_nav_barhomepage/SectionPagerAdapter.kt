package com.example.lion_nav_barhomepage

import android.util.ArrayMap
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lion_nav_barhomepage.patientdashboard.reports.pdfactivity

class SectionPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = VaccineRequestFragment()
            1 -> fragment = VaccineCertficatesFragment()
        }
        return fragment as Fragment
    }
}


