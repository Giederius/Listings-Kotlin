package com.giedriusmecius.listings.data.remote.model.product

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class InCartProduct(
    val id: Int,
    val title: String,
    val img: String,
    val price: Float,
    val discountedPrice: Float,
    val quantity: Int,
    val size: String,
    val color: String
) : Serializable