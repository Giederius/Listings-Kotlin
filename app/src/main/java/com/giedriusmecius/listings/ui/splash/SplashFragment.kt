package com.giedriusmecius.listings.ui.splash

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.giedriusmecius.listings.databinding.FragmentSplashBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val vm by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.splashButton.setOnClickListener {
            vm.transition(SplashState.Event.TappedButton)
//            navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
        }
        // todo create some sort of login check
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(viewLifecycleOwner) { _, newState ->
            when (newState.command) {
                SplashState.Command.OpenHomeScreen -> {
                    Log.d("GM1", "state")
                    navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
                }
            }
        }
    }
}