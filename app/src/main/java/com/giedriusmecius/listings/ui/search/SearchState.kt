package com.giedriusmecius.listings.ui.search

import android.util.Log
import com.giedriusmecius.listings.data.local.FilterOptions
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
    val sortProductsBy: String = "az",
    val isSortingActive: Boolean = false,
    val searchUIState: SearchType = SearchType.Search,
    val mainSearchFilter: FilterOptions = FilterOptions(),
    val userSearchFilter: FilterOptions = FilterOptions(),
    val filteredCategory: List<Category> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    val isFilteringActive: Boolean = false
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

        data class HandleFilterChange(val filterOptions: FilterOptions) : Event()
        data class ReceivedProductsWithFilter(
            val filteredCategory: List<Category>,
            val filteredProducts: List<Product>
        ) : Event()
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
        data class OpenFilterDialog(
            val filterOptions: FilterOptions,
            val mainFilterOptions: FilterOptions
        ) : Command()

        data class SortProductsBy(val sortBy: String, val products: List<Product>) : Command()

        object SetupSearchUI : Command()
        data class SetupSuggestionsUI(
            val categorySuggestion: List<Triple<String, String, String>>,
            val simpleSuggestion: List<String>,
            val query: String
        ) : Command()

        object SetupResultsUI : Command()
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
            val filterOptions: FilterOptions,
            val resultsProducts: List<Product>
        ) : Request()
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
                    sortBy = sortProductsBy
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
                        mainSearchFilter = mainSearchFilter.copyTo(
                            event.resultsProducts,
                            event.resultsCategories
                        ),
                        userSearchFilter = mainSearchFilter.copyTo(
                            event.resultsProducts,
                            event.resultsCategories
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
                Log.d(
                    "MANOtapped",
                    "${suggestionsCategory?.size} ${suggestionsProduct?.size} $searchQuery"
                )
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
            Event.TappedSort -> copy(command = Command.OpenSortDialog(sortProductsBy))
            Event.DisabledSorting -> copy(isSortingActive = false)
            Event.TappedFilter -> copy(
                command = Command.OpenFilterDialog(
                    userSearchFilter,
                    mainSearchFilter
                )
            )
            is Event.ChangedSortBy -> copy(
                sortProductsBy = event.sortBy,
                isSortingActive = event.sortBy != "az",
                command = Command.SortProductsBy(event.sortBy, productResults)
            )
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
                copy(
                    isFilteringActive = event.filterOptions != mainSearchFilter,
                    userSearchFilter = event.filterOptions,
                    request = Request.ApplyFilteredOptions(
                        event.filterOptions,
                        productResults,
                    )
                )
            }
            is Event.ReceivedProductsWithFilter -> {
                val newCommand = if (isSortingActive) {
                    Command.SortProductsBy(sortProductsBy, event.filteredProducts)
                } else {
                    Command.DisplaySearchResults(event.filteredProducts)
                }
                copy(
                    filteredCategory = event.filteredCategory,
                    filteredProducts = event.filteredProducts,
                    command = newCommand
                )
            }

            else -> {
                copy()
            }
        }
    }

    override fun clearCommandAndRequest(): SearchState {
        return copy(request = null, command = null)
    }
}