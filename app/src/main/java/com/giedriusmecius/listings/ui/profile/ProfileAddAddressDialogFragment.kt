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
    var address = UserAddress("", "", "", "", "", "", "", "", "", "", "", "")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
        setupUI()
    }

    private fun setupUI() {
        with(binding) {

            if (navArgs.isEdit) {
                address = navArgs.addressInfo!!
                addressLabel.setText(navArgs.addressInfo?.addressLabel)
                firstName.setText(navArgs.addressInfo?.firstName)
                lastName.setText(navArgs.addressInfo?.lastName)
                addressStreenName.setText(navArgs.addressInfo?.addressStreetName)
                addressHouseNumber.setText(navArgs.addressInfo?.addressHouseNumber)
                addressLine2.setText(navArgs.addressInfo?.addressLine2)
                addressZipCode.setText(navArgs.addressInfo?.zipCode)
                addressCity.setText(navArgs.addressInfo?.city)
                addressCounty.setText(navArgs.addressInfo?.county)
                addressCountry.setText(navArgs.addressInfo?.country)
                addressEmail.setText(navArgs.addressInfo?.email)
                addressState.setText(navArgs.addressInfo?.state)
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
            addressLine2.doAfterTextChanged {
                address = address.copy(addressLine2 = it.toString())
            }
            addressZipCode.doAfterTextChanged {
                address = address.copy(zipCode = it.toString())
            }
            addressCity.doAfterTextChanged { address = address.copy(city = it.toString()) }
            addressCounty.doAfterTextChanged { address = address.copy(county = it.toString()) }
            addressCountry.doAfterTextChanged {
                address = address.copy(country = it.toString())
            }
            addressEmail.doAfterTextChanged { address = address.copy(email = it.toString()) }
            addressState.doAfterTextChanged { address = address.copy(state = it.toString()) }


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