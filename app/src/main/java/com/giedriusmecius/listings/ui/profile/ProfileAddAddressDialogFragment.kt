package com.giedriusmecius.listings.ui.profile

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.navArgs
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.databinding.DialogProfileAddAddressBinding
import com.giedriusmecius.listings.ui.common.base.BaseDialogFragment
import com.giedriusmecius.listings.utils.extensions.setNavigationResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileAddAddressDialogFragment :
    BaseDialogFragment<DialogProfileAddAddressBinding>(DialogProfileAddAddressBinding::inflate) {
    private val navArgs by navArgs<ProfileAddAddressDialogFragmentArgs>()
    var address = UserAddress("", "", "", "", "", "", 0, "", "", "", "", "")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
        setupUI()
    }

    private fun setupUI() {
        with(binding) {

            addAddressTitle.setOnClickListener {
                addressLabel.setText("Main")
                firstName.setText("Giedrius")
                lastName.setText("Mecius")
                addressStreenName.setText("Parko g")
                addressHouseNumber.setText("1-14")
                addressLine2.setText("")
                addressZipCode.setText("90311")
                addressCity.setText("Rietavas")
                addressCounty.setText("Rietavo")
                addressCountry.setText("Lietuva")
                addressEmail.setText("test@test.com")
                addressState.setText("Zemaiciu")
            }


            addressLabel.apply {
                requestFocus()
                this.doAfterTextChanged { address = address.copy(addressLabel = it.toString()) }
            }
            firstName.doAfterTextChanged { address = address.copy(firstName = it.toString()) }
            lastName.doAfterTextChanged { address = address.copy(lastName = it.toString()) }
            addressStreenName.doAfterTextChanged {
                address = address.copy(addressStreetName = it.toString())
            }
            addressHouseNumber.doAfterTextChanged {
                address = address.copy(addressHouseNumber = it.toString())
            }
            addressLine2.doAfterTextChanged { address = address.copy(addressLine2 = it.toString()) }
            addressZipCode.doAfterTextChanged {
                address = address.copy(zipCode = Integer.parseInt(it.toString()))
            }
            addressCity.doAfterTextChanged { address = address.copy(city = it.toString()) }
            addressCounty.doAfterTextChanged { address = address.copy(county = it.toString()) }
            addressCountry.doAfterTextChanged { address = address.copy(country = it.toString()) }
            addressEmail.doAfterTextChanged { address = address.copy(email = it.toString()) }
            addressState.doAfterTextChanged { address = address.copy(state = it.toString()) }

            saveAddressBtn.setOnClickListener {
                setResult(Triple(navArgs.isEdit, address, navArgs.addressInfo))
            }
        }
    }

    private fun setResult(value: Triple<Boolean, UserAddress, UserAddress?>) {
        // isEdit, newAddress, oldAddress
        setNavigationResult(RESULT_KEY, value)
        dismiss()
    }

    companion object {
        const val RESULT_KEY = "profileAddAddressResultKey"
    }
}