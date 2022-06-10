package com.giedriusmecius.listings.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.local.CardType
import com.giedriusmecius.listings.data.local.PaymentMethod
import com.giedriusmecius.listings.data.local.Size
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.databinding.FragmentProfileBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.dialogs.ColorSelectionDialogFragment
import com.giedriusmecius.listings.ui.common.dialogs.DepartmentSelectionDialogFragment
import com.giedriusmecius.listings.ui.common.dialogs.SizeDialogFragment
import com.giedriusmecius.listings.ui.common.groupie.PaymentMethodCardItem
import com.giedriusmecius.listings.ui.common.groupie.ProfileAddressItem
import com.giedriusmecius.listings.utils.extensions.getNavigationResult
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val vm by viewModels<ProfileViewModel>()
    private val paymentMethodAdapter = GroupieAdapter()
    private val addressAdapter = GroupieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        vm.transition(ProfileState.Event.ViewCreated)
        setupUI()
        (activity as MainActivity).hideBottomNavBar()

        listenForCCAdd()
        listenForAddressAdd()
        listenForDepartmentSelection()
        listenForSizeSelection()
        listenForColorSelection()
    }

    private fun setupRecyclerViews() {
        with(binding) {
            profilePaymentMethodRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = paymentMethodAdapter
            }

            addressesRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = addressAdapter
            }
        }
    }


// todo
//    susitvarkyti sharedprefsus+
//    edit button card itemam+
//              edit add address+
//                                  preferences list and selection

    override fun observeState() {
        vm.subscribeWithAutoDispose(viewLifecycleOwner) { oldState, newState ->

            with(binding) {
//                profileNoPaymentMethods.isVisible = newState.paymentMethods?.isEmpty() ?: true
//                profilePaymentMethodRecyclerView.isVisible =
//                    newState.paymentMethods?.isEmpty() ?: false
//
//                profileNoAddresses.isVisible = newState.userAddresses?.isEmpty() ?: false
//                addressesRecyclerView.isVisible = newState.userAddresses?.isEmpty() ?: true


//                if (oldState?.departmentName != newState.departmentName) {
//
//                }
            }

            when (val cmd = newState.command) {
                is ProfileState.Command.SetupUserDetails -> {
                    setupPaymentMethods(cmd.paymentMethods)
                    setupUserAddresses(cmd.userAddresses)
                }
                is ProfileState.Command.OpenPaymentMethodDialog -> {
                    navigate(
                        ProfileFragmentDirections.profileFragmentToAddCardDialog(
                            cmd.method, cmd.isEdit
                        )
                    )
                }
                is ProfileState.Command.AddPaymentMethod -> {
                    paymentMethodAdapter.add(0, mapPaymentMethod(cmd.method))
                }
                is ProfileState.Command.OpenUserAddressDialog -> {
                    navigate(
                        ProfileFragmentDirections.profileFragmentToProfileAddAddressDialog(
                            cmd.address,
                            cmd.isEdit
                        )
                    )
                }
                is ProfileState.Command.AddUserAddress -> {
                    addressAdapter.add(0, mapAddress(cmd.address))
                }
                is ProfileState.Command.OpenSizePicker -> {
                    navigate(ProfileFragmentDirections.profileFragmentToSizeDialog(cmd.userSize))
                }
                is ProfileState.Command.OpenDepartmentPicker -> {
                    navigate(
                        ProfileFragmentDirections.profileFragmentToDepartmentSelectionDialog(
                            cmd.departmentName
                        )
                    )
                }
                is ProfileState.Command.UpdateDepartment -> {
                    with(binding) {
                        departmentIcon.setText(cmd.departmentName.first().lowercase())
                        userMainDepartmentText.text = cmd.departmentName
                    }
                }
                is ProfileState.Command.UpdateSize -> {
                    with(binding) {
                        userSizeText.text = getString(
                            R.string.profile_preferences_sizeDescription,
                            cmd.size.us,
                            cmd.size.eu.uppercase()
                        )
                        sizeIcon.setText(cmd.size.eu)
                    }
                }
                is ProfileState.Command.OpenColorPicker -> {
                    navigate(ProfileFragmentDirections.profileFragmentToColorSelectionDialog(cmd.colorName))
                }
                is ProfileState.Command.UpdateColor -> {
                    with(binding) {
                        userFavoriteColorText.text = cmd.color.second
                        favoriteColorIcon.setIconTintWithString(cmd.color.first)
                    }
                }
                else -> {}
            }
        }
    }

    private fun setupPaymentMethods(methods: List<PaymentMethod>) {
        paymentMethodAdapter.clear()
        methods.map {
            mapPaymentMethod(it).also { card -> paymentMethodAdapter.add(card) }
        }
    }

    private fun setupUserAddresses(addresses: List<UserAddress>) {
        addressAdapter.clear()
        addresses.map {
            mapAddress(it).also { addressAdapter.add(it) }
        }

    }

    private fun setupUI() {
        with(binding) {
            close.setOnClickListener {
//                navigateUp()
                navigate(ProfileFragmentDirections.profileFragmentToProfileDrawerFragment())
            }
            followerCountButton.setOnClickListener {
                navigate(ProfileFragmentDirections.profileFragmentToProfileFollowingDialog())
            }
            paymentMethodsAddCardButton.setOnClickListener {
                vm.transition(ProfileState.Event.TappedPaymentMethod(null, false))
            }
            addressAddButton.setOnClickListener {
                vm.transition(ProfileState.Event.TappedUserAddress(null, false))
            }
            sizeIcon.setOnClickListener {
                vm.transition(ProfileState.Event.TappedSizePicker)
            }
            departmentIcon.setOnClickListener {
                vm.transition(ProfileState.Event.TappedDepartmentPicker)
            }
            favoriteColorIcon.setOnClickListener {
                vm.transition(ProfileState.Event.TappedColorPicker)
            }
        }
    }

    private fun listenForCCAdd() {
        getNavigationResult<Triple<Boolean, PaymentMethod, PaymentMethod>>(
            // isEdit, newCard, oldCard
            R.id.profileFragment,
            ProfileAddCardDialogFragment.RESULT_KEY
        ) {
            if (it.first) {
                vm.transition(ProfileState.Event.EditedPaymentMethod(it.second, it.third))
            } else {
                vm.transition(ProfileState.Event.AddedPaymentMethod(it.second))
            }
        }
    }

    private fun listenForAddressAdd() {
        getNavigationResult<Triple<Boolean, UserAddress, UserAddress>>(
            R.id.profileFragment,
            ProfileAddAddressDialogFragment.RESULT_KEY
        ) {
            if (it.first) {
                vm.transition(ProfileState.Event.EditedUserAddress(it.second, it.third))
            } else {
                vm.transition(ProfileState.Event.AddedUserAddress(it.second))
            }
        }
    }

    private fun listenForDepartmentSelection() {
        getNavigationResult<String>(
            R.id.profileFragment,
            DepartmentSelectionDialogFragment.RESULT_KEY
        ) {
            vm.transition(ProfileState.Event.ReceivedDepartment(it))
        }
    }

    private fun listenForSizeSelection() {
        getNavigationResult<Size>(
            R.id.profileFragment,
            SizeDialogFragment.RESULT_KEY
        ) {
            Log.d("Manosize", it.toString())
            vm.transition(ProfileState.Event.ReceivedUserSize(it))
        }
    }

    private fun listenForColorSelection() {
        getNavigationResult<Pair<String, String>>(
            R.id.profileFragment,
            ColorSelectionDialogFragment.RESULT_KEY
        ) {
            vm.transition(ProfileState.Event.ReceivedUserColor(it))
        }
    }

    private fun mapAddress(address: UserAddress): ProfileAddressItem {
        return ProfileAddressItem(
            houseNumber = address.addressHouseNumber,
            streetName = address.addressStreetName,
            county = address.county,
            state = address.state,
            country = address.country,
            zipCode = address.zipCode,
            addressDescription = address.addressLabel,
            userFullName = "${address.firstName} ${address.lastName}",
            onEditClick = { vm.transition(ProfileState.Event.TappedUserAddress(address, true)) },
            onDeleteClick = { vm.transition(ProfileState.Event.TappedDeleteAddress(address)) },
        )
    }

    private fun mapPaymentMethod(method: PaymentMethod): PaymentMethodCardItem {
        val numberString = getString(
            R.string.profile_payments_cardNumber,
            method.number.toString().takeLast(4)
        )
        return PaymentMethodCardItem(method.type ?: CardType.VISA, numberString, {
            vm.transition(
                ProfileState.Event.TappedPaymentMethod(method, true)
            )
        },
            { vm.transition(ProfileState.Event.TappedDeletePaymentMethod(method)) })
    }
}
