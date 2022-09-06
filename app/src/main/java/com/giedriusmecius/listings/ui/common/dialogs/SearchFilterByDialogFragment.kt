package com.giedriusmecius.listings.ui.common.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.local.FilterData
import com.giedriusmecius.listings.data.local.FilterOptions
import com.giedriusmecius.listings.databinding.DialogSearchFilterBinding
import com.giedriusmecius.listings.ui.common.base.BaseDialogFragment
import com.giedriusmecius.listings.utils.extensions.setNavigationResult
import com.giedriusmecius.listings.utils.extensions.toCurrency
import com.google.android.material.slider.RangeSlider

class SearchFilterByDialogFragment :
    BaseDialogFragment<DialogSearchFilterBinding>(DialogSearchFilterBinding::inflate) {

    private val args by navArgs<SearchFilterByDialogFragmentArgs>()
    private var userFilter: FilterOptions = FilterOptions(listOf(), listOf())
    private var mainFilter: FilterOptions = FilterOptions(listOf(), listOf())
    private var filterData: FilterData = FilterData()
    private var userCheckedCategories = mutableListOf<String>()
    private var userSelectedPriceRange = listOf<Float>()
    private var createdCheckboxes = mutableListOf<View>()
    private var isSaved = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userFilter = args.filterData.userOptions
        mainFilter = args.filterData.mainOptions
        userCheckedCategories = userFilter.userSelectedCategories.toMutableList()
        userSelectedPriceRange =
            listOf(userFilter.priceRange[0], userFilter.priceRange[1])

        with(binding) {
            searchFilterLowPrice.text = userFilter.priceRange[0].toCurrency()
            searchFilterHighPrice.text = userFilter.priceRange[1].toCurrency()

            searchFilterPriceSlider.apply {
                setValues(userFilter.priceRange[0], userFilter.priceRange[1])
                setCustomThumbDrawable(R.drawable.slider_thumb_active)

                addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                    override fun onStartTrackingTouch(slider: RangeSlider) {
                        searchFilterLowPrice.text = slider.values[0].toCurrency()
                        searchFilterHighPrice.text = slider.values[1].toCurrency()
                    }

                    override fun onStopTrackingTouch(slider: RangeSlider) {
                        searchFilterLowPrice.text = slider.values[0].toCurrency()
                        searchFilterHighPrice.text = slider.values[1].toCurrency()
                        userSelectedPriceRange = slider.values
                    }
                })
            }
            for (i in userFilter.allCategories) {
                val checkBox = CheckBox(context)

                checkBox.apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    text =
                        userFilter.allCategories[userFilter.allCategories.indexOf(i)]
                    id = View.generateViewId()
                    isChecked = userCheckedCategories.contains(this.text)
                    setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            userCheckedCategories.add(buttonView.text.toString())
                        } else {
                            userCheckedCategories.remove(buttonView.text.toString())
                        }
                    }
                }
                checkboxGroup.addView(checkBox)
                createdCheckboxes.add(checkBox)
            }

            searchFilterClearFilter.setOnClickListener {
                clearFilter()
            }
            searchFilterSaveFilter.setOnClickListener {
                saveFilter()
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        setResult(filterData to isSaved)
    }

    private fun clearFilter() {
        with(binding) {

            searchFilterLowPrice.text = mainFilter.priceRange[0].toCurrency()
            searchFilterHighPrice.text = mainFilter.priceRange[1].toCurrency()

            createdCheckboxes.forEach {
                (it as CheckBox).isChecked = true
            }
            userCheckedCategories = mainFilter.userSelectedCategories.toMutableList()
            userSelectedPriceRange = mainFilter.priceRange
            searchFilterPriceSlider.setValues(mainFilter.priceRange[0], mainFilter.priceRange[1])
            isSaved = true
            userFilter = mainFilter
        }
    }

    private fun saveFilter() {
        filterData = filterData.copy(
            userOptions = userFilter.copy(
                priceRange = userSelectedPriceRange,
                userSelectedCategories = userCheckedCategories
            )
        )
        Toast.makeText(context, "Your filter settings have been saved.", Toast.LENGTH_SHORT).show()
        isSaved = true
    }

    private fun setResult(value: Pair<FilterData, Boolean>) {
        setNavigationResult(RESULT_KEY, value)
    }

    companion object {
        const val RESULT_KEY = "searchFilterDialogResultKey"
    }
}