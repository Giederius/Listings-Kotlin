package com.giedriusmecius.listings.ui.search.viewPagerFragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.giedriusmecius.listings.databinding.FragmentSearchResultsProductsBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.ProductItem
import com.giedriusmecius.listings.ui.search.SearchViewModel
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultProductsFragment :
    BaseFragment<FragmentSearchResultsProductsBinding>(FragmentSearchResultsProductsBinding::inflate) {

    private val groupieAdapter = GroupieAdapter()
    private val vm by viewModels<SearchViewModel>({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchResultRV.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = groupieAdapter
        }

        vm.searchResults.observe(viewLifecycleOwner) {
            setupUI(it)
        }
    }

    private fun setupUI(results: List<ProductItem>) {
        with(binding) {
            groupieAdapter.clear()
            groupieAdapter.addAll(results)
            searchResultRV.isGone = false
        }
    }

    companion object {
        fun getInstance(): SearchResultProductsFragment {
            return SearchResultProductsFragment()
        }
    }
}