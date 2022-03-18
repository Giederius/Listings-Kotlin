package com.giedriusmecius.listings.data.remote.model

data class CC(
    val number: Int?,
    val name: String?,
    val type: CardType?,
    val expDate: String?,
    val ccv: Int?
)

enum class CardType {
    VISA, MASTERCARD
}
