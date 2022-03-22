package com.giedriusmecius.listings.ui.common.groupie

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import coil.load
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemPaymentMethodCardBinding
import com.xwray.groupie.viewbinding.BindableItem

data class PaymentMethodCardItem(private val paymentType: PaymentType, val cardNumber: String) :
    BindableItem<ItemPaymentMethodCardBinding>() {
    override fun getLayout(): Int = R.layout.item_payment_method_card

    override fun initializeViewBinding(view: View): ItemPaymentMethodCardBinding =
        ItemPaymentMethodCardBinding.bind(view)

    @SuppressLint("ResourceAsColor")
    override fun bind(viewBinding: ItemPaymentMethodCardBinding, position: Int) {
        with(viewBinding) {
            paymentMethodCardContainer.setCardBackgroundColor(paymentType.bgColor)
            paymentMethodsCardImage.load(paymentType.cardImage)
            paymentMethodsCardType.text = paymentType.cardName
            paymentMethodsCardNumber.text = cardNumber
        }
    }

    enum class PaymentType(val cardImage: Int, val cardName: String, val bgColor: Int) {
        VISA(R.drawable.icon_visa, "Visa", getRandomColor()),
        MASTERCARD(R.drawable.icon_mastercard, "Mastercard", getRandomColor())
    }

}

// todo fix this somehow ??
fun getRandomColor(): Int = arrayListOf(
    Color.parseColor("#FFF1DC"),
    Color.parseColor("#D2CEF6"),
    Color.parseColor("#D7FAF4"),
    Color.parseColor("#FEEFEF"),
).random()