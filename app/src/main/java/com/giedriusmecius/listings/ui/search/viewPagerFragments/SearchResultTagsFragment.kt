package com.giedriusmecius.listings.ui.search.viewPagerFragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.FragmentSearchResultTagsBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.search.SearchViewModel
import com.google.android.material.chip.Chip
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultTagsFragment :
    BaseFragment<FragmentSearchResultTagsBinding>(FragmentSearchResultTagsBinding::inflate) {
    private val vm by viewModels<SearchViewModel>({ requireParentFragment() })
    private val groupie = GroupieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = listOf("this", "that", "and")
        vm.searchTags.observe(viewLifecycleOwner) {
            displayTags(it)

        }

    }

    private fun displayTags(it: List<String>?) {
        with(binding) {
            searchResultTags.removeAllViewsInLayout()

            val newContext = ContextThemeWrapper(context, R.style.ListingsChip)
            it?.forEach {
                val chip = Chip(newContext, null, R.style.ListingsChip)
                chip.apply {
                    chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.warmPurple))
                    chipIcon = ContextCompat.getDrawable(context, R.drawable.item_chip_icon)
                    setTextAppearance(R.style.H5_Tag)
                    text = it
                }
                searchResultTags.addView(chip)
            }
        }
    }

    companion object {
        fun getInstance(): SearchResultTagsFragment {
            return SearchResultTagsFragment()
        }
    }
}