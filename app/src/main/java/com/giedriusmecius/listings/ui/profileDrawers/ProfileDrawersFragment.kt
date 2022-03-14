package com.giedriusmecius.listings.ui.profileDrawers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.databinding.FragmentProfileDrawersBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.ProfileDrawerItem
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import com.xwray.groupie.GroupieAdapter

class ProfileDrawersFragment :
    BaseFragment<FragmentProfileDrawersBinding>(FragmentProfileDrawersBinding::inflate) {
    private val vm by viewModels<ProfileDrawersViewModel>()
    private val groupie = GroupieAdapter()

    private var electronicsList = mutableListOf<Product>()
    private var jewelryList = mutableListOf<Product>()
    private var mensClothingList = mutableListOf<Product>()
    private var womensClothignList = mutableListOf<Product>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.transition(ProfileDrawersState.Event.ViewCreated)
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(this) { oldState, newState ->
            when (val cmd = newState.command) {
                is ProfileDrawersState.Command.DisplayData -> {
                    displayData(cmd.data)
                }
                else -> {}
            }
        }
    }

    private fun displayData(data: List<Product>) {
        data.map {
            when (it.category) {
                "electronics" -> {
                    electronicsList.add(it)
                }
                "jewelery" -> {
                    jewelryList.add(it)
                }
                "men's clothing" -> {
                    mensClothingList.add(it)
                }
                else -> {
                    womensClothignList.add(it)
                }
            }
        }
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
                    ProfileDrawerItem("Electronics", electronicsList),
                    ProfileDrawerItem("Jewelry", jewelryList),
                    ProfileDrawerItem("Men\'s clothing", mensClothingList),
                    ProfileDrawerItem("Women\'s clothing", womensClothignList),
                )
            )
        }
    }
}