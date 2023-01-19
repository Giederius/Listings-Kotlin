package com.giedriusmecius.listings.ui.home

import androidx.lifecycle.MutableLiveData
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.utils.UserPreferences
import com.giedriusmecius.listings.utils.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userPrefs: UserPreferences
) : BaseViewModel<HomeState, HomeState.Event>(HomeState()) {

    val _fetchedFeatured = MutableLiveData<List<Product>>()
    val fetchedFeaturedProducts : MutableLiveData<List<Product>>
        get() = _fetchedFeatured


    override fun handleState(newState: HomeState) {
        when (val req = newState.request) {
            is HomeState.Request.FetchFeatured -> {
                val products =
                    userPrefs.getAllProducts().flatMap { it.products }
                val featured = products.subList(2, 10)
                _fetchedFeatured.value = featured
//                transition(HomeState.Event.ReceivedFeatured(featured))
            }
            else -> {

            }
        }
    }
}