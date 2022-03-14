package com.giedriusmecius.listings.ui.ProfileDrawers

import com.giedriusmecius.listings.utils.state.State

data class ProfileDrawersState(val request: Request? = null, val command: Command? = null) :
    State<ProfileDrawersState, ProfileDrawersState.Event> {
    sealed class Event {

    }

    sealed class Request {

    }

    sealed class Command {

    }

    override fun reduce(event: Event): ProfileDrawersState {
        TODO("Not yet implemented")
    }

    override fun clearCommandAndRequest(): ProfileDrawersState {
        TODO("Not yet implemented")
    }
}