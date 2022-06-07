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
// susitvarkyti sharedprefsus+
//              edit button card itemam
// edit add address
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
                                ProfileState.Event.TappedEditPaymentMethod(cmd.method, true)
                            )
                        }
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
                    ProfileState.Event.TappedEditPaymentMethod(it, true)
                )
            }.also { card -> paymentMethodAdapter.add(card) }
        }
    }

    private fun setupUserAddresses(addresses: List<UserAddress>) {
        addressAdapter.clear()
        addressAdapter.addAll(
            arrayListOf(
                ProfileAddressItem(
                    "3480",
                    "Crim Lane",
                    "Greendale County",
                    "Colorado",
                    789456,
                    "Home",
                    "Ava Johnson",
                    onEditClick = { Log.d("MANO", "edit") },
                    onDeleteClick = { Log.d("MANO", "edit") }
                ) {
                    Log.d("MANO", "first")
                },
            )
        )
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
                vm.transition(ProfileState.Event.TappedEditPaymentMethod(null, false))
            }
            addressAddButton.setOnClickListener {
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
}
