package com.giedriusmecius.listings.ui.product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.data.remote.repository.ProductRepository
import com.giedriusmecius.listings.ui.cart.CartState
import com.giedriusmecius.listings.utils.UserPreferences
import com.giedriusmecius.listings.utils.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userPreferences: UserPreferences
) :
    BaseViewModel<ProductState, ProductState.Event>(ProductState()) {

    private val fetchedProducts = MutableLiveData<List<Product>>()
    val products: MutableLiveData<List<Product>>
        get() = fetchedProducts

    override fun handleState(newState: ProductState) {
        when (val req = newState.request) {
            is ProductState.Request.FetchData -> {
                val productList = userPreferences.getAllProducts()
                fetchedProducts.value = productList[0].products
            }
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