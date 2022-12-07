package com.giedriusmecius.listings.ui.register

import com.giedriusmecius.listings.utils.state.BaseViewModel

class RegisterViewModel : BaseViewModel<RegisterState, RegisterState.Event>(RegisterState()) {
    override fun handleState(newState: RegisterState) {
        return when(val req = newState.request) {

            else -> {}
        }
    }
}