package com.giedriusmecius.listings.ui.checkout

import com.giedriusmecius.listings.utils.state.State

data class CheckoutState(val command: Command? = null, val request: Request? = null) :
    State<CheckoutState, CheckoutState.Event> {
    sealed class Event {
        object ViewCreated : Event()
    }

    sealed class Command {

    }

    sealed class Request {
        object FetchData : Request()
    }

    override fun reduce(event: Event): CheckoutState {
        return when (event) {
            Event.ViewCreated -> copy(request = Request.FetchData)
            else -> copy()
        }
    }

    override fun clearCommandAndRequest(): CheckoutState {
        return copy(command = null, request = null)
    }

}