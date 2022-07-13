package com.giedriusmecius.listings.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.FragmentSearchBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.RecentSearchItem
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
                            delay(500)
                            vm.transition(SearchState.Event.TypedInSearch(s.toString()))
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
        }
    }

    private fun setupUIForSearch() {
        with(binding) {
            recentSearchesTitle.isGone = false
            recentSearchResultsRV.isGone = false
            searchResultViewPagerTabLayout.isGone = true
            searchResultRV.isGone = true
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

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showBottomNavBar()
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(this) { oldState, newState ->

            if (newState.isResultPage) {
                binding.searchField.setOnClickListener {
                    setupUIForSearch()
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
                    Log.d("MANO", cmd.query)
                }
                is SearchState.Command.DisplaySearchResults -> {
                    with(binding) {
                        searchProgressIndicator.isGone = true
                        recentSearchResultsRV.isGone = true
                        searchResultViewPagerTabLayout.isGone = false
                    }

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
            Log.d("MANO", list.toString())
            binding.recentSearchResultsRV.isGone = false
            list.map {
                RecentSearchItem(it, false, {
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

}