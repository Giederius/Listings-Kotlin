package com.giedriusmecius.listings.ui.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.data.remote.repository.ProductRepository
import com.giedriusmecius.listings.utils.UserPreferences
import com.giedriusmecius.listings.utils.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val productRepository: ProductRepository
) :
    BaseViewModel<SearchState, SearchState.Event>(SearchState()) {
    override fun handleState(newState: SearchState) {
        when (val req = newState.request) {
            SearchState.Request.FetchData -> {
                val queryList = userPreferences.getRecentSearches()
                val productList = userPreferences.getAllProducts()
                viewModelScope.launch {
                    delay(500)
                    transition(SearchState.Event.ReceivedData(queryList, productList))
                }
            }
            is SearchState.Request.SaveSearchQueryAndGetResults -> {
                val searchList = req.recentSearchList.toMutableList()
                if (searchList[0] != req.query) {
                    searchList.add(0, req.query)
                }
                if (searchList.size > 5) {
                    searchList.removeLast()
                }
                userPreferences.saveRecentSearches(searchList)

                viewModelScope.launch {
                    getProductsForTesting()
                }
            }
            is SearchState.Request.RemoveSearchQuery -> {
                val searchList = req.recentSearchList.toMutableList()
                searchList.remove(req.query)
                userPreferences.saveRecentSearches(searchList)
                transition(SearchState.Event.SearchQueryRemoved(searchList.toList()))
            }
            is SearchState.Request.GenerateSuggestions -> {
                val foundCatList: MutableList<Category> = mutableListOf()
                val foundProductList: MutableList<Product> = mutableListOf()
                val products = req.products
                products.forEach { category ->
                    category.title.contains(req.query).let {
                        if (it) {
                            foundCatList.add(category)
                        }
                    }
                    category.products.filter { product ->
                        product.title.contains(req.query)
                                || product.description.contains(req.query)
                                || product.category.contains(req.query)
                    }.let {
                        foundProductList.addAll(it)
                    }
                }
                Log.d("MANO3", "$foundCatList, $foundProductList")
                Log.d("MANO3", "${foundCatList.size}, ${foundProductList.size}")
            }

            else -> {}
        }
    }

    private suspend fun getProductsForTesting() {
        val response = productRepository.getProducts()
        val data = response.getOrNull()
        if (!data.isNullOrEmpty()) {
            transition(SearchState.Event.ReceivedSearchResults(data))
        }
    }
}