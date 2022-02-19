package com.giedriusmecius.listings.utils.state

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class BaseViewModel<T : State<T, E>, E>(initial: T) : ViewModel() {

    val machine by lazy {
        StateMachine<T, E>(initial)
    }

    val parentJob = Job()


}