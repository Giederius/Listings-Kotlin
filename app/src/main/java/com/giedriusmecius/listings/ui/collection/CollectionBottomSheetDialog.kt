package com.giedriusmecius.listings.ui.collection

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.local.FilterData
import com.giedriusmecius.listings.databinding.DialogCollectionFragmentBinding
import com.giedriusmecius.listings.ui.common.base.BaseDialogFragment
import com.giedriusmecius.listings.ui.common.dialogs.SearchFilterByDialogFragment
import com.giedriusmecius.listings.ui.common.dialogs.SearchSortByDialogFragment
import com.giedriusmecius.listings.ui.common.groupie.ProductItem
import com.giedriusmecius.listings.ui.search.SearchFragmentDirections
import com.giedriusmecius.listings.ui.search.SearchState
import com.giedriusmecius.listings.ui.search.SearchViewModel
import com.giedriusmecius.listings.utils.UserPreferences
import com.giedriusmecius.listings.utils.extensions.getNavigationResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CollectionBottomSheetDialog :
    BaseDialogFragment<DialogCollectionFragmentBinding>(DialogCollectionFragmentBinding::inflate) {

    private val vm by viewModels<SearchViewModel>()
    private var groupie = GroupieAdapter()
    private var hasScrolledToTop = true
    private var filterData: FilterData = FilterData()

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        dialog.behavior.skipCollapsed = true
        dialog.behavior.peekHeight = resources.displayMetrics.heightPixels
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.isDraggable = false
        return dialog
    }

    override fun onStart() {
        super.onStart()
        binding.root.layoutParams.height = resources.displayMetrics.heightPixels
        binding.root.requestLayout()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayout()
        listenForSortDialogResult()
        listenForFilterDialogResult()
        vm.resultFilterData.observe(viewLifecycleOwner) {
            filterData = it
            binding.apply {
                sortBtn.toggleState(it.isSortingActive)
                filterBtn.toggleState(it.isFilterActive)
            }
        }

        binding.apply {
            vm.searchResults.observe(viewLifecycleOwner) { Log.d("MANO", it.size.toString()) }

            collectionProductRV.apply {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = groupie
            }

            setupProducts()

            backBtn.setOnClickListener {
                dismiss()
            }
            sortBtn.setOnClickListener {
                navigate(CollectionBottomSheetDialogDirections.globalToSortDialog(filterData.sortData))
            }

            filterBtn.setOnClickListener {
                navigate(
                    CollectionBottomSheetDialogDirections.globalToFilterDialog(filterData)
                )
            }

            nestedScroll.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                val firstItemTop = collectionProductRV.get(0).y.toInt()
                val firstItemTopMiddle = 400
                when {
                    scrollY > firstItemTop -> {
                        if (hasScrolledToTop) {
                            hasScrolledToTop = false
                            motionLayout.transitionToEnd()
                        }
                    }
                    scrollY < firstItemTopMiddle -> {
                        if (!hasScrolledToTop) {
                            hasScrolledToTop = true
                            motionLayout.transitionToStart()
                        }
                    }
                }
            }
        }
    }

    private fun setupProducts() {
        val results = userPreferences.getAllProducts()
        binding.apply {
            groupie.clear()
            val products: MutableList<ProductItem> = mutableListOf()
            results.map {
                it.products.map {
                    ProductItem(
                        title = it.title,
                        image = it.image,
                        price = it.price,
                        discountedPrice = it.price,
                        onClick = {
                            navigate(SearchFragmentDirections.globalProductDialogFragmentAction(it))
                        }).also {
                        products.add(it)
                    }
                }
            }
            groupie.addAll(products)
        }
    }

    private fun setupTabLayout() {
        binding.apply {
            // tags should be in this tablayout, once CLICKED they should update the RV
            // sort out the products and etc.
            tabLayout.addTab(tabLayout.newTab().setText("All"))
            tabLayout.addTab(tabLayout.newTab().setText("This"))
            tabLayout.addTab(tabLayout.newTab().setText("that"))
            tabLayout.addTab(tabLayout.newTab().setText("tehn"))
        }
    }

    private fun listenForSortDialogResult() {
        getNavigationResult<String>(
            R.id.dialogCollectionFragment,
            SearchSortByDialogFragment.RESULT_KEY
        ) {
            if (it.isNotEmpty()) {
                vm.transition(SearchState.Event.ChangedSortBy(it))
            }
        }
    }

    private fun listenForFilterDialogResult() {
        getNavigationResult<Pair<FilterData, Boolean>>(
            R.id.dialogCollectionFragment,
            SearchFilterByDialogFragment.RESULT_KEY
        ) {
            if (it.second) {
                vm.transition(SearchState.Event.HandleFilterChange(it.first))
            }
        }
    }

}