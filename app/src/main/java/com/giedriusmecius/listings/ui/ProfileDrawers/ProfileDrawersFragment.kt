package com.giedriusmecius.listings.ui.ProfileDrawers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.FragmentProfileDrawersBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.ProfileDrawerItem
import com.xwray.groupie.GroupieAdapter

class ProfileDrawersFragment :
    BaseFragment<FragmentProfileDrawersBinding>(FragmentProfileDrawersBinding::inflate) {
    private val vm by viewModels<ProfileDrawersViewModel>()
    private val groupie = GroupieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        with(binding) {
            profilePicture.setOnClickListener {
                navigate(ProfileDrawersFragmentDirections.actionProfileDrawerFragmentToProfileFragment())
            }
            profileDrawerRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = groupie
            }
            groupie.addAll(
                arrayListOf(
                    ProfileDrawerItem("Summer dress", 4, R.drawable.sample_image),
                    ProfileDrawerItem("Winter clothes", 2, R.drawable.sample_image),
                    ProfileDrawerItem("Autumn", 5, R.drawable.sample_image),
                    ProfileDrawerItem("new things", 7, R.drawable.sample_image),

                    )
            )
        }
    }
}