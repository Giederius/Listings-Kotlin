package com.giedriusmecius.listings.data.local

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)

// do user setup when logging in?
data class User(
    val userName: String = "Ava",
    val firstName: String = "Johnson",
    val lastName: String = "",
    val addresses: List<UserAddress> = emptyList(),
    val paymentMethods: List<PaymentMethod> = emptyList(),
    val userSize: Size = Size(0, ""),
    val favoriteColor: Pair<String,String> = Pair("", ""),
    val mainDepartment: String = ""
) : Serializable

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
    val zipCode: String,
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

@JsonClass(generateAdapter = true)
data class Size(
    val us: Int,
    val eu: String,
) : Serializable

enum class CardType {
    VISA, MASTERCARD
}
