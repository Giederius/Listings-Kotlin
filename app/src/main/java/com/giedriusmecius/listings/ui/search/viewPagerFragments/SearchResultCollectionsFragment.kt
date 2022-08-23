package com.giedriusmecius.listings.ui.search.viewPagerFragments

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.databinding.FragmentSearchResultCollectionsBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.ProfileDrawerListItem
import com.giedriusmecius.listings.ui.search.SearchViewModel
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultCollectionsFragment :
    BaseFragment<FragmentSearchResultCollectionsBinding>(FragmentSearchResultCollectionsBinding::inflate) {
    private val vm by viewModels<SearchViewModel>({ requireParentFragment() })
    private val groupie = GroupieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchResultCollections.apply {
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            adapter = groupie
        }

        vm.searchCollections.observe(viewLifecycleOwner) {
            setupData(it)
        }
    }

    fun setupData(collections: List<Category>) {
        groupie.clear()
        val mappedList = mutableListOf<ProfileDrawerListItem>()
        collections.map {
            ProfileDrawerListItem(
                count = it.products.size,
                title = it.title,
                img = it.products[0].image
            ).also {
                mappedList.add(it)
            }
        }
        groupie.addAll(mappedList)
    }

    companion object {
        fun getInstance(): SearchResultCollectionsFragment {
            return SearchResultCollectionsFragment()
        }
    }
}