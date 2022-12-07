package com.giedriusmecius.listings.ui.login

import com.giedriusmecius.listings.utils.state.BaseViewModel

class LoginViewModel : BaseViewModel<LoginState, LoginState.Event>(LoginState()) {
    override fun handleState(newState: LoginState) {
        return when (val req = newState.request) {
            else -> {}
        }
    }
}