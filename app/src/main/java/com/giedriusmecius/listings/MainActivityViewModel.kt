package com.giedriusmecius.listings

import androidx.lifecycle.viewModelScope
import com.giedriusmecius.listings.data.remote.repository.ProductRepository
import com.giedriusmecius.listings.utils.UserPreferences
import com.giedriusmecius.listings.utils.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val productRepo: ProductRepository,
    private val userPreferences: UserPreferences
) :
    BaseViewModel<MainActivityState, MainActivityState.Event>(
        MainActivityState()
    ) {
    override fun handleState(newState: MainActivityState) {
        when (newState.request) {
            MainActivityState.Request.FetchProducts -> {
                viewModelScope.launch {
                    fetchProducts()
                }
            }
            else -> {}
        }
    }

    private suspend fun fetchProducts() {
        val response = productRepo.getAllProducts()
        val data = response.getOrNull()
        if (!data.isNullOrEmpty())
            userPreferences.saveAllProducts(data)
    }
}