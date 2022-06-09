package com.giedriusmecius.listings.data.local

import com.squareup.moshi.JsonClass
import java.io.Serializable

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
    val firstName: String,
    val lastName: String,
    val addressStreetName: String,
    val addressHouseNumber: String,
    val addressLine2: String?,
    val zipCode: Int,
    val city: String,
    val county: String,
    val state: String,
    val country: String,
    val email: String
) : Serializable

@JsonClass(generateAdapter = true)
data class PaymentMethodResponse(
    val methods: List<PaymentMethod> = emptyList()
)

@JsonClass(generateAdapter = true)
data class PaymentMethod(
    val number: Long?,
    val name: String?,
    val type: CardType?,
    val expDate: String?,
    val ccv: Int?
) : Serializable

enum class CardType {
    VISA, MASTERCARD
}
