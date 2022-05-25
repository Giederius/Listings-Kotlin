package com.giedriusmecius.listings.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.CC
import com.giedriusmecius.listings.data.remote.model.CardType
import com.giedriusmecius.listings.databinding.FragmentProfileBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.PaymentMethodCardItem
import com.giedriusmecius.listings.ui.common.groupie.ProfileAddressItem
import com.giedriusmecius.listings.utils.extensions.getNavigationResult
import com.giedriusmecius.listings.utils.extensions.showAlertDialog
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import com.xwray.groupie.GroupieAdapter


class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val vm by viewModels<ProfileViewModel>()
    private val paymentMethodAdapter = GroupieAdapter()
    private val addressAdapter = GroupieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.transition(ProfileState.Event.ViewCreated)
        setupUI()
    }

    // todo
    // edit button card itemam
    // edit add address
    // preferences list and selection

    override fun observeState() {
        vm.subscribeWithAutoDispose(viewLifecycleOwner) { _, newState ->
            when (val cmd = newState.command) {
                else -> {}
            }
        }
    }

    private fun setupUI() {
        with(binding) {
            close.setOnClickListener {
//                navigateUp()
                navigate(ProfileFragmentDirections.profileFragmentToProfileDrawerFragment("GIedrius"))
            }
            followerCountButton.setOnClickListener {
                navigate(ProfileFragmentDirections.profileFragmentToProfileFollowingDialog())
            }
            paymentMethodsEditButton.setOnClickListener {
                showAlertDialog(
                    it.context,
                    onPositiveClick = {
                        Toast.makeText(
                            context,
                            "Positive",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onNegativeClick = {
                        Toast.makeText(
                            context,
                            "Negative!",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
            }
            paymentMethodsAddCardButton.setOnClickListener {
                navigate(ProfileFragmentDirections.profileFragmentToAddCardDialog(null, false))
            }
            addressAddButton.setOnClickListener {
            }

            profilePaymentMethodRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = paymentMethodAdapter
            }

            paymentMethodAdapter.addAll(
                arrayListOf(
                    PaymentMethodCardItem(
                        PaymentMethodCardItem.PaymentType.MASTERCARD,
                        "****1789"
                    ) {
                        navigate(
                            ProfileFragmentDirections.profileFragmentToAddCardDialog(
                                CC(
                                    number = 123451789,
                                    name = "Netikra korta",
                                    type = CardType.VISA,
                                    expDate = "11/26",
                                    ccv = 123
                                ),
                                true
                            )
                        )
                    },
                    PaymentMethodCardItem(
                        PaymentMethodCardItem.PaymentType.VISA,
                        "****1722"
                    ),
                    PaymentMethodCardItem(
                        PaymentMethodCardItem.PaymentType.MASTERCARD,
                        "****1891"
                    ),
                    PaymentMethodCardItem(
                        PaymentMethodCardItem.PaymentType.VISA,
                        "****8881"
                    ),
                )
            )

            addressesRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = addressAdapter
            }

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
                    ProfileAddressItem(
                        "20",
                        "Crim Lane",
                        "Greendale County",
                        "Colorado",
                        891351,
                        "Work",
                        "Ava Johnson",
                        onEditClick = { Log.d("MANO", "edit") },
                        onDeleteClick = { Log.d("MANO", "edit") }
                    ) {
                        Log.d("MANO", "second")
                    }
                )
            )

        }
    }

    private fun listenForCCAdd() {
        getNavigationResult<CC>(
            R.id.profileAddCardDialogFragment,
            ProfileAddCardDialogFragment.RESULT_KEY
        ) {
            paymentMethodAdapter.add(
                PaymentMethodCardItem(
                    PaymentMethodCardItem.PaymentType.VISA,
                    it?.number.toString()
                )
            )
        }
    }

    companion object {
        const val RESULT_KEY = "profileResultKey"
    }
}