package com.giedriusmecius.listings.data.remote.model.category

import com.giedriusmecius.listings.data.remote.model.product.Product
import java.io.Serializable

data class Category(
    val title: String,
    val products: List<Product> = emptyList()
) : Serializable