package com.giedriusmecius.listings.utils.state

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

typealias OnStateUpdate<T> = (boundState: T?, newState: T) -> Unit

fun <T : State<T, E>, E> BaseViewModel<T, E>.subscribeWithAutoDispose(
    lifecycleOwner: LifecycleOwner,
    onUpdate: OnStateUpdate<T>
) {
    val listener = object : StateListener<T, E> {
        override fun onStateUpdated(oldState: T?, newState: T) = onUpdate(oldState, newState)
    }
    lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun start() = addListener(listener)

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun end() = removeListener(listener)
    })

    onUpdate(null, machine.state)
}