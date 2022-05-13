package com.giedriusmecius.listings.ui.profile

import com.giedriusmecius.listings.utils.state.BaseViewModel

class ProfileViewModel : BaseViewModel<ProfileState, ProfileState.Event>(ProfileState()) {
    override fun handleState(newState: ProfileState) {
        when (newState.request) {
            else -> {}
        }
    }
}