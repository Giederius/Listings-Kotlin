package com.giedriusmecius.listings.ui.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.giedriusmecius.listings.utils.UserPreferences
import com.giedriusmecius.listings.utils.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) :
    BaseViewModel<ProfileState, ProfileState.Event>(ProfileState()) {
    override fun handleState(newState: ProfileState) {
        when (newState.request) {
            ProfileState.Request.FetchUser -> {
                viewModelScope.launch {
                    fetchUserData()
                }
            }
            else -> {}
        }
    }

    private suspend fun fetchUserData() {
        val paymentMethods = userPreferences.getUserPaymentMethods().methods
        val userAddresses = userPreferences.getUserAddresses().addresses
        Log.d("MANOpayment", "$paymentMethods $userAddresses")
        delay(300)
        transition(ProfileState.Event.ReceivedUserDetails(paymentMethods, userAddresses))
    }
}