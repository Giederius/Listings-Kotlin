package com.giedriusmecius.listings.utils.extensions

import android.util.Log
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Float.toCurrency(): String {
    val locale = Locale.getDefault()
    val currency = Currency.getInstance(locale)
    val currencyFormat = NumberFormat.getCurrencyInstance(locale)

    currencyFormat.currency = currency
    return currencyFormat.format(this)
}