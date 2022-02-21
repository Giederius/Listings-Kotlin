package com.giedriusmecius.listings.ui.splash

import android.util.Log
import com.giedriusmecius.listings.utils.state.State

data class SplashState(val request: Request? = null, val command: Command? = null) :
    State<SplashState, SplashState.Event> {
    sealed class Event {
        object ViewCreated : Event()
        object TappedButton : Event()
    }

    sealed class Request {}
    sealed class Command {
        object OpenHomeScreen : Command()
    }

    override fun reduce(event: Event): SplashState {
        return when (event) {
            Event.ViewCreated -> {
                Log.d("GM","state")
                copy(command = Command.OpenHomeScreen)
            }
            Event.TappedButton -> copy(command = Command.OpenHomeScreen)
        }
    }

    override fun clearCommandAndRequest(): SplashState {
        return copy(request = null, command = null)
    }
}