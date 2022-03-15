package com.giedriusmecius.listings.ui.profileDrawers

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.databinding.FragmentProfileDrawersBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.ProfileDrawerItem
import com.giedriusmecius.listings.ui.profile.ProfileFollowingDialogFragment
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

    private val adjustSheet = ProfileDrawersAdjustDialogFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.transition(ProfileDrawersState.Event.ViewCreated)
        setupUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        groupie.clear()
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(this) { oldState, newState ->
            when (val cmd = newState.command) {
                is ProfileDrawersState.Command.DisplayData -> {
                    displayData(cmd.data)
                }
                is ProfileDrawersState.Command.FilterSearch -> {
                    newState.data?.let { handleSearch(cmd.query, it) }
                }
                ProfileDrawersState.Command.OpenAdjustDrawers -> {
                    adjustSheet.show(
                        (activity as MainActivity).supportFragmentManager,
                        ""
                    )
                }
                else -> {}
            }
        }
    }

    private fun handleSearch(query: String, data: List<Product>) {
        groupie.clear()
        Log.d("mano", binding.profileSearchField.text.toString())
        var electronicsSearchList = mutableListOf<Product>()
        var jewelrySearchList = mutableListOf<Product>()
        var mensSearchList = mutableListOf<Product>()
        var womensSearchList = mutableListOf<Product>()
        val searchResult = data?.filter {
            it.category.contains(query)
        }
        searchResult?.map {
            when (it.category) {
                "electronics" -> {
                    electronicsSearchList.add(it)
                }
                "jewelery" -> {
                    jewelrySearchList.add(it)
                }
                "men's clothing" -> {
                    mensSearchList.add(it)
                }
                else -> {
                    womensSearchList.add(it)
                }
            }
        }
        if (electronicsSearchList.isNotEmpty()) {
            groupie.addAll(
                arrayListOf(
                    ProfileDrawerItem("Electronics", electronicsSearchList)
                )
            )
        }
        if (jewelrySearchList.isNotEmpty()) {
            groupie.addAll(
                arrayListOf(
                    ProfileDrawerItem("Jewelry", jewelrySearchList)
                )
            )
        }
        if (mensSearchList.isNotEmpty()) {
            groupie.addAll(
                arrayListOf(
                    ProfileDrawerItem("Men\'s clothing", mensSearchList)
                )
            )
        }
        if (womensSearchList.isNotEmpty()) {
            groupie.addAll(
                arrayListOf(
                    ProfileDrawerItem("Women\'s clothing", womensSearchList)
                )
            )
        }
        Log.d("MANO", searchResult.toString())

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
        groupie.addAll(
            arrayListOf(
                ProfileDrawerItem("Electronics", electronicsList),
                ProfileDrawerItem("Jewelry", jewelryList),
                ProfileDrawerItem("Men\'s clothing", mensClothingList),
                ProfileDrawerItem("Women\'s clothing", womensClothignList),
            )
        )
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

            profileSearchField.addTextChangedListener {
                vm.transition(ProfileDrawersState.Event.TypedInSearch(it.toString()))
                if (it.isNullOrEmpty()) {
                    binding.profileSearchButton.setBackgroundResource(R.drawable.ic_search)
                } else {
                    binding.profileSearchButton.setBackgroundResource(R.drawable.ic_delete)
                }
            }
            adjustDrawersButton.setOnClickListener {
                vm.transition(ProfileDrawersState.Event.TappedAdjustDrawers)
            }
        }
    }
}