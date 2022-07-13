package com.giedriusmecius.listings.ui.common.groupie

import android.view.View
import coil.load
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemSearchSuggestionCategoryBinding
import com.xwray.groupie.viewbinding.BindableItem

class SuggestionCategoryItem(
    val img: String,
    val titleText: String,
    val descText: String,
    val onClick: () -> Unit
) : BindableItem<ItemSearchSuggestionCategoryBinding>() {

    override fun getLayout(): Int = R.layout.item_search_suggestion_category

    override fun initializeViewBinding(view: View): ItemSearchSuggestionCategoryBinding =
        ItemSearchSuggestionCategoryBinding.bind(view)

    override fun bind(viewBinding: ItemSearchSuggestionCategoryBinding, position: Int) {
        viewBinding.apply {
            root.setOnClickListener {
                onClick()
            }

            suggestionCategoryImg.load(img)
            suggestionCategoryTitle.text = titleText
            suggestionCategoryDescription.text = descText
        }
    }
}