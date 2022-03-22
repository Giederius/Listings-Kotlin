package com.giedriusmecius.listings.ui.profileDrawers

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.giedriusmecius.listings.data.ProductApi
import com.giedriusmecius.listings.utils.state.BaseViewModel
import kotlinx.coroutines.launch

class ProfileDrawersViewModel : BaseViewModel<ProfileDrawersState, ProfileDrawersState.Event>(
    ProfileDrawersState()
) {
    override fun handleState(newState: ProfileDrawersState) {
        when (val req = newState.request) {
            ProfileDrawersState.Request.FetchProducts -> {
                viewModelScope.launch {
                    fetchData()
                }
            }
        }
    }

    private suspend fun fetchData() {
        val response = ProductApi.retrofitService.getAllProducts()
        val data = response.isSuccessful
        if (response.body() != null) {
//            Log.d("MANO", response.toString())
//            Log.d("MANO", response.body().toString())
            transition(ProfileDrawersState.Event.ReceivedProducts(response.body()!!))
        }
    }
}