package com.giedriusmecius.listings.ui.profile

import com.giedriusmecius.listings.utils.state.State


data class ProfileState(val request: Request? = null, val command: Command? = null) :
    State<ProfileState, ProfileState.Event> {

    sealed class Event {
        object ViewCreated : Event()
    }

    sealed class Command {}

    sealed class Request {

    }

    override fun reduce(event: Event): ProfileState {
        return when (event) {
            Event.ViewCreated -> copy()
        }
    }

    override fun clearCommandAndRequest(): ProfileState {
        return copy(request = null, command = null)
    }


}