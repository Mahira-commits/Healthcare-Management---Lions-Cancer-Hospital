package com.example.lion_nav_barhomepage.patientdashboard.vaccines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.denzcoskun.imageslider.adapters.ViewPagerAdapter
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.SectionPagerAdapter
import com.example.lion_nav_barhomepage.VaccineCertficatesFragment
import com.example.lion_nav_barhomepage.VaccineRequestFragment
import com.example.lion_nav_barhomepage.databinding.FragmentVaccinesBinding
import com.example.lion_nav_barhomepage.patientdashboard.reports.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class VaccinesFragment : Fragment(R.layout.fragment_vaccines) {
    private lateinit var binding: FragmentVaccinesBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab1,
            R.string.tab2
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title="Vaccines"
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVaccinesBinding.bind(view)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}