package com.giedriusmecius.listings.data

interface MapperToProductList<T, R> {
    fun mapToProductList(T: T): R
    fun mapToCategoryList(R: R): T
}



