package com.giedriusmecius.listings.ui.home

import com.giedriusmecius.listings.utils.state.State

data class HomeState(val request: Request? = null, val command: Command? = null) :
    State<HomeState, HomeState.Event> {
    sealed class Event {
        object ViewCreated : Event()
    }

    sealed class Request {}
    sealed class Command {
        object ChangeName: Command()
    }

    override fun reduce(event: Event): HomeState {
        return when (event) {
            Event.ViewCreated -> copy(command = Command.ChangeName)
        }
    }

    override fun clearCommandAndRequest(): HomeState {
        return copy(request = null, command = null)
    }
}