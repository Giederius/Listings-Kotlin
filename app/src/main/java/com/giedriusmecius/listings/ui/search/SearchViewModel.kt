package com.giedriusmecius.listings.ui.search

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
                if (req.query != searchList[0]) {
                    searchList.add(0, req.query)
                    if (searchList.size > 5) {
                        searchList.removeLast()
                    }
                    userPreferences.saveRecentSearches(searchList)
                }
                handleSearch(req.query, req.products, true)
            }
            is SearchState.Request.RemoveSearchQuery -> {
                val searchList = req.recentSearchList.toMutableList()
                searchList.remove(req.query)
                userPreferences.saveRecentSearches(searchList)
                transition(SearchState.Event.SearchQueryRemoved(searchList.toList()))
            }
            is SearchState.Request.GenerateSuggestions -> {
                handleSearch(req.query, req.products, false)
            }
            is SearchState.Request.ApplyFilteredOptions -> {
                val priceRange: Pair<Float, Float> =
                    Pair(req.filterOptions.priceRange[0], req.filterOptions.priceRange[1])
                val categories: List<String> = req.filterOptions.userSelectedCategories
                val filteredByPrice: MutableList<Product> = mutableListOf()
                val filteredByCategory: MutableList<Category> = mutableListOf()

                req.resultsProducts.forEach { prod ->
                    if (prod.price in priceRange.first..priceRange.second) {
                        categories.find { it == prod.category }.let { cat ->
                            if (cat != null) {
                                filteredByPrice.add(prod)
                            }
                        }
                    }
                }
                categories.map { cat ->
                    val filteredProducts = filteredByPrice.filter { it.category == cat }
                    if (filteredByPrice.isNotEmpty()) {
                        filteredByCategory.add(Category(cat, filteredProducts))
                    }
                }
                transition(
                    SearchState.Event.ReceivedProductsWithFilter(
                        filteredByCategory,
                        filteredByPrice
                    )
                )
            }
            else -> {}
        }
    }

    private fun handleSearch(query: String, products: List<Category>, isResults: Boolean) {
        val foundCatList: MutableList<Triple<String, String, String>> = mutableListOf()
        val foundProductTitleList: MutableList<String> = mutableListOf()
        var foundProductList: MutableList<Product> = mutableListOf()
        foundCatList.clear()
        foundProductTitleList.clear()
        if (query.length >= 2) {
            products.forEach { category ->
                category.title.contains(query, ignoreCase = true).let {
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
                    product.title.contains(query, ignoreCase = true)
                            || product.description.contains(query)
                            || product.category.contains(query)
                }.let {
                    it.forEach { prod ->
                        foundProductTitleList.add(prod.title)
                        foundProductList.add(prod)

                        foundCatList.filter { cat ->
                            cat.first == prod.category
                        }.also {
                            if (it.isEmpty()) {
                                foundCatList.add(Triple(prod.category, prod.image, "sth"))
                            }
                        }
                    }
                }
            }

            foundProductList.sortBy { it.title }
            foundCatList.sortBy { it.first }
            foundProductTitleList.sortBy { it }

            // mapping found products to categories for filtering and displaying collections?
            val productsInCategories = foundCatList.map { cat ->
                val filteredProducts = foundProductList.filter { it.category == cat.first }
                Category(cat.first, filteredProducts)
            }

            if (isResults) {
                transition(
                    SearchState.Event.ReceivedSearchResults(
                        foundProductList.toList(),
                        productsInCategories
                    )
                )
            } else {
                transition(
                    SearchState.Event.ReceivedSuggestions(
                        foundCatList.toList(),
                        foundProductTitleList.toList(),
                        query
                    )
                )
            }
        }
    }

//    private suspend fun getProductsForTesting() {
//        val response = productRepository.getProducts()
//        val data = response.getOrNull()
//        if (!data.isNullOrEmpty()) {
//            transition(SearchState.Event.ReceivedSearchResults(data))
//        }
//    }
}