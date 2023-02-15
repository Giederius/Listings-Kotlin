package com.giedriusmecius.listings.ui.product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.giedriusmecius.listings.data.checkoutManager.CheckoutManager
import com.giedriusmecius.listings.data.checkoutManager.CheckoutMapper
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.data.remote.repository.ProductRepository
import com.giedriusmecius.listings.utils.UserPreferences
import com.giedriusmecius.listings.utils.extensions.shareWhileObserved
import com.giedriusmecius.listings.utils.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userPreferences: UserPreferences,
    private val checkoutManager: CheckoutManager,
    private val checkoutMapper: CheckoutMapper
) :
    BaseViewModel<ProductState, ProductState.Event>(ProductState()) {
    //    private val _docsAnimalFlow = MutableSharedFlow<ViewState<ResponseBody>>()
    private val _inCartProducts = MutableSharedFlow<Int>()
    private var _product = MutableSharedFlow<Product>()

    private val fetchedProducts = MutableLiveData<List<Product>>()
    val products: MutableLiveData<List<Product>>
        get() = fetchedProducts

//    private val fetchedProduct = MutableLiveData<Product>()
//    val product: MutableLiveData<Product>
//        get() = fetchedProduct

//    private val inCartProducts = MutableLiveData<Int>()
//    val inCart: MutableLiveData<Int>
//        get() = inCartProducts

//    val docsPreferentialDeclarationFlow: SharedFlow<ViewState<ResponseBody>> =
//        _docsPreferentialDeclarationFlow.shareWhileObserved(viewModelScope)

    val inCartProducts: SharedFlow<Int> = _inCartProducts

//    val product: SharedFlow<Product> = _product.shareWhileObserved(viewModelScope)
    val product: SharedFlow<Product> = _product


    fun init(productId: Int) {
        viewModelScope.launch {
            getProduct(productId)
        }
    }

    fun reset() {
        checkoutManager.resetCart()
    }

    fun fetchProduct(productId: Int) {
        viewModelScope.launch {
//            _product.emit(ViewState.loading())
//            val viewState = when (val response = productRepository.getProduct(productId)) {
//                is ResponseResult.Success -> {
//                    ViewState.success(response.data)
//                }
//                is ResponseResult.Error -> {
//                    ViewState.error(response.message)
//                }
//            }
//            _product.emit(viewState)
            getProduct(productId)
        }
    }


    override fun handleState(newState: ProductState) {
        when (val req = newState.request) {
            is ProductState.Request.FetchData -> {
                val productList = userPreferences.getAllProducts()
                fetchedProducts.value = productList[0].products
            }
            is ProductState.Request.FetchProduct -> {
//                inCartProducts.value = userPreferences.getCartProducts().size ?: 0
                viewModelScope.launch {
//                    getProduct(req.productId)
                }
            }
            is ProductState.Request.HandleATC -> {
                Log.d("MANO", "add to cart vm")
                val item = checkoutMapper.mapProductToCartItem(req.product)
                val response = checkoutManager.addToCart(item)
                if (response) {
                    transition(ProductState.Event.SuccessfullyAddedToCart)
                    Log.d("MANO", "${checkoutManager.cartItems.size}")
                } else {
                    transition(ProductState.Event.ErrorAddingToCart)
                }
//                userPreferences.saveCartProducts(listOf(req.product))
            }
        }
    }

    private suspend fun getProduct(productId: Int) {
        val product = productRepository.getProduct(productId)
        val response = product.getOrNull()
        if (response != null) {
            delay(500)
            _product.emit(response)
        }
    }

    fun aTC(product :Product) {
        viewModelScope.launch {
            addProductToCart(product)
        }
    }

    suspend fun removeProductFromCart(product: Product) {
        val item = checkoutMapper.mapProductToCartItem(product)
        checkoutManager.removeFromCart(item)
        _inCartProducts.emit(checkoutManager.getCartSize())
    }

    suspend fun addProductToCart(product: Product) {
        val item = checkoutMapper.mapProductToCartItem(product)
        val response = checkoutManager.addToCart(item)
        Log.d("MANO", "atc2 $response")
        if (response) {
            Log.d("MANO", "atc2 cart size ${checkoutManager.getCartSize()}")
            _inCartProducts.emit(checkoutManager.getCartSize())
        } else {
            transition(ProductState.Event.ErrorAddingToCart)
        }
    }
}