package com.giedriusmecius.listings.data.remote.model.product

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Product(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Float,
    val rating: Rating,
    val title: String
) : Serializable