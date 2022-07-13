package com.giedriusmecius.listings.ui.search

import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.utils.state.State

data class SearchState(
    val request: Request? = null, val command: Command? = null,
    val products: List<Category>? = null,
    val recentSearchResults: List<String>? = null,
    val isResultPage: Boolean = false
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
        data class ReceivedSearchResults(val results: List<Product>) : Event()
    }

    sealed class Command {
        data class DisplayRecentSearches(val list: List<String>) : Command()
        data class DisplaySuggestions(val query: String) : Command()
        data class UpdateRecentSearchQueries(val list: List<String>) : Command()
        data class DisplaySearchResults(val results: List<Product>) : Command()
    }

    sealed class Request {
        object FetchData : Request()
        data class GenerateSuggestions(val query: String, val products: List<Category>) : Request()
        data class SaveSearchQueryAndGetResults(
            val query: String,
            val recentSearchList: List<String>
        ) : Request()

        data class RemoveSearchQuery(
            val query: String,
            val queryIndex: Int,
            val recentSearchList: List<String>
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
            is Event.TypedInSearch -> copy(
                request = Request.GenerateSuggestions(
                    event.query,
                    products ?: emptyList()
                ), isResultPage = false
            )
            is Event.PressedToSearch -> copy(
                request = Request.SaveSearchQueryAndGetResults(
                    event.query,
                    recentSearchResults ?: emptyList()
                ),
                isResultPage = true
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
            is Event.ReceivedSearchResults -> copy(command = Command.DisplaySearchResults(event.results))
            else -> {
                copy()
            }
        }
    }

    override fun clearCommandAndRequest(): SearchState {
        return copy(request = null, command = null)
    }
}