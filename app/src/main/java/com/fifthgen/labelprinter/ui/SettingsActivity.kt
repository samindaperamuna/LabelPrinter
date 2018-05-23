package com.fifthgen.labelprinter.ui

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.fifthgen.labelprinter.R
import com.fifthgen.labelprinter.ui.fragment.LabelSettingsFragment
import com.fifthgen.labelprinter.ui.fragment.PrinterSettingsFragment

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Set activity title.
        if (supportActionBar != null) {
            supportActionBar!!.title = getString(R.string.main_title)
        }

        // Load the fragment manager into the ViewPager instance.
        val pagerAdapter = SettingsPagerAdapter(supportFragmentManager)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = pagerAdapter

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
    }

    /**
     * Adapter to handle the `[ViewPager]` fragment transitions.
     */
    private inner class SettingsPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {

            return if (position == 1) LabelSettingsFragment()
            else PrinterSettingsFragment()
        }

        override fun getCount(): Int {
            return FRAG_COUNT
        }

        override fun getPageTitle(position: Int): CharSequence? {

            return if (position == 1) getString(R.string.label_settings)
            else getString(R.string.printer_settings)
        }
    }

    companion object {

        private const val FRAG_COUNT = 2
    }
}