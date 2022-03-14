package com.giedriusmecius.listings.ui.profileDrawers

import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.utils.state.State

data class ProfileDrawersState(val request: Request? = null, val command: Command? = null) :
    State<ProfileDrawersState, ProfileDrawersState.Event> {
    sealed class Event {
        object ViewCreated : Event()
        data class ReceivedProducts(val data: List<Product>) : Event()
    }

    sealed class Request {
        object FetchProducts : Request()
    }

    sealed class Command {
        data class DisplayData(val data: List<Product>) : Command()
    }

    override fun reduce(event: Event): ProfileDrawersState {
        return when (event) {
            Event.ViewCreated -> copy(request = Request.FetchProducts)
            is Event.ReceivedProducts -> copy(command = Command.DisplayData(event.data))
            else -> copy()
        }
    }

    override fun clearCommandAndRequest(): ProfileDrawersState {
        return copy(request = null, command = null)
    }
}