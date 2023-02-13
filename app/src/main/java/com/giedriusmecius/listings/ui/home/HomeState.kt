package com.giedriusmecius.listings.ui.home

import android.util.Log
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.utils.state.State

data class HomeState(val request: Request? = null, val command: Command? = null) :
    State<HomeState, HomeState.Event> {
    sealed class Event {
        object ViewCreated : Event()
        object TappedSearch : Event()
        object OpenedFeatured : Event()
        data class ReceivedFeatured(val featured: List<Product>) : Event()
    }

    sealed class Request {
        object FetchFeatured : Request()
    }

    sealed class Command {
        object OpenSearch : Command()
        data class HandleFeatured(val featured: List<Product>) : Command()
    }

    override fun reduce(event: Event): HomeState {
        return when (event) {
            Event.ViewCreated -> {
                copy()
            }
            Event.TappedSearch -> copy(command = Command.OpenSearch)
            Event.OpenedFeatured -> copy(request = Request.FetchFeatured)
            is Event.ReceivedFeatured -> copy(command = Command.HandleFeatured(event.featured))
            else -> {
                copy()
            }
        }
    }

    override fun clearCommandAndRequest(): HomeState {
        return copy(request = null, command = null)
    }
}