package com.giedriusmecius.listings.ui.search

import android.util.Log
import androidx.lifecycle.viewModelScope
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
                searchList.add(0, req.query)
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
                val foundCatList: MutableList<Triple<String, String, String>> = mutableListOf()
                val foundProductList: MutableList<String> = mutableListOf()
                foundCatList.clear()
                foundProductList.clear()
                val products = req.products
                if (req.query.length >= 2) {
                    products.forEach { category ->
                        category.title.contains(req.query, ignoreCase = true).let {
                            if (it) {
                                foundCatList.add(
                                    Triple(
                                        category.title,
                                        category.products[0].image,
                                        "something"
                                    )
                                )
                            }
                        }
                        category.products.filter { product ->
                            product.title.contains(req.query, ignoreCase = true)
                                    || product.description.contains(req.query)
                                    || product.category.contains(req.query)
                        }.let {
                            it.forEach {
                                foundProductList.add(it.title)
                            }
                        }
                    }
                    transition(
                        SearchState.Event.ReceivedSuggestions(
                            foundCatList.toList(),
                            foundProductList.toList(),
                            req.query
                        )
                    )
                }
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