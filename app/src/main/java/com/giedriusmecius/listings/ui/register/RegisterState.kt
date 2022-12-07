package com.giedriusmecius.listings.ui.register

import com.giedriusmecius.listings.utils.state.State

data class RegisterState(val request: Request? = null, val command: Command? = null) :
    State<RegisterState, RegisterState.Event> {
    sealed class Event {

    }

    sealed class Command {

    }

    sealed class Request {

    }

    override fun reduce(event: Event): RegisterState {
        return when (event) {
            else -> copy()
        }
    }

    override fun clearCommandAndRequest(): RegisterState {
        return copy(request = null, command = null)
    }
}