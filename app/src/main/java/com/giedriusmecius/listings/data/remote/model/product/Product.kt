package com.giedriusmecius.listings.data.remote.model.product

import java.io.Serializable

data class Product(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Float,
    val rating: Rating,
    val title: String
) : Serializable