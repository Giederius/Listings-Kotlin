package com.giedriusmecius.listings.ui.common.groupie

import android.view.View
import androidx.core.content.ContextCompat
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemRecentSearchBinding
import com.xwray.groupie.viewbinding.BindableItem

class RecentSearchItem(
    private val query: String,
    private val isSuggestion: Boolean,
    val onItemClick: (String) -> Unit,
    val onRemoveClick: () -> Unit
) :
    BindableItem<ItemRecentSearchBinding>() {

    override fun getLayout(): Int = R.layout.item_recent_search

    override fun initializeViewBinding(view: View): ItemRecentSearchBinding =
        ItemRecentSearchBinding.bind(view)

    override fun bind(viewBinding: ItemRecentSearchBinding, position: Int) {
        viewBinding.apply {
            searchQuery.text = query


            if (isSuggestion) {
                searchQueryIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        R.drawable.ic_arrow_45deg
                    )
                )
            }

            if (!isSuggestion) {
                searchQueryIcon.setOnClickListener {
                    onRemoveClick()
                }
            }

            root.setOnClickListener {
                onItemClick(query)
            }
        }
    }
}