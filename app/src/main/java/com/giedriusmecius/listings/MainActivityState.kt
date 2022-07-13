package com.giedriusmecius.listings

import com.giedriusmecius.listings.utils.state.State

data class MainActivityState(val request: Request? = null, val command: Command? = null) :
    State<MainActivityState, MainActivityState.Event> {

    sealed class Event {
        object ViewCreated : Event()
    }

    sealed class Request {
        object FetchProducts : Request()
    }

    sealed class Command {}

    override fun reduce(event: Event): MainActivityState {
        return when (event) {
            Event.ViewCreated -> copy(request = Request.FetchProducts)
        }
    }

    override fun clearCommandAndRequest(): MainActivityState {
        return copy(request = null, command = null)
    }
}