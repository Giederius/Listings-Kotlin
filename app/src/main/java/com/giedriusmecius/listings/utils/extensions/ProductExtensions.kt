package com.giedriusmecius.listings.utils.extensions

import com.giedriusmecius.listings.data.local.FilterOptions
import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.data.remote.model.product.Product

// not sure if this file should be here or with data.

fun List<Category>.toProductList(): List<Product> {
    val productList = mutableListOf<Product>()
    this.map {
        it.products.forEach { product ->
            productList.add(product)
        }
    }
    return productList.toList()
}

fun FilterOptions.copyTo(
    resultsProducts: List<Product>,
    resultsCategories: List<Category>
): FilterOptions {
    return this.copy(priceRange = listOf(
        resultsProducts.minOf { it.price },
        resultsProducts.maxOf { it.price }),
        userSelectedCategories = resultsCategories.map { it.title },
        allCategories = resultsCategories.map { it.title })
}

//fun List<Category>.toCategoryList(catList: List<Category>, productList: List<Product>): List<Category> {
//    return this = catList.map { cat ->
//        val filteredlist = productList.filter { it.category == cat.title }
//        Category(cat.title, filteredlist)
//    }
//    return mappedCats.toList()
//}