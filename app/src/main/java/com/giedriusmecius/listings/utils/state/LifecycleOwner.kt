package com.giedriusmecius.listings.utils.state

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

typealias OnStateUpdate<T> = (boundState: T?, newState: T) -> Unit

fun <T : State<T, E>, E> BaseViewModel<T, E>.subscribeWithAutoDispose(
    lifecycleOwner: LifecycleOwner,
    onUpdate: OnStateUpdate<T>
) {
    val listener = object : StateListener<T, E> {
        override fun onStateUpdated(oldState: T?, newState: T) = onUpdate(oldState, newState)
    }
    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            Log.d("GMSTATE","START")
            addListener(listener)
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            Log.d("GMSTATE","STOP")
            removeListener(listener)
        }
    })

//    lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
//        //todo cia sudas cj
//        @OnLifecycleEvent(Lifecycle.Event.ON_START)
//        fun start() = addListener(listener)
//
//        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//        fun stop() = removeListener(listener)
//    })

    onUpdate(null, machine.state)
}