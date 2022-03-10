package com.giedriusmecius.listings.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.FragmentHomeBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import com.google.android.material.tabs.TabLayoutMediator

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
                HomeState.Command.ChangeName -> {
                    binding.homeTitle.text = "THIS IS NEW HOME TITLE"
                }
                else -> {}
            }
        }
    }

    private fun setupView() {
        with(binding) {
            homeScreenViewPager.adapter = HomeTabLayoutAdapter(this@HomeFragment)
            homeScreenViewPager.isUserInputEnabled = false

//            homeScreenViewPagerTabLayout.addTab(
//                homeScreenViewPagerTabLayout.newTab().setText(R.string.home_featured)
//            )
//            homeScreenViewPagerTabLayout.addTab(
//                homeScreenViewPagerTabLayout.newTab().setText(R.string.home_latest)
//            )
//            homeScreenViewPagerTabLayout.addTab(
//                homeScreenViewPagerTabLayout.newTab().setText(R.string.home_watchList)
//            )
            val tabArray = arrayOf("Featured", "Latest", "WatchList")

            TabLayoutMediator(homeScreenViewPagerTabLayout, homeScreenViewPager){ tab, position ->
                tab.text = tabArray[position]
            }.attach()
        }
    }
}