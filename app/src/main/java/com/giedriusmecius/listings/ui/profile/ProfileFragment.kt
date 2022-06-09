package com.giedriusmecius.listings.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.local.CardType
import com.giedriusmecius.listings.data.local.PaymentMethod
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.databinding.FragmentProfileBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
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
//              edit add address
// preferences list and selection

    override fun observeState() {
        vm.subscribeWithAutoDispose(viewLifecycleOwner) { _, newState ->

            with(binding) {
//                profileNoPaymentMethods.isVisible = newState.paymentMethods?.isEmpty() ?: true
//                profilePaymentMethodRecyclerView.isVisible =
//                    newState.paymentMethods?.isEmpty() ?: false
//
//                profileNoAddresses.isVisible = newState.userAddresses?.isEmpty() ?: false
//                addressesRecyclerView.isVisible = newState.userAddresses?.isEmpty() ?: true
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
                    val numberString = getString(
                        R.string.profile_payments_cardNumber,
                        cmd.method.number.toString().takeLast(4)
                    )

                    paymentMethodAdapter.add(
                        0,
                        PaymentMethodCardItem(cmd.method.type ?: CardType.VISA, numberString) {
                            vm.transition(
                                ProfileState.Event.TappedPaymentMethod(cmd.method, true)
                            )
                        }
                    )
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
                    addressAdapter.add(
                        0, ProfileAddressItem(
                            houseNumber = cmd.address.addressHouseNumber,
                            streetName = cmd.address.addressStreetName,
                            county = cmd.address.county,
                            state = cmd.address.state,
                            country = cmd.address.country,
                            zipCode = cmd.address.zipCode,
                            addressDescription = cmd.address.addressLabel,
                            userFullName = "${cmd.address.firstName} ${cmd.address.lastName}",
                            onEditClick = {
                                vm.transition(
                                    ProfileState.Event.TappedUserAddress(
                                        cmd.address,
                                        true
                                    )
                                )
                            },
                            onDeleteClick = {
                                vm.transition(
                                    ProfileState.Event.TappedDeleteAddress(
                                        cmd.address
                                    )
                                )
                            }
                        )
                    )
                }
                else -> {}
            }
        }
    }

    private fun setupPaymentMethods(methods: List<PaymentMethod>) {
        paymentMethodAdapter.clear()
        methods.map {
            val numberString = getString(
                R.string.profile_payments_cardNumber,
                it.number.toString().takeLast(4)
            )
            PaymentMethodCardItem(it.type ?: CardType.VISA, numberString) {
                vm.transition(
                    ProfileState.Event.TappedPaymentMethod(it, true)
                )
            }.also { card -> paymentMethodAdapter.add(card) }
        }
    }

    private fun setupUserAddresses(addresses: List<UserAddress>) {
        addressAdapter.clear()
        addresses.map {
            ProfileAddressItem(
                houseNumber = it.addressHouseNumber,
                streetName = it.addressStreetName,
                county = it.county,
                state = it.state,
                country = it.country,
                zipCode = it.zipCode,
                addressDescription = it.addressLabel,
                userFullName = "${it.firstName} ${it.lastName}",
                onEditClick = { vm.transition(ProfileState.Event.TappedUserAddress(it, true)) },
                onDeleteClick = { vm.transition(ProfileState.Event.TappedDeleteAddress(it)) },
            ).also { addressAdapter.add(it) }
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
}
