package com.giedriusmecius.listings.ui.product

import com.giedriusmecius.listings.utils.state.BaseViewModel

class ProductViewModel : BaseViewModel<ProductState, ProductState.Event>(ProductState()) {
    override fun handleState(newState: ProductState) {
        when (val req = newState.request) {

        }
    }
}