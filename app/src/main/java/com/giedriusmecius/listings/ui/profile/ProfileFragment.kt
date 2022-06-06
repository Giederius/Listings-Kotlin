package com.giedriusmecius.listings.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.local.PaymentMethod
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.data.remote.model.CardType
import com.giedriusmecius.listings.databinding.FragmentProfileBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.PaymentMethodCardItem
import com.giedriusmecius.listings.ui.common.groupie.ProfileAddressItem
import com.giedriusmecius.listings.utils.extensions.getNavigationResult
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
                else -> {}
            }
        }
    }

    private fun setupPaymentMethods(methods: List<PaymentMethod>) {
        paymentMethodAdapter.addAll(
            arrayListOf(
                PaymentMethodCardItem(
                    PaymentMethodCardItem.PaymentType.MASTERCARD,
                    "****1789"
                ) {
                    vm.transition(
                        ProfileState.Event.TappedEditPaymentMethod(
                            PaymentMethod(
                                450000000,
                                "asd",
                                CardType.VISA,
                                "11/30",
                                123
                            ), true
                        )
                    )
                },
            )
        )
    }

    private fun setupUserAddresses(addresses: List<UserAddress>) {
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
        Log.d("MANOpirmas", "nopr")
        getNavigationResult<PaymentMethod>(
            R.id.profileAddCardDialogFragment,
            ProfileAddCardDialogFragment.RESULT_KEY
        ) {
                Log.d("MANOgryzo", it.toString())
//            paymentMethodAdapter.add(
//                PaymentMethodCardItem(
//                    PaymentMethodCardItem.PaymentType.VISA,
//                    it?.number.toString()
//                ) {}
//            )
        }
    }

    companion object {
        const val RESULT_KEY = "profileResultKey"
    }
}