package com.giedriusmecius.listings.ui.profileDrawers

import android.util.Log
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
                    fetchCategories()
                }
            }
        }
    }

    private suspend fun fetchCategories() {
        val response = productRepo.getAllCategories()
        val data = response.getOrNull()
        if (!data.isNullOrEmpty()) {
            Log.d("MANO", data.toString())
            transition(ProfileDrawersState.Event.ReceivedProducts(data))
        }
    }

//    private suspend fun fetchProducts() {
//        val response = productRepo.getAllProducts()
//        val data = response.getOrNull()
//        if (!data.isNullOrEmpty()) {
//            transition(ProfileDrawersState.Event.ReceivedProducts(response.getOrNull()!!))
//        }
//    }
}
