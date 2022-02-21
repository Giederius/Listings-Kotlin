package com.giedriusmecius.listings.ui.home

import com.giedriusmecius.listings.utils.state.BaseViewModel

class HomeViewModel : BaseViewModel<HomeState, HomeState.Event>(HomeState()) {
    override fun handleState(newState: HomeState) {
        when (newState.request) {
            else -> {}
        }
    }
}