package com.giedriusmecius.listings.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.FragmentSearchBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.RecentSearchItem
import com.giedriusmecius.listings.ui.common.groupie.SuggestionCategoryItem
import com.giedriusmecius.listings.utils.extensions.hideKeyboard
import com.giedriusmecius.listings.utils.extensions.showKeyboard
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import com.google.android.material.tabs.TabLayout
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val vm by viewModels<SearchViewModel>()
    private val groupie = GroupieAdapter()
    private val groupieCat = GroupieAdapter()
    private var recentSearchQueries: MutableList<RecentSearchItem> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.transition(SearchState.Event.ViewCreated)
        (activity as MainActivity).hideBottomNavBar()

        setupUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showBottomNavBar()
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(this) { oldState, newState ->

            if (oldState?.isResultPage != newState.isResultPage) {
                binding.searchField.setOnClickListener {
//                    setupUIForSearch()
                    setupUIForSuggestions()
                }
            }

            if (oldState?.isSuggestionPage != newState.isSuggestionPage) {
                binding.apply {
                    searchField.setOnClickListener {
                        setupUIForSearch()
                        groupie.clear()
                        groupie.addAll(recentSearchQueries)
                    }
                    searchButton.setOnClickListener {
                        searchField.setText("")
                        setupUIForSearch()
                    }
                }
            }

            when (val cmd = newState.command) {
                is SearchState.Command.DisplayRecentSearches -> {
                    handleRecentSearches(cmd.list)
                }
                is SearchState.Command.UpdateRecentSearchQueries -> {
                    recentSearchQueries = mutableListOf()
                    groupie.clear()
                    handleRecentSearches(cmd.list)
                }
                is SearchState.Command.DisplaySuggestions -> {
                    val suggestionList: MutableList<RecentSearchItem> = mutableListOf()
                    val categoryList: MutableList<SuggestionCategoryItem> = mutableListOf()
                    suggestionList.clear()
                    categoryList.clear()
                    if (cmd.categorySuggestion.isNotEmpty()) {
                        groupieCat.clear()
                        cmd.categorySuggestion.map {
                            SuggestionCategoryItem(it.second, it.first, it.third) {
                                Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT)
                                    .show()
                            }.also {
                                categoryList.add(it)
                            }
                        }
                        groupieCat.addAll(categoryList)
                        binding.apply {
                            recentSearchesTitle.isGone = true
                            categorySuggestionRV.isGone = false
                            categoryDivider.isGone = false
                            recentSearchResultsRV.apply {
                                updateLayoutParams<ConstraintLayout.LayoutParams> {
                                    topToBottom = categoryDivider.id
                                }
                            }
                        }
                    }
                    if (cmd.simpleSuggestion.isNotEmpty()) {
                        groupie.clear()
                        cmd.simpleSuggestion.map {
                            RecentSearchItem(cmd.query, it, true, {
                                binding.searchField.setText(it)
                                vm.transition(SearchState.Event.PressedToSearch(it))
                                setupUIForSearchResults()
                            }, {})
                        }.also {
                            suggestionList.addAll(it)
                        }
                        groupie.addAll(suggestionList)
                    }
                }
                is SearchState.Command.DisplaySearchResults -> {
                    binding.apply {
                        categorySuggestionRV.isGone = false
                        categoryDivider.isGone = false
                        searchProgressIndicator.isGone = false
                    }
                    setupUIForSearchResults()
                }
                else -> {

                }
            }
        }
    }

    private fun handleRecentSearches(list: List<String>) {
        if (list.isEmpty()) {
            binding.recentSearchesEmpty.isGone = false
        } else {
            binding.recentSearchResultsRV.isGone = false
            list.map {
                RecentSearchItem(it, "", false, {
                    binding.searchField.setText(it)
                    vm.transition(SearchState.Event.PressedToSearch(it))
                    setupUIForSearchResults()
                }, {
                    vm.transition(
                        SearchState.Event.RemovedRecentSearch(
                            it,
                            list.indexOf(it)
                        )
                    )
                }).also { query -> recentSearchQueries.add(query) }
            }
            groupie.addAll(recentSearchQueries)
        }
    }

    private fun setupUI() {
        binding.apply {
            backBtn.setOnClickListener {
                navigateUp()
            }

            setUpViewPager()
            setupSearchField()

            categorySuggestionRV.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = groupieCat
            }

            recentSearchResultsRV.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = groupie
            }

            searchButton.setOnClickListener {
                searchField.setText("")
                setupUIForSearch()
            }
        }
    }

    private fun setupSearchField() {
        binding.apply {
            searchField.apply {
                showKeyboard()
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (s.isNullOrEmpty()) {
                            searchButton.setBackgroundResource(R.drawable.ic_search)
                        } else {
                            searchButton.setBackgroundResource(R.drawable.ic_delete)
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        lifecycleScope.launch {
                            if (s.toString() == "") {
                                binding.apply {
                                    setupUIForSearch()
                                    groupie.clear()
                                    groupie.addAll(recentSearchQueries)
                                }
                            } else {
                                delay(800)
                                vm.transition(SearchState.Event.TypedInSearch(s.toString()))
                            }
                        }
                    }

                })

                this.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                        if (this.text.toString() != "") {
                            vm.transition(SearchState.Event.PressedToSearch(this.text.toString()))
                            setupUIForSearchResults()
                        }
                        true
                    }
                    false
                }
            }
        }
    }

    private fun setupUIForSuggestions() {
        binding.apply {
            searchTopBar.apply {
                transitionToStart()
            }
            searchResultViewPagerTabLayout.isGone = false
            searchResultRV.isGone = true
            categorySuggestionRV.isGone = false
            categoryDivider.isGone = false
            searchField.showKeyboard()
        }
    }

    private fun setupUIForSearchResults() {
        with(binding) {
            recentSearchesTitle.isGone = true
            recentSearchResultsRV.isGone = true
            searchProgressIndicator.isGone = false
            searchField.hideKeyboard()
            searchTopBar.apply {
                setTransition(R.id.fromSearchToResult)
                transitionToEnd()
            }
            categorySuggestionRV.isGone = true
            categoryDivider.isGone = true
            searchProgressIndicator.isGone = true
            recentSearchResultsRV.isGone = true
            searchResultViewPagerTabLayout.isGone = false
        }
    }

    private fun setupUIForSearch() {
        with(binding) {
            recentSearchesTitle.isGone = false
            recentSearchResultsRV.apply {
                isGone = false
                updateLayoutParams<ConstraintLayout.LayoutParams> {
                    topToBottom = recentSearchesTitle.id
                }
            }
            searchResultViewPagerTabLayout.isGone = true
            searchResultRV.isGone = true
            categorySuggestionRV.isGone = true
            categoryDivider.isGone = true
            searchField.showKeyboard()
            searchTopBar.transitionToStart()
        }
    }

    private fun setUpViewPager() {
        with(binding) {
            searchResultViewPagerTabLayout.apply {
                addTab(
                    this.newTab().setText("Products")
                )
                addTab(
                    this.newTab().setText("Collections")
                )
                addTab(
                    this.newTab().setText("Stores")
                )
                addTab(
                    this.newTab().setText("Tags")
                )
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        when (tab?.position) {
                            0 -> binding.recentSearchResultsRV.isGone = false
                            else -> binding.recentSearchResultsRV.isGone = true
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })
            }
        }
    }

}