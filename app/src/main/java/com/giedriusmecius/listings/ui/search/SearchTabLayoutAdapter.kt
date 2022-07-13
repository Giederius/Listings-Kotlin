package com.giedriusmecius.listings.ui.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.giedriusmecius.listings.ui.home.fragments.DemoFragment

class SearchTabLayoutAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> DemoFragment.getInstance("Products")
        1 -> DemoFragment.getInstance("Collections")
        2 -> DemoFragment.getInstance("Stores")
        else -> DemoFragment.getInstance("Tags")
    }
}