package com.giedriusmecius.listings.ui.profileDrawers

import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.utils.state.State

data class ProfileDrawersState(
    val request: Request? = null,
    val command: Command? = null,
    val data: List<Category> = emptyList(),
    val isLoading: Boolean = true
) : State<ProfileDrawersState, ProfileDrawersState.Event> {
    sealed class Event {
        object ViewCreated : Event()
        data class ReceivedProducts(val data: List<Category>) : Event()
        data class TypedInSearch(val query: String) : Event()
        object TappedAdjustDrawers : Event()
        object TappedHorizontalLayout : Event()
        object TappedGridLayout : Event()
        object TappedListLayout : Event()
    }

    sealed class Request {
        object FetchProducts : Request()
    }

    sealed class Command {
        data class DisplayData(val data: List<Category>) : Command()
        data class FilterSearch(val query: String) : Command()
        object OpenAdjustDrawers : Command()
        object ChangeLayoutHorizontal : Command()
        object ChangeLayoutGrid : Command()
        object ChangeLayoutList : Command()
    }

    override fun reduce(event: Event): ProfileDrawersState {
        return when (event) {
            Event.ViewCreated -> copy(request = Request.FetchProducts, isLoading = true)
            is Event.ReceivedProducts -> copy(
                command = Command.DisplayData(event.data),
                data = event.data,
                isLoading = false
            )
            is Event.TypedInSearch -> copy(command = Command.FilterSearch(event.query))
            Event.TappedAdjustDrawers -> copy(command = Command.OpenAdjustDrawers)
            Event.TappedHorizontalLayout -> copy(command = Command.ChangeLayoutHorizontal)
            Event.TappedGridLayout -> copy(command = Command.ChangeLayoutGrid)
            Event.TappedListLayout -> copy(command = Command.ChangeLayoutList)
        }
    }

    override fun clearCommandAndRequest(): ProfileDrawersState {
        return copy(command = null, request = null)
    }
}