package com.giedriusmecius.listings.data.remote.model.category

import com.giedriusmecius.listings.data.remote.model.product.Product

data class Category(
    val title: String,
    val products: List<Product> = emptyList()
)