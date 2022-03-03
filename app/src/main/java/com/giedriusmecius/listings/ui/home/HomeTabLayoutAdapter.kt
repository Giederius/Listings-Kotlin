package com.giedriusmecius.listings.ui.home

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.giedriusmecius.listings.ui.home.fragments.DemoFragment

class HomeTabLayoutAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        1 -> {
            DemoFragment.getInstance("featured")
        }
        2 -> DemoFragment.getInstance("latest")
        3 -> DemoFragment.getInstance("watchlist")
        else -> Fragment()
    }

}