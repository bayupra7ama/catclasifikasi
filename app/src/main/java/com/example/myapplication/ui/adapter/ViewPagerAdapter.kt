package com.example.myapplication.ui.adapter

import MakananFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.ui.frament.PerawatanFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2 // Jumlah tab
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PerawatanFragment()
            1 -> MakananFragment()
            else -> PerawatanFragment()
        }
    }
}