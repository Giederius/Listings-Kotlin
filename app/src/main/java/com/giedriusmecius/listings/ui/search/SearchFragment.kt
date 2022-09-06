package com.giedriusmecius.listings.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
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
import com.giedriusmecius.listings.data.local.FilterData
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.databinding.FragmentSearchBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.dialogs.SearchFilterByDialogFragment
import com.giedriusmecius.listings.ui.common.dialogs.SearchSortByDialogFragment
import com.giedriusmecius.listings.ui.common.groupie.ProductItem
import com.giedriusmecius.listings.ui.common.groupie.RecentSearchItem
import com.giedriusmecius.listings.ui.common.groupie.SuggestionCategoryItem
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
                resultSortBtn.toggleState(newState.filterData.isSortingActive)
                resultFilterBtn.toggleState(newState.filterData.isFilterActive)
                bottomFABSortBtn.toggleState(newState.filterData.isSortingActive)
                bottomFABFilterBtn.toggleState(newState.filterData.isFilterActive)
            }

            if (oldState?.searchUIState != newState.searchUIState) {
                Log.d("MANO", "${newState.searchUIState}")
            }

            if (oldState?.searchQuery != newState.searchQuery) {
                binding.searchField.setText(newState.searchQuery)
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
                    setupUIForSearchResults()
                    if (cmd.results.isNotEmpty()) {
                        setupSearchResults(cmd.results)
                    } else {
                        binding.noSearchResultFound.isGone = false
                        setupSearchResults(emptyList())
                    }
                }

                is SearchState.Command.OpenSortDialog -> {
                    navigate(SearchFragmentDirections.searchFragmentToSortDialog(cmd.sortBy))
                }
                is SearchState.Command.OpenFilterDialog -> {
                    navigate(
                        SearchFragmentDirections.searchFragmentToFilterDialog(
                            cmd.filterData
                        )
                    )
                }
                SearchState.Command.SetupSearchUI -> {
                    setupUIForSearch()
                }
                is SearchState.Command.SetupSuggestionsUI -> {
                    setupUIForSuggestions()
                    handleSuggestions(cmd.categorySuggestion, cmd.simpleSuggestion, cmd.query)
                }
                is SearchState.Command.OpenProduct -> {
                    navigate(SearchFragmentDirections.globalProductDialogFragmentAction(cmd.product))
                }
                else -> {

                }
            }
        }
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
                    vm.transition(SearchState.Event.TappedOnProduct(it.id))
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


    @SuppressLint("ClickableViewAccessibility")
    private fun setupSearchField() {
        binding.apply {
            searchField.apply {
                setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            vm.transition(SearchState.Event.TappedSearchField)
                            searchTopBar.apply {
                                setTransition(R.id.fromSearchToResult)
                                transitionToStart()
                            }
                            true
                        }
                        else -> false
                    }
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
                            Log.d("MANOafterTextChanged", "text changed: $s")
                            if (s.toString() == "") {
                                binding.apply {
                                    setupUIForSearch()
                                    groupie.clear()
                                    groupie.addAll(recentSearchQueries)
                                    vm.transition(SearchState.Event.ChangedSearchUI(SearchType.Search))
                                }
                            } else {
                                delay(500)
                                Log.d("MANOafterTextChanged", "1")
                                vm.transition(SearchState.Event.TypedInSearch(s.toString()))
                            }
                        }
                    }
                })

                this.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                        Log.d("MANOpressedreturn", "2 ${this.text}")
                        if (this.text.toString() != "") {
                            vm.transition(SearchState.Event.PressedToSearch(this.text.toString()))
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
//        lifecycleScope.launch(Dispatchers.Main) {
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
            searchField.apply {
                setSelection(this.text.length)
                showKeyboard()
            }
            noSearchResultFound.isGone = true
//            }
        }
    }

    private fun setupUIForSearchResults() {
        with(binding) {
            recentSearchesTitle.isGone = true
            recentSearchResultsRV.isGone = true
            searchField.hideKeyboard()
            searchTopBar.apply {
                setTransition(R.id.fromSearchToResult)
                transitionToEnd()
            }
            categorySuggestionRV.isGone = true
            categoryDivider.isGone = true
            searchResultRV.isGone = true
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
            Log.d("MANO", "$it")
            if (it.isNotEmpty()) {
                vm.transition(SearchState.Event.ChangedSortBy(it))
            }
        }
    }

    private fun listenForFilterDialogResult() {
        getNavigationResult<Pair<FilterData, Boolean>>(
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
                    Snackbar.make(binding.root, "not implemented", Snackbar.LENGTH_SHORT).show()
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