package com.giedriusmecius.listings.ui.search

import android.util.Log
import com.giedriusmecius.listings.data.local.FilterData
import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.utils.extensions.copyTo
import com.giedriusmecius.listings.utils.state.State

data class SearchState(
    val request: Request? = null, val command: Command? = null,
    val products: List<Category>? = null,
    val recentSearchResults: List<String>? = null,
    val suggestionsProduct: List<String>? = null,
    val suggestionsCategory: List<Triple<String, String, String>>? = null,
    val isLoading: Boolean = false,
    val searchQuery: String? = null,
    val productResults: List<Product> = emptyList(),
    val categoryResults: List<Category> = emptyList(),
    val searchUIState: SearchType = SearchType.Search,
    val filteredCategory: List<Category> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    var filterData: FilterData = FilterData(),
) :
    State<SearchState, SearchState.Event> {
    sealed class Event {
        object ViewCreated : Event()
        data class ReceivedData(val recentSearchList: List<String>, val products: List<Category>) :
            Event()

        data class TypedInSearch(val query: String) : Event()
        data class PressedToSearch(val query: String) : Event()
        data class RemovedRecentSearch(val query: String, val queryIndex: Int) : Event()
        data class SearchQueryRemoved(val queries: List<String>) : Event()
        data class ReceivedSearchResults(
            val resultsProducts: List<Product>,
            val resultsCategories: List<Category>
        ) : Event()

        data class ReceivedSuggestions(
            val catResults: List<Triple<String, String, String>>,
            val productResults: List<String>,
            val query: String
        ) : Event()

        object TappedSearchField : Event()

        object TappedFilter : Event()
        object TappedSort : Event()

        data class ChangedSortBy(val sortBy: String) : Event()
        object DisabledSorting : Event()
        data class ChangedSearchUI(val type: SearchType) : Event()

        data class HandleFilterChange(val filterData: FilterData) : Event()
        data class ReceivedProductsWithFilter(
            val filteredCategory: List<Category>,
            val filteredProducts: List<Product>
        ) : Event()

        data class TappedOnProduct(val productID: Int) : Event()
        data class ReceivedProduct(val product: Product) : Event()
        data class ReceivedSortedProducts(val products: List<Product>) : Event()
    }

    sealed class Command {
        data class DisplayRecentSearches(val list: List<String>) : Command()
        data class DisplaySuggestions(
            val categorySuggestion: List<Triple<String, String, String>>,
            val simpleSuggestion: List<String>,
            val query: String
        ) : Command()

        data class UpdateRecentSearchQueries(val list: List<String>) : Command()
        data class DisplaySearchResults(val results: List<Product>) : Command()
        data class OpenSortDialog(val sortBy: String) : Command()
        data class OpenFilterDialog(val filterData: FilterData) : Command()

        object SetupSearchUI : Command()
        data class SetupSuggestionsUI(
            val categorySuggestion: List<Triple<String, String, String>>,
            val simpleSuggestion: List<String>,
            val query: String
        ) : Command()

        object SetupResultsUI : Command()

        data class OpenProduct(val product: Product) : Command()
    }

    sealed class Request {
        object FetchData : Request()
        data class GenerateSuggestions(val query: String, val products: List<Category>) : Request()
        data class SaveSearchQueryAndGetResults(
            val query: String,
            val recentSearchList: List<String>,
            val products: List<Category>,
            val sortBy: String
        ) : Request()

        data class RemoveSearchQuery(
            val query: String,
            val queryIndex: Int,
            val recentSearchList: List<String>
        ) : Request()

        data class ApplyFilteredOptions(
            val filterData: FilterData,
            val resultsProducts: List<Product>,
            val isSortingActive: Boolean,
            val sortBy: String,
        ) : Request()

        data class SortProductsBy(val filterData: FilterData, val products: List<Product>) :
            Request()

        data class FetchProduct(val productID: Int) : Request()
    }

    override fun reduce(event: Event): SearchState {
        return when (event) {
            Event.ViewCreated -> copy(request = Request.FetchData)
            is Event.ReceivedData -> copy(
                products = event.products,
                recentSearchResults = event.recentSearchList,
                command = Command.DisplayRecentSearches(event.recentSearchList)
            )
            is Event.TypedInSearch -> {
                if (searchUIState != SearchType.Results) {
                    copy(
                        request = Request.GenerateSuggestions(
                            event.query,
                            products ?: emptyList()
                        ),
                    )
                } else {
                    copy()
                }
            }
            is Event.PressedToSearch -> copy(
                request = Request.SaveSearchQueryAndGetResults(
                    event.query,
                    recentSearchResults ?: emptyList(),
                    products ?: emptyList(),
                    sortBy = filterData.sortData
                ),
                searchQuery = event.query,
                isLoading = true
            )
            is Event.RemovedRecentSearch -> copy(
                request = Request.RemoveSearchQuery(
                    event.query,
                    event.queryIndex,
                    recentSearchResults ?: emptyList()
                )
            )
            is Event.SearchQueryRemoved -> copy(
                recentSearchResults = event.queries,
                command = Command.UpdateRecentSearchQueries(event.queries)
            )
            is Event.ReceivedSearchResults -> {
                if (event.resultsCategories.isNotEmpty() && event.resultsProducts.isNotEmpty()) {
                    copy(
                        command = Command.DisplaySearchResults(event.resultsProducts),
                        productResults = event.resultsProducts,
                        searchUIState = SearchType.Results,
                        isLoading = false,
                        filterData = filterData.copy(
                            mainOptions = filterData.mainOptions.copyTo(
                                event.resultsProducts,
                                event.resultsCategories
                            ),
                            userOptions = filterData.mainOptions.copyTo(
                                event.resultsProducts,
                                event.resultsCategories
                            )
                        ),
                    )
                } else {
                    copy(
                        command = Command.DisplaySearchResults(emptyList()),
                        productResults = emptyList(),
                        searchUIState = SearchType.Results,
                        isLoading = false,
                    )
                }
            }
            is Event.ReceivedSuggestions -> {
                copy(
                    command = Command.DisplaySuggestions(
                        event.catResults,
                        event.productResults,
                        event.query
                    ),
                    searchQuery = event.query,
                    suggestionsCategory = event.catResults,
                    suggestionsProduct = event.productResults,
                    searchUIState = SearchType.Suggestions,
                    isLoading = false
                )
            }
            is Event.TappedSearchField -> {
                if (searchUIState == SearchType.Results) {
                    copy(
                        request = Request.GenerateSuggestions(
                            searchQuery ?: "",
                            products ?: emptyList()
                        )
                    )

                } else {
                    copy()
                }
            }
            Event.TappedSort -> copy(command = Command.OpenSortDialog(filterData.sortData))
            Event.DisabledSorting -> copy(filterData = filterData.copy(isSortingActive = false))
            Event.TappedFilter -> copy(
                command = Command.OpenFilterDialog(filterData)
            )
            is Event.ChangedSortBy -> {
                var newData = filterData.copy(
                    sortData = event.sortBy,
                    isSortingActive = event.sortBy != "az"
                )
                copy(
                    filterData = newData,
                    request = Request.SortProductsBy(newData, productResults)
                )
            }
            is Event.ChangedSearchUI -> {
                val newCommand: Command = when (event.type) {
                    SearchType.Search -> {
                        Command.SetupSearchUI
                    }
                    SearchType.Suggestions -> {
                        if (suggestionsCategory != null && suggestionsProduct != null && searchQuery != null) {
                            Command.SetupSuggestionsUI(
                                categorySuggestion = suggestionsCategory,
                                simpleSuggestion = suggestionsProduct,
                                query = searchQuery
                            )
                        } else {
                            Command.SetupSuggestionsUI(
                                categorySuggestion = listOf(Triple("", "", "")),
                                simpleSuggestion = emptyList(),
                                query = ""
                            )
                        }
                    }
                    SearchType.Results -> {
                        Command.SetupResultsUI
                    }
                }
                copy(command = newCommand, searchUIState = event.type)
            }
            is Event.HandleFilterChange -> {
                var newData = filterData.copy(
                    isFilterActive = event.filterData.userOptions != filterData.mainOptions,
                    userOptions = event.filterData.userOptions
                )
                copy(
                    filterData = newData,
                    request = Request.ApplyFilteredOptions(
                        newData,
                        productResults,
                        newData.isSortingActive,
                        newData.sortData
                    )
                )
            }
            is Event.ReceivedProductsWithFilter -> {
                copy(
                    filteredCategory = event.filteredCategory,
                    filteredProducts = event.filteredProducts,
                    command = Command.DisplaySearchResults(event.filteredProducts)
                )
            }
            is Event.TappedOnProduct -> copy(request = Request.FetchProduct(event.productID))
            is Event.ReceivedProduct -> copy(command = Command.OpenProduct(event.product))
            is Event.ReceivedSortedProducts -> copy(command = Command.DisplaySearchResults(event.products))
            else -> {
                copy()
            }
        }
    }

    override fun clearCommandAndRequest(): SearchState {
        return copy(request = null, command = null)
    }
}