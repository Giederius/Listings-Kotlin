package com.giedriusmecius.listings.ui.common.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.navigation.fragment.navArgs
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.DialogSearchSortBinding
import com.giedriusmecius.listings.ui.common.base.BaseDialogFragment
import com.giedriusmecius.listings.utils.extensions.setNavigationResult

class SearchSortByDialogFragment :
    BaseDialogFragment<DialogSearchSortBinding>(DialogSearchSortBinding::inflate) {

    private var sortBy = ""
    private val args by navArgs<SearchSortByDialogFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            searchSortRadioGroup.apply {
                when (args.sortBy) {
                    "az" -> this.check(R.id.az)
                    "za" -> this.check(R.id.za)
                    "priceLow" -> this.check(R.id.priceLow)
                    "priceHigh" -> this.check(R.id.priceHigh)
                }

                setOnCheckedChangeListener { _, checkedId ->
                    when (findViewById<RadioButton>(checkedId).text) {
                        getString(R.string.common_sort_alphabeticallyAZ) -> sortBy = "az"
                        getString(R.string.common_sort_alphabeticallyZA) -> sortBy = "za"
                        getString(R.string.common_sort_priceLowHigh) -> sortBy = "priceLow"
                        getString(R.string.common_sort_priceHighLow) -> sortBy = "priceHigh"
                    }
                }
            }
        }

    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        if (sortBy != args.sortBy) {
            setResult(sortBy)
        }
    }

    private fun setResult(value: String) {
        setNavigationResult(RESULT_KEY, value)
    }

    companion object {
        const val RESULT_KEY = "searchSortResultKey"
    }
}