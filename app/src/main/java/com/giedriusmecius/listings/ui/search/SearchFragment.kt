package com.giedriusmecius.listings.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.local.FilterOptions
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.databinding.FragmentSearchBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.dialogs.SearchFilterByDialogFragment
import com.giedriusmecius.listings.ui.common.dialogs.SearchSortByDialogFragment
import com.giedriusmecius.listings.ui.common.groupie.ProductItem
import com.giedriusmecius.listings.ui.common.groupie.RecentSearchItem
import com.giedriusmecius.listings.ui.common.groupie.SuggestionCategoryItem
import com.giedriusmecius.listings.ui.search.viewPagerFragments.SearchResultProductsFragment
import com.giedriusmecius.listings.utils.extensions.getNavigationResult
import com.giedriusmecius.listings.utils.extensions.hideKeyboard
import com.giedriusmecius.listings.utils.extensions.setMotionLayoutVisibility
import com.giedriusmecius.listings.utils.extensions.showKeyboard
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val vm by viewModels<SearchViewModel>()
    private val groupie = GroupieAdapter()
    private val groupieCat = GroupieAdapter()
    private val groupieResults = GroupieAdapter()
    private var recentSearchQueries: MutableList<RecentSearchItem> = mutableListOf()
    private var hasScrolledToTop = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.transition(SearchState.Event.ViewCreated)
        (activity as MainActivity).hideBottomNavBar()

        setupUI()
        listenForSortDialogResult()
        listenForFilterDialogResult()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showBottomNavBar()
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(this) { oldState, newState ->

            binding.apply {
                searchProgressIndicator.isGone = !newState.isLoading
                resultSortBtn.toggleState(newState.isSortingActive)
                resultFilterBtn.toggleState(newState.isFilteringActive)
                bottomFABSortBtn.toggleState(newState.isSortingActive)
                bottomFABFilterBtn.toggleState(newState.isFilteringActive)
            }

            if (oldState?.searchUIState != newState.searchUIState) {
                Log.d("MANO", "${newState.searchUIState}")
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
                    setupUIForSuggestions()
                    handleSuggestions(cmd.categorySuggestion, cmd.simpleSuggestion, cmd.query)
                }
                is SearchState.Command.DisplaySearchResults -> {
                    if (cmd.results.isNotEmpty()) {
                        setupUIForSearchResults(true)
                        setupSearchResults(cmd.results)
                    } else {
                        binding.noSearchResultFound.isGone = false
                        setupUIForSearchResults(false)
                        setupSearchResults(emptyList())
                    }
                }

                is SearchState.Command.OpenSortDialog -> {
                    navigate(SearchFragmentDirections.searchFragmentToSortDialog(cmd.sortBy))
                }
                is SearchState.Command.OpenFilterDialog -> {
                    navigate(
                        SearchFragmentDirections.searchFragmentToFilterDialog(
                            cmd.filterOptions,
                            cmd.mainFilterOptions
                        )
                    )
                }
                is SearchState.Command.SortProductsBy -> {
                    handleSorting(cmd.sortBy, cmd.products)
                }
                SearchState.Command.SetupSearchUI -> {
                    setupUIForSearch()
                }
                is SearchState.Command.SetupSuggestionsUI -> {
                    setupUIForSuggestions()
                    handleSuggestions(cmd.categorySuggestion, cmd.simpleSuggestion, cmd.query)
                }
                else -> {

                }
            }
        }
    }

    private fun handleSorting(sortBy: String, products: List<Product>) {
        var sortedProducts: List<Product> = emptyList()
        when (sortBy) {
            "az" -> {
                sortedProducts = products.sortedBy { it.title }
            }
            "za" -> {
                sortedProducts = products.sortedByDescending { it.title }
            }
            "priceLow" -> {
                sortedProducts = products.sortedBy { it.price }
            }
            "priceHigh" -> {
                sortedProducts = products.sortedByDescending { it.price }
            }
        }
        setupSearchResults(sortedProducts)
    }

    private fun setupSearchResults(results: List<Product>) {
        groupieResults.clear()
        val products: MutableList<ProductItem> = mutableListOf()
        results.map {
            ProductItem(
                title = it.title,
                image = it.image,
                price = it.price,
                discountedPrice = it.price,
                onClick = {
                    Snackbar.make(binding.root, "Snack", Snackbar.LENGTH_SHORT).show()
                }).also {
                products.add(it)
            }
        }
        groupieResults.addAll(products)
        vm.searchResults.setValue(products)
    }

    private fun handleRecentSearches(list: List<String>) {
        if (list.isEmpty()) {
            binding.recentSearchesEmpty.isGone = false
        } else {
            binding.recentSearchResultsRV.isGone = false
            list.map {
                RecentSearchItem(it, "", false, {
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.apply {
                            searchField.setText(it)
                            searchProgressIndicator.isGone = false
                            categoryDivider.isGone = true
                            categorySuggestionRV.isGone = true
                        }
                    }
                    vm.transition(SearchState.Event.PressedToSearch(it))
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

            searchResultRV.apply {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = groupieResults
            }

            searchButton.setOnClickListener {
                searchField.setText("")
                setupUIForSearch()
            }

            resultSortBtn.setOnClickListener {
                vm.transition(SearchState.Event.TappedSort)
            }
            resultFilterBtn.setOnClickListener {
                vm.transition(SearchState.Event.TappedFilter)
            }

            searchNestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (searchFragmentViewPager.isVisible) {
                    val bottom = searchTopBar.y + searchTopBar.height
                    when {
                        scrollY > bottom -> {
                            bottomSearchFAB.apply {
                                isGone = false
                                if (hasScrolledToTop) {
                                    translationY = 500F
                                    animate()
                                        .translationY(0F)
                                    searchTopBar.apply {
                                        setTransition(R.id.fromResultToOnScroll)
                                        transitionToEnd()
                                    }
                                }
                            }
                            hasScrolledToTop = false
                        }
                        scrollY < bottom -> {
                            bottomSearchFAB.apply {
                                if (!hasScrolledToTop) {
                                    translationY = 0F
                                    animate()
                                        .translationY(500F).withEndAction { isGone = true }
                                    searchTopBar.transitionToStart()
                                }
                            }
                            hasScrolledToTop = true
                        }
                    }
                }
            })
            bottomFABSearchBtn.setOnClickListener {
                setupUIForSearch()
                bottomSearchFAB.isGone = true
            }
            bottomFABSortBtn.setOnClickListener {
                vm.transition(SearchState.Event.TappedSort)
            }
            bottomFABFilterBtn.setOnClickListener {
                vm.transition(SearchState.Event.TappedFilter)
            }
        }
    }


    private fun setupSearchField() {
        binding.apply {
            searchField.apply {
                setOnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            vm.transition(SearchState.Event.TappedSearchField)
                            searchTopBar.apply {
                                setTransition(R.id.fromSearchToResult)
                                transitionToStart()
                            }
                            true
                        }
                    }
                    false
                }
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
                            binding.searchButton.setBackgroundResource(R.drawable.ic_search)
                        } else {
                            binding.searchButton.setBackgroundResource(R.drawable.ic_delete)
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        lifecycleScope.launch {
                            if (s.toString() == "") {
                                binding.apply {
                                    setupUIForSearch()
                                    groupie.clear()
                                    groupie.addAll(recentSearchQueries)
                                    vm.transition(SearchState.Event.ChangedSearchUI(SearchType.Search))
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
                            binding.apply {
                                searchProgressIndicator.isGone = false
//                                categorySuggestionRV.isGone = true
//                                categoryDivider.isGone = true
//                                recentSearchResultsRV.isGone = true
                            }
                        }
                        true
                    }
                    false
                }
            }
        }
    }

    private fun setupUIForSuggestions() {
        Log.d("MANOsuggestions", "setup")
        lifecycleScope.launch(Dispatchers.Main) {
            binding.apply {
                searchTopBar.apply {
                    transitionToStart()
                }
                recentSearchesTitle.isGone = true
                searchResultViewPagerTabLayout.setMotionLayoutVisibility(View.GONE)
                searchFragmentViewPager.isGone = true
                searchResultRV.isGone = true
                categorySuggestionRV.isGone = false
                recentSearchResultsRV.isGone = false
                categoryDivider.isGone = false
                searchField.showKeyboard()
                noSearchResultFound.isGone = true
            }
        }
    }

    private fun setupUIForSearchResults(hasResults: Boolean) {
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
//            searchResultRV.isGone = !hasResults
            searchResultRV.isGone = true
            searchProgressIndicator.isGone = true
            searchResultViewPagerTabLayout.setMotionLayoutVisibility(View.VISIBLE)
            searchFragmentViewPager.isGone = false
            searchResultViewPagerTabLayout.getTabAt(0)?.select()
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
            searchResultViewPagerTabLayout.setMotionLayoutVisibility(View.GONE)
            searchFragmentViewPager.isGone = true
            searchResultRV.isGone = true
            categorySuggestionRV.isGone = true
            categoryDivider.isGone = true
            searchField.showKeyboard()
            searchTopBar.transitionToStart()
            noSearchResultFound.isGone = true
        }
    }

    private fun setUpViewPager() {
        with(binding) {
            searchFragmentViewPager.adapter = SearchTabLayoutAdapter(this@SearchFragment)

            val tabArray = arrayOf("Products", "Collections", "Stores", "Tags")

            TabLayoutMediator(
                searchResultViewPagerTabLayout,
                searchFragmentViewPager
            ) { tab, position ->
                tab.text = tabArray[position]
            }.attach()
        }
    }

    private fun listenForSortDialogResult() {
        getNavigationResult<String>(R.id.searchFragment, SearchSortByDialogFragment.RESULT_KEY) {
            if (it.isNotEmpty()) {
                vm.transition(SearchState.Event.ChangedSortBy(it))
            }
        }
    }

    private fun listenForFilterDialogResult() {
        getNavigationResult<Pair<FilterOptions, Boolean>>(
            R.id.searchFragment,
            SearchFilterByDialogFragment.RESULT_KEY
        ) {
            if (it.second) {
                vm.transition(SearchState.Event.HandleFilterChange(it.first))
            }
        }
    }

    private fun handleSuggestions(
        categorySuggestion: List<Triple<String, String, String>>,
        simpleSuggestion: List<String>,
        query: String
    ) {
        val suggestionList: MutableList<RecentSearchItem> = mutableListOf()
        val categoryList: MutableList<SuggestionCategoryItem> = mutableListOf()
        suggestionList.clear()
        categoryList.clear()
        if (categorySuggestion.isNotEmpty()) {
            groupieCat.clear()
            categorySuggestion.map {
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
        if (simpleSuggestion.isNotEmpty()) {
            groupie.clear()
            simpleSuggestion.map { title ->
                RecentSearchItem(query, title, true, {
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.apply {
                            searchField.setText(title)
                            searchProgressIndicator.isGone = false
                            categorySuggestionRV.isGone = true
                            categoryDivider.isGone = true
                            recentSearchResultsRV.isGone = true
                        }
                    }
                    vm.transition(SearchState.Event.PressedToSearch(title))
                }, {})
            }.also {
                suggestionList.addAll(it)
            }
            groupie.addAll(suggestionList)
        }
    }

}

enum class SearchType {
    Search, Suggestions, Results
}