package com.giedriusmecius.listings.ui.profileDrawers

import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.utils.state.State

data class ProfileDrawersState(
    val request: Request? = null,
    val command: Command? = null,
    val data: List<Product>? = null
) :
    State<ProfileDrawersState, ProfileDrawersState.Event> {
    sealed class Event {
        object ViewCreated : Event()
        data class ReceivedProducts(val data: List<Product>) : Event()
        data class TypedInSearch(val query: String) : Event()
        object TappedAdjustDrawers : Event()
    }

    sealed class Request {
        object FetchProducts : Request()
    }

    sealed class Command {
        data class DisplayData(val data: List<Product>) : Command()
        data class FilterSearch(val query: String) : Command()
        object OpenAdjustDrawers : Command()
    }

    override fun reduce(event: Event): ProfileDrawersState {
        return when (event) {
            Event.ViewCreated -> copy(request = Request.FetchProducts)
            is Event.ReceivedProducts -> copy(
                command = Command.DisplayData(event.data),
                data = event.data
            )
            is Event.TypedInSearch -> copy(command = Command.FilterSearch(event.query))
            Event.TappedAdjustDrawers -> copy(command = Command.OpenAdjustDrawers)
            else -> copy()
        }
    }

    override fun clearCommandAndRequest(): ProfileDrawersState {
        return copy(request = null, command = null)
    }
}