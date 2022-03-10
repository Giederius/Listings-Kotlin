package com.giedriusmecius.listings.ui.Profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.databinding.FragmentProfileBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.PaymentMethodCardItem
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import com.xwray.groupie.GroupieAdapter


class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val vm by viewModels<ProfileViewModel>()
    val bottomSheet = ProfileFollowingDialogFragment()
    private val groupieAdapter = GroupieAdapter()

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
                adapter = groupieAdapter
            }

            groupieAdapter.addAll(
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

        }
    }
}