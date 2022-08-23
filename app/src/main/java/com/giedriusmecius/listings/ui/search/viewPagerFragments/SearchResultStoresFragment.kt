package com.giedriusmecius.listings.ui.search.viewPagerFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.databinding.FragmentSearchResultStoresBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.composeItems.storeItemFullWidth
import com.giedriusmecius.listings.ui.common.groupie.ProfileDrawerItem
import com.giedriusmecius.listings.ui.search.SearchViewModel
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultStoresFragment :
    BaseFragment<FragmentSearchResultStoresBinding>(FragmentSearchResultStoresBinding::inflate) {
    private val vm by viewModels<SearchViewModel>({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_result_stores, container, false).apply {
            findViewById<ComposeView>(R.id.storeRecyclerView).setContent {
                storeSection()
            }
        }
    }

    // change to stores later on
    @Composable
    private fun storeSection() {
        val data: List<Category> by vm.searchCollections.observeAsState(listOf())
        Surface {
            LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
                items(data) {
                    storeItemFullWidth(
                        storeTitle = it.title,
                        storeDesc = "size ${it.products.size}"
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    fun storeRecyclerViewPreview() {
        storeSection()
    }

    companion object {
        fun getInstance(): SearchResultStoresFragment {
            return SearchResultStoresFragment()
        }
    }
}