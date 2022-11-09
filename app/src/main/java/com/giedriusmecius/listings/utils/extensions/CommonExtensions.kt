package com.giedriusmecius.listings.utils.extensions

import android.content.res.Resources
import com.giedriusmecius.listings.data.remote.model.product.Product

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

fun calculateTotalPrice(data: List<Product>?): Float {
    var price = 0F
    data?.forEach {
        price += it.price
    }
    return price
}