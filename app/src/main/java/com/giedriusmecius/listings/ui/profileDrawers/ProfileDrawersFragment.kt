package com.giedriusmecius.listings.ui.profileDrawers

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.databinding.FragmentProfileDrawersBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.ProfileDrawerItem
import com.giedriusmecius.listings.ui.common.groupie.ProfileDrawerListItem
import com.giedriusmecius.listings.utils.extensions.getNavigationResult
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDrawersFragment :
    BaseFragment<FragmentProfileDrawersBinding>(FragmentProfileDrawersBinding::inflate) {

    private val vm by viewModels<ProfileDrawersViewModel>()
    private val groupie = GroupieAdapter()

    /* all of the data here should be saved somewhere
       you should create a drawer and add products to it
       make something like a favorite page that should hold
       different items in different categories. */

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val navHostFragment = (R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.transition(ProfileDrawersState.Event.ViewCreated)
        setupUI()
        listenForAdjustDialogResult()
    }

    override fun onDestroy() {
        super.onDestroy()
        groupie.clear()
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(this) { oldState, newState ->
            if (oldState != newState) {
                with(binding) {
                    profileDrawersProgressBar.isGone = !newState.isLoading
                }
            }
            when (val cmd = newState.command) {
                is ProfileDrawersState.Command.DisplayData -> {
                    displayData(cmd.data)
                }
                is ProfileDrawersState.Command.FilterSearch -> {
                    handleSearch(cmd.query, newState.data)
                }
                ProfileDrawersState.Command.OpenAdjustDrawers -> {
                    navigate(ProfileDrawersFragmentDirections.profileDrawerFragmentToProfileAdjustDialogFragment())
                }
                ProfileDrawersState.Command.ChangeLayoutHorizontal -> {
                    handleLayoutChange(true)
                    displayData(newState.data)
                }
                ProfileDrawersState.Command.ChangeLayoutGrid -> {
                    handleLayoutChange(false)
                    displayData(newState.data)
                }
                ProfileDrawersState.Command.ChangeLayoutList -> {
                    handleLayoutChange(true)
                    displayData(newState.data, true)
                }
                ProfileDrawersState.Command.OpenProfileFragment -> {
                    navigate(ProfileDrawersFragmentDirections.actionProfileDrawerFragmentToProfileFragment())
                }
                // todo layout adjustment.
                else -> {}
            }
        }
    }

    private fun handleSearch(query: String, data: List<Category>) {
        groupie.clear()
        val searchResults: MutableList<Category> = mutableListOf()
        data.forEach { cat ->
            cat.products.filter { product -> product.category.contains(query) }.let { products ->
                if (products.isNotEmpty()) {
                    searchResults.add(Category(cat.title, products))
                }
            }
        }

        displayData(searchResults)
    }

    private fun setupUI() {
        with(binding) {
            profilePicture.setOnClickListener {
                vm.transition(ProfileDrawersState.Event.TappedProfile)
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

    private fun listenForAdjustDialogResult() {
        getNavigationResult<ProfileDrawersAdjustDialogFragment.AdjustDialogActions>(
            R.id.profileDrawerFragment,
            ProfileDrawersAdjustDialogFragment.RESULT_KEY
        ) {
            when (it) {
                ProfileDrawersAdjustDialogFragment.AdjustDialogActions.HORIZONTAL -> {
                    vm.transition(ProfileDrawersState.Event.TappedHorizontalLayout)
                }
                ProfileDrawersAdjustDialogFragment.AdjustDialogActions.GRID -> {
                    vm.transition(ProfileDrawersState.Event.TappedGridLayout)
                }
                ProfileDrawersAdjustDialogFragment.AdjustDialogActions.LIST -> {
                    vm.transition(ProfileDrawersState.Event.TappedListLayout)
                }
                else -> {}
            }
        }
    }

    private fun handleLayoutChange(isLinear: Boolean) {
        if (isLinear) {
            binding.profileDrawerRecyclerView.layoutManager =
                LinearLayoutManager(context)
        } else {
            binding.profileDrawerRecyclerView.layoutManager = GridLayoutManager(context, 2)
        }
        groupie.clear()
    }

    private fun displayData(data: List<Category>, isListItem: Boolean = false) {
        if (data.isNotEmpty()) {
            if (isListItem) {
                groupie.addAll(data.map {
                    ProfileDrawerListItem(
                        it.title,
                        it.products.size,
                        it.products[0].image
                    )
                })
            } else {
                groupie.addAll(data.map { ProfileDrawerItem(it.title, it.products) })
            }
        }
    }
}