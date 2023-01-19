package com.giedriusmecius.listings.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.giedriusmecius.listings.databinding.FragmentHomeBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val vm by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.transition(HomeState.Event.ViewCreated)
        setupView()
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(viewLifecycleOwner) { _, newState ->
            when (val cmd = newState.command) {
                HomeState.Command.OpenSearch -> {
                    navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
                }
                else -> {}
            }
        }
    }

    private fun setupView() {
        with(binding) {

            inboxIcon.setOnClickListener {
                navigate(HomeFragmentDirections.actionHomeFragmentToInboxFragment())
            }

            homeSearchBar.setOnClickListener {
                vm.transition(HomeState.Event.TappedSearch)
            }
            setupViewPager()
        }
    }

    private fun setupViewPager() {
        with(binding) {
            homeScreenViewPager.adapter = HomeTabLayoutAdapter(this@HomeFragment)

            val tabArray = arrayOf("Featured", "Collection", "Stores", "Tags")

            TabLayoutMediator(homeScreenViewPagerTabLayout, homeScreenViewPager) { tab, position ->
                tab.text = tabArray[position]
            }.attach()
        }
    }
}