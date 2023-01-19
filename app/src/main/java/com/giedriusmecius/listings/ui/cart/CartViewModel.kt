package com.giedriusmecius.listings.ui.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.giedriusmecius.listings.data.remote.model.product.InCartProduct
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.data.remote.repository.ProductRepository
import com.giedriusmecius.listings.utils.UserPreferences
import com.giedriusmecius.listings.utils.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val productRepository: ProductRepository
) : BaseViewModel<CartState, CartState.Event>(CartState()) {

    private val fetchedProducts = MutableLiveData<List<Product>>()
    val products: MutableLiveData<List<Product>>
        get() = fetchedProducts

    override fun handleState(newState: CartState) {
        when (val req = newState.request) {
            is CartState.Request.FetchData -> {
//                val productList = userPreferences.getCartProducts()
                val productList = userPreferences.getAllProducts()[0].products
                fetchedProducts.value = productList
            }
            is CartState.Request.DeleteProductFromCart -> {
                val products = fetchedProducts.value?.toMutableList()
//                products?.remove(req.item)
                fetchedProducts.value = products
                Log.d("MANO", "REMOVED")
            }
        }
    }
}