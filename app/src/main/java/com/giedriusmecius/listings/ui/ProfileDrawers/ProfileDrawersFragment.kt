package com.giedriusmecius.listings.ui.ProfileDrawers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.giedriusmecius.listings.databinding.FragmentProfileDrawersBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment

class ProfileDrawersFragment :
    BaseFragment<FragmentProfileDrawersBinding>(FragmentProfileDrawersBinding::inflate) {
    private val vm by viewModels<ProfileDrawersViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}