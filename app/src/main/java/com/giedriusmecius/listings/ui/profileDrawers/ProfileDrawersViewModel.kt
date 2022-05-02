package com.giedriusmecius.listings.ui.profileDrawers

import androidx.lifecycle.viewModelScope
import com.giedriusmecius.listings.data.remote.repository.ProductRepository
import com.giedriusmecius.listings.utils.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDrawersViewModel
@Inject constructor(
    private val productRepo: ProductRepository,
) : BaseViewModel<ProfileDrawersState, ProfileDrawersState.Event>
        (ProfileDrawersState()) {
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
        val response = productRepo.getAllProducts()
        val data = response.isSuccess
        if (response.getOrNull() != null && data) {
            transition(ProfileDrawersState.Event.ReceivedProducts(response.getOrNull()!!))
        }
    }
}