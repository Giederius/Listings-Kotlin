package com.giedriusmecius.listings.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.giedriusmecius.listings.databinding.FragmentSplashBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val vm by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.splashTitle.text = "ASDSAD"
        vm.transition(SplashState.Event.ViewCreated)
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(viewLifecycleOwner) { _, newState ->
            when (newState.command) {
                SplashState.Command.OpenHomeScreen -> {}
            }
        }
    }
}