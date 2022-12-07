package com.giedriusmecius.listings.ui.splash

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.giedriusmecius.listings.databinding.FragmentSplashBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    private lateinit var auth: FirebaseAuth
    private val vm by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.transition(SplashState.Event.ViewCreated)

        binding.splashButton.setOnClickListener {
            vm.transition(SplashState.Event.TappedButton)
        }
        // todo create some sort of login check
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(viewLifecycleOwner) { _, newState ->
            when (newState.command) {
                SplashState.Command.OpenHomeScreen -> {
                    navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
                }
                else -> {}
            }
        }
    }
}