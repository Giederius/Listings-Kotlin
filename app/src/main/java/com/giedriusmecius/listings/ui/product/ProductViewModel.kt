package com.giedriusmecius.listings.ui.product

import androidx.lifecycle.viewModelScope
import com.giedriusmecius.listings.data.remote.repository.ProductRepository
import com.giedriusmecius.listings.utils.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) :
    BaseViewModel<ProductState, ProductState.Event>(ProductState()) {
    override fun handleState(newState: ProductState) {
        when (val req = newState.request) {
            is ProductState.Request.FetchProduct -> {
                viewModelScope.launch {
                    getProduct(req.productId)
                }
            }
        }
    }

    private suspend fun getProduct(productId: Int) {
        val product = productRepository.getProduct(productId)
        val response = product.getOrNull()
        if (response != null) {
            transition(ProductState.Event.ReceivedProduct(response))
        }
    }
}