package com.giedriusmecius.listings.ui.testRange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.FragmentTestRangeBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.views.ButtonTestingGroundsScreen

class TestRangeFragment :
    BaseFragment<FragmentTestRangeBinding>(FragmentTestRangeBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test_range, container, false).apply {
            findViewById<ComposeView>(R.id.testRangeComposeView).setContent {
                Scaffold() {
                    ButtonTestingGroundsScreen(Modifier)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).hideBottomNavBar()
    }
}