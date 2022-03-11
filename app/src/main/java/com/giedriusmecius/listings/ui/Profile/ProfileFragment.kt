package com.giedriusmecius.listings.ui.Profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.databinding.FragmentProfileBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.PaymentMethodCardItem
import com.giedriusmecius.listings.ui.common.groupie.ProfileAddressItem
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import com.xwray.groupie.GroupieAdapter


class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val vm by viewModels<ProfileViewModel>()
    val bottomSheet = ProfileFollowingDialogFragment()
    private val paymentMethodAdapter = GroupieAdapter()
    private val addressAdapter = GroupieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.transition(ProfileState.Event.ViewCreated)
        setupUI()
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(viewLifecycleOwner) { _, newState ->
            when (val cmd = newState.command) {
                else -> {}
            }
        }
    }

    private fun setupUI() {
        with(binding) {
            followerCountButton.setOnClickListener {
                bottomSheet.show(
                    (activity as MainActivity).supportFragmentManager,
                    ProfileFollowingDialogFragment.TAG
                )
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
                    ),
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


}