package com.giedriusmecius.listings.ui.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.giedriusmecius.listings.ui.search.viewPagerFragments.SearchResultCollectionsFragment
import com.giedriusmecius.listings.ui.search.viewPagerFragments.SearchResultProductsFragment
import com.giedriusmecius.listings.ui.search.viewPagerFragments.SearchResultStoresFragment
import com.giedriusmecius.listings.ui.search.viewPagerFragments.SearchResultTagsFragment

class SearchTabLayoutAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> SearchResultProductsFragment.getInstance()
        1 -> SearchResultCollectionsFragment.getInstance()
        2 -> SearchResultStoresFragment.getInstance()
        else -> SearchResultTagsFragment.getInstance()
    }
}