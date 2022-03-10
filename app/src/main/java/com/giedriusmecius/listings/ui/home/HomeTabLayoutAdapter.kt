package com.giedriusmecius.listings.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.giedriusmecius.listings.ui.home.fragments.DemoFragment

class HomeTabLayoutAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> DemoFragment.getInstance("featured")
        1 -> DemoFragment.getInstance("latest")
        else -> DemoFragment.getInstance("watchlist")
    }
}