package com.giedriusmecius.listings.ui.home.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import com.giedriusmecius.listings.databinding.FragmentDemoBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment

class DemoFragment :
    BaseFragment<FragmentDemoBinding>(FragmentDemoBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            demoTitle.text = requireArguments().getString(TITLE)
        }
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