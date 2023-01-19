package com.giedriusmecius.listings.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.giedriusmecius.listings.ui.home.fragments.DemoFragment
import com.giedriusmecius.listings.ui.home.fragments.FeaturedViewPagerFragment

class HomeTabLayoutAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> FeaturedViewPagerFragment.getInstance()
        1 -> DemoFragment.getInstance("Collection")
        2 -> DemoFragment.getInstance("Stores")
        else -> DemoFragment.getInstance("Tags")
    }
}