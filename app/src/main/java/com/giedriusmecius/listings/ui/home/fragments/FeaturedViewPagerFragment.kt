package com.giedriusmecius.listings.ui.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.databinding.FragmentFeaturedBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.groupie.ProductItem
import com.giedriusmecius.listings.ui.home.HomeFragmentDirections
import com.giedriusmecius.listings.ui.home.HomeState
import com.giedriusmecius.listings.ui.home.HomeViewModel
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeaturedViewPagerFragment :
    BaseFragment<FragmentFeaturedBinding>(FragmentFeaturedBinding::inflate) {
    private val vm by viewModels<HomeViewModel>()
    private val mAdapter = GroupieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.machine.transition(HomeState.Event.OpenedFeatured)

        vm.fetchedFeaturedProducts.observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }

    private fun updateUI(products: List<Product>?) {
        binding.apply {
            featuredProductRV.apply {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = mAdapter
            }
            val mappedItems = products!!.map {
                ProductItem(
                    title = it.title,
                    image = it.image,
                    price = it.price,
                    discountedPrice = it.price,
                    onClick = {
                        navigate(HomeFragmentDirections.globalProductFragmentAction(it.id))
                    })
            }
            mAdapter.addAll(mappedItems)
        }
    }

//    override fun observeState() {
//        vm.subscribeWithAutoDispose(viewLifecycleOwner) { _, newState ->
//            when (val cmd = newState.command) {
//                is HomeState.Command.HandleFeatured -> {
//                    Log.d("MANO","this is from featured ${cmd.featured.size}")
////                    updateUI()
//                }
//                else -> {}
//            }
//        }
//    }

    companion object {
        fun getInstance(): FeaturedViewPagerFragment {
            return FeaturedViewPagerFragment()
        }
    }
}