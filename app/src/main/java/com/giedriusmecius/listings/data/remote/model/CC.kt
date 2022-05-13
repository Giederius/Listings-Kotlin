package com.giedriusmecius.listings.data.remote.model

data class CC(
    var number: Long?,
    var name: String?,
    var type: CardType?,
    var expDate: String?,
    var ccv: Int?
)

enum class CardType {
    VISA, MASTERCARD
}
