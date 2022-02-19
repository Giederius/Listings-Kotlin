package com.giedriusmecius.listings.utils.state

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class BaseViewModel<T : State<T, E>, E>(initial: T) : ViewModel() {

    val machine by lazy {
        StateMachine<T, E>(initial)
    }

    val parentJob = Job()

    private val listeners = mutableListOf<StateListener<T, E>>()
    fun addListener(listener: StateListener<T, E>) = listeners.add(listener)
    fun removeListener(listener: StateListener<T, E>) = listeners.remove(listener)

    private val _listener = object : StateListener<T, E> {
        override fun onStateUpdated(oldState: T?, newState: T) {
            listeners.forEach { it.onStateUpdated(oldState, newState) }
            handleState(newState)
        }
    }

    init {
        machine.addListener(_listener)
    }

    abstract fun handleState(newState: T)

    fun transition(event: E) {
        machine.transition(event)
    }

    override fun onCleared() {
        machine.removeListener(_listener)
    }

}