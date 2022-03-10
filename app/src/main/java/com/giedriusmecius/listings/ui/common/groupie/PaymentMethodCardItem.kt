package com.giedriusmecius.listings.ui.common.groupie

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import coil.load
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemPaymentMethodCardBinding
import com.xwray.groupie.viewbinding.BindableItem

class PaymentMethodCardItem(val paymentType: PaymentType, val cardNumber: String) :
    BindableItem<ItemPaymentMethodCardBinding>() {
    override fun getLayout(): Int = R.layout.item_payment_method_card

    override fun initializeViewBinding(view: View): ItemPaymentMethodCardBinding =
        ItemPaymentMethodCardBinding.bind(view)

    @SuppressLint("ResourceAsColor")
    override fun bind(viewBinding: ItemPaymentMethodCardBinding, position: Int) {
        with(viewBinding) {
            Log.d("MANO", paymentType.bgColor.toString())
            paymenMethodCardContainer.setBackgroundColor(paymentType.bgColor)
            paymentMethodsCardImage.load(paymentType.cardImage)
            paymentMethodsCardType.text = paymentType.cardName
        }
    }


    enum class PaymentType(val cardImage: Int, val cardName: String, val bgColor: Int) {
        VISA(R.drawable.icon_visa, "Visa", getRandomColor()),
        MASTERCARD(R.drawable.icon_mastercard, "Mastercard", getRandomColor())
    }

}

fun getRandomColor(): Int {
    val colorList =
        arrayListOf(R.color.warmPurple, R.color.warmGreen, R.color.warmRed, R.color.warmYellow)
    return colorList.random()
}