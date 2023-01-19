package com.giedriusmecius.listings.ui.home.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.giedriusmecius.listings.databinding.FragmentDemoBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.home.HomeState
import com.giedriusmecius.listings.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DemoFragment :
    BaseFragment<FragmentDemoBinding>(FragmentDemoBinding::inflate) {
    private val vm by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            demoTitle.text = requireArguments().getString(TITLE)
        }
        vm.machine.transition(HomeState.Event.ViewCreated)
    }

    companion object {
        private const val TITLE = "title"
        fun getInstance(
            title: String
        ): DemoFragment {
            val instance = DemoFragment()
            instance.arguments =
                bundleOf(
                    TITLE to title
                )
            return instance
        }
    }
}