package com.giedriusmecius.listings.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.giedriusmecius.listings.data.local.FilterData
import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.data.remote.repository.ProductRepository
import com.giedriusmecius.listings.ui.common.groupie.ProductItem
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

    private var mutableSearchResults = MutableLiveData<List<ProductItem>>()
    val searchResults: MutableLiveData<List<ProductItem>>
        get() = mutableSearchResults

    private var mutableSearchCollections = MutableLiveData<List<Category>>()
    val searchCollections: MutableLiveData<List<Category>>
        get() = mutableSearchCollections

//    private var mutableSearchStores = MutableLiveData<List<Category>>()
//    val searchStores: MutableLiveData<List<Category>>
//        get() = mutableSearchStores

    // use from suggestions??
    private var mutableSearchTags = MutableLiveData<List<String>>()
    val searchTags: MutableLiveData<List<String>>
        get() = mutableSearchTags

    private var mutableResultFilterData = MutableLiveData<FilterData>()
    val resultFilterData: MutableLiveData<FilterData>
        get() = mutableResultFilterData

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
                resultFilterData.value = req.filterData
                val priceRange: Pair<Float, Float> =
                    Pair(
                        req.filterData.userOptions.priceRange[0],
                        req.filterData.userOptions.priceRange[1]
                    )
                val categories: List<String> = req.filterData.userOptions.userSelectedCategories
                var filteredByPrice: MutableList<Product> = mutableListOf()
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
                if (req.isSortingActive) {
                    filteredByPrice = handleSorting(req.sortBy, filteredByPrice).toMutableList()
                }
                transition(
                    SearchState.Event.ReceivedProductsWithFilter(
                        filteredByCategory,
                        filteredByPrice
                    )
                )
            }
            is SearchState.Request.SortProductsBy -> {
                Log.d("MANOVM", "${req.filterData}")
                resultFilterData.postValue(req.filterData)
                val products = handleSorting(req.filterData.sortData, req.products)
                transition(SearchState.Event.ReceivedSortedProducts(products))
            }
            is SearchState.Request.FetchProduct -> {
                viewModelScope.launch {
                    getProduct(req.productID)
                }
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

            mutableSearchCollections.value = productsInCategories
            searchTags.value = foundProductTitleList

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

    private suspend fun getProduct(productId: Int) {
        val product = productRepository.getProduct(productId)
        val response = product.getOrNull()
        if (response != null) {
            transition(SearchState.Event.ReceivedProduct(response))
        }
    }

    private fun handleSorting(sortBy: String, products: List<Product>): List<Product> {
        var sortedProducts: List<Product> = emptyList()
        when (sortBy) {
            "az" -> {
                sortedProducts = products.sortedBy { it.title }
            }
            "za" -> {
                sortedProducts = products.sortedByDescending { it.title }
            }
            "priceLow" -> {
                sortedProducts = products.sortedBy { it.price }
            }
            "priceHigh" -> {
                sortedProducts = products.sortedByDescending { it.price }
            }
        }
        return sortedProducts
    }

//    private suspend fun getProductsForTesting() {
//        val response = productRepository.getProducts()
//        val data = response.getOrNull()
//        if (!data.isNullOrEmpty()) {
//            transition(SearchState.Event.ReceivedSearchResults(data))
//        }
//    }
}