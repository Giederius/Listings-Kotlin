package com.giedriusmecius.listings.ui.splash

import com.giedriusmecius.listings.utils.state.BaseViewModel

class SplashViewModel : BaseViewModel<SplashState, SplashState.Event>(SplashState()) {
    override fun handleState(newState: SplashState) {
        when (newState.request) {
            else -> {}
        }
    }
}