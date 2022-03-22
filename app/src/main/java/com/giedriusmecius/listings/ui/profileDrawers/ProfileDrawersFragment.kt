package com.giedriusmecius.listings.ui.profileDrawers

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.databinding.FragmentProfileDrawersBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.ProfileDrawerGridItem
import com.giedriusmecius.listings.ui.common.groupie.ProfileDrawerItem
import com.giedriusmecius.listings.ui.common.groupie.ProfileDrawerListItem
import com.giedriusmecius.listings.ui.profile.ProfileFragment
import com.giedriusmecius.listings.utils.extensions.getNavigationResult
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
        setupUI()
//        listenForAdjustDialogAction()
        listenForAdjustDialogResult()
//        listenForProfileFragmentResult()
        Log.d("MANOENTRY", previousBackStackEntry.toString())
    }

    private fun listenForProfileFragmentResult() {
        getNavigationResult<String?>(
            R.id.profileDrawerFragment,
            ProfileFragment.RESULT_KEY
        ) {
            Log.d("MANOPROFILIO", it.toString())
        }
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
                    newState.data?.let { handleSearch(cmd.query, it) }
                }
                ProfileDrawersState.Command.OpenAdjustDrawers -> {
                    navigate(ProfileDrawersFragmentDirections.profileDrawerFragmentToProfileAdjustDialogFragment())
                }
                // todo there must be a better way to do this
                ProfileDrawersState.Command.ChangeLayoutHorizontal -> {
                    binding.profileDrawerRecyclerView.layoutManager =
                        LinearLayoutManager(context)
                    groupie.clear()
                    groupie.addAll(
                        arrayListOf(
                            ProfileDrawerItem("Electronics", electronicsList),
                            ProfileDrawerItem("Jewelry", jewelryList),
                            ProfileDrawerItem("Men\'s clothing", mensClothingList),
                            ProfileDrawerItem("Women\'s clothing", womensClothignList),
                        )
                    )
                }
                ProfileDrawersState.Command.ChangeLayoutGrid -> {
                    binding.profileDrawerRecyclerView.layoutManager = GridLayoutManager(context, 2)
                    groupie.clear()
                    groupie.addAll(
                        arrayListOf(
                            ProfileDrawerGridItem("Electronics", electronicsList),
                            ProfileDrawerGridItem("Jewelry", jewelryList),
                            ProfileDrawerGridItem("Men\'s clothing", mensClothingList),
                            ProfileDrawerGridItem("Women\'s clothing", womensClothignList),
                        )
                    )

                }
                ProfileDrawersState.Command.ChangeLayoutList -> {
                    groupie.clear()
                    binding.profileDrawerRecyclerView.layoutManager = LinearLayoutManager(context)
                    groupie.addAll(
                        arrayListOf(
                            ProfileDrawerListItem(
                                "Electronics",
                                electronicsList.size,
                                electronicsList[0].image
                            ),
                            ProfileDrawerListItem(
                                "Jewelry",
                                jewelryList.size,
                                jewelryList[0].image
                            ),
                            ProfileDrawerListItem(
                                "Men\'s clothing",
                                mensClothingList.size,
                                mensClothingList[0].image
                            ),
                            ProfileDrawerListItem(
                                "Women\'s clothing",
                                womensClothignList.size,
                                womensClothignList[0].image
                            ),
                        )
                    )
                }
                // todo layout adjustment.
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

    private fun listenForAdjustDialogResult() {
        getNavigationResult<Any?>(
            R.id.profileDrawerFragment,
            ProfileDrawersAdjustDialogFragment.RESULT_KEY
        ) {
            Log.d("MANOKEY", "$it")
//            when (it) {
//                ProfileDrawersAdjustDialogFragment.AdjustDialogActions.HORIZONTAL -> {
//                    Log.d("MANO", "HORIZONATL")
//                    vm.transition(ProfileDrawersState.Event.TappedHorizontalLayout)
//                }
//                ProfileDrawersAdjustDialogFragment.AdjustDialogActions.GRID -> {
//                    Log.d("MANO", "GRID")
//                    vm.transition(ProfileDrawersState.Event.TappedGridLayout)
//                }
//                ProfileDrawersAdjustDialogFragment.AdjustDialogActions.LIST -> {
//                    Log.d("MANO", "LIST")
//                    vm.transition(ProfileDrawersState.Event.TappedListLayout)
//                }
//                else -> {}
//            }
        }
    }
}