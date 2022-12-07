package com.giedriusmecius.listings.ui.login

import com.giedriusmecius.listings.utils.state.State

data class LoginState(val request: Request? = null, val command: Command? = null) :
    State<LoginState, LoginState.Event> {
    sealed class Event {

    }

    sealed class Command {

    }

    sealed class Request {

    }

    override fun reduce(event: Event): LoginState {
        return when (event) {
            else -> copy()
        }
    }

    override fun clearCommandAndRequest(): LoginState {
        return copy(request = null, command = null)
    }
}