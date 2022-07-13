package com.giedriusmecius.listings.ui.home

import com.giedriusmecius.listings.utils.state.State

data class HomeState(val request: Request? = null, val command: Command? = null) :
    State<HomeState, HomeState.Event> {
    sealed class Event {
        object ViewCreated : Event()
        object TappedSearch : Event()
    }

    sealed class Request {}
    sealed class Command {
        object OpenSearch : Command()
    }

    override fun reduce(event: Event): HomeState {
        return when (event) {
            Event.TappedSearch -> copy(command = Command.OpenSearch)
            else -> {
                copy()
            }
        }
    }

    override fun clearCommandAndRequest(): HomeState {
        return copy(request = null, command = null)
    }
}