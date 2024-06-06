package com.example.lion_nav_barhomepage.Vaccines


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                VaccinesAdminFragment()
            }
            1->{
                VaccineRequestAdminFragment()
            }
            2->{
                VaccineCertificateFragment()
            }
            else -> {
                createFragment(position)
            }
        }
    }
}