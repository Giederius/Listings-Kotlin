package com.giedriusmecius.listings.ui.common.groupie

import android.annotation.SuppressLint
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import android.widget.PopupWindow
import androidx.annotation.MenuRes
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemProfileAddressBinding
import com.xwray.groupie.viewbinding.BindableItem

data class ProfileAddressItem(
    val houseNumber: String,
    val streetName: String,
    val county: String,
    val state: String,
    val country: String,
    val zipCode: Int,
    val addressDescription: String,
    val userFullName: String,
    val onEditClick: () -> Unit,
    val onDeleteClick: () -> Unit,
) :
    BindableItem<ItemProfileAddressBinding>() {

    override fun getLayout(): Int = R.layout.item_profile_address

    override fun initializeViewBinding(view: View): ItemProfileAddressBinding =
        ItemProfileAddressBinding.bind(view)

    @SuppressLint("SetTextI18n")
    override fun bind(viewBinding: ItemProfileAddressBinding, position: Int) {
        with(viewBinding) {
            addressLine1.text = "$houseNumber $streetName"
            addressLine2.text = "$county, $state"
            userZipCode.text = "$zipCode"
            userCountry.text = ", $country"
            addressDescriptionTitle.text = "$userFullName - "
            addressDescriptionTag.text = addressDescription
            addressItemMenu.setOnClickListener {
                showPopUpMenu(it, R.menu.menu_address_item_more)
            }
        }
    }

    private fun showPopUpMenu(v: View, @MenuRes menuRes: Int) {
        val wrapper = ContextThemeWrapper(v.context, R.style.BasePopUpMenu)

        val popUp = PopupMenu(wrapper, v, 0, 0, 0)

        popUp.menuInflater.inflate(menuRes, popUp.menu)
        popUp.gravity = Gravity.END
        popUp.setOnMenuItemClickListener { item ->
            when (item!!.itemId) {
                R.id.menuEditAddress -> {
                    onEditClick()
                }
                R.id.menuDeleteAddress -> {
                    onDeleteClick()
                }
            }
            true
        }
        popUp.show()
    }
}