package com.giedriusmecius.listings.data.local

import com.giedriusmecius.listings.data.remote.model.CardType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val userName: String,
    val firstName: String,
    val lastName: String,
    val addresses: List<UserAddress>,
    val paymentMethods: List<PaymentMethod>,
    val userSizeNumber: Int,
    val userSizeLetter: String,
    val favoriteColor: String,
//    val mainDepartment: Department
)

@JsonClass(generateAdapter = true)
data class UserAddressResponse(
    val addresses: List<UserAddress> = emptyList()
)

@JsonClass(generateAdapter = true)
data class UserAddress(
    val addressLabel: String,
    val addressLine1: String?,
    val addressLine2: String,
    val postCode: String,
    val county: String,
    val country: String
)

@JsonClass(generateAdapter = true)
data class PaymentMethodResponse(
    val methods: List<PaymentMethod> = emptyList()
)

@JsonClass(generateAdapter = true)
data class PaymentMethod(
    val number: Long,
    val name: String,
    val type: CardType,
    val expDate: String,
    val ccv: Int
)

enum class CardType {
    VISA, MasterCard
}
