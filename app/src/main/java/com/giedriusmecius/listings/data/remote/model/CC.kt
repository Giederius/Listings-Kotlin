package com.giedriusmecius.listings.data.remote.model

import java.io.Serializable

data class CC(
    var number: Long?,
    var name: String?,
    var type: CardType?,
    var expDate: String?,
    var ccv: Int?
) : Serializable

enum class CardType {
    VISA, MASTERCARD
}
