package com.giedriusmecius.listings.ui.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.giedriusmecius.listings.databinding.FragmentProductBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose

class ProductFragment : BaseFragment<FragmentProductBinding>(FragmentProductBinding::inflate) {
    private val vm by viewModels<ProductViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        with(binding) {
            close.setOnClickListener {
                navigateUp()
            }

            bookmarkIcon.setOnClickListener {
                //todo save to acc / drawer
            }

            moreBtn.setOnClickListener {
                //todo ????
            }
        }
    }


    override fun observeState() {
        vm.subscribeWithAutoDispose(this) { _, newState ->
            when (val cmd = newState.command) {

            }
        }
    }
}