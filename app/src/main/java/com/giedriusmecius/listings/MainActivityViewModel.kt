package com.giedriusmecius.listings

import com.giedriusmecius.listings.utils.state.BaseViewModel

class MainActivityViewModel : BaseViewModel<MainActivityState, MainActivityState.Event>(
    MainActivityState()
) {
    override fun handleState(newState: MainActivityState) {
        when (newState.request) {

            else -> {}
        }
    }
}