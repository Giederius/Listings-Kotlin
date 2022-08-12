package com.giedriusmecius.listings.ui.common.groupie

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemRecentSearchBinding
import com.xwray.groupie.viewbinding.BindableItem

class RecentSearchItem(
    private val query: String,
    private val productTitle: String,
    private val isSuggestion: Boolean,
    val onItemClick: (String) -> Unit,
    val onRemoveClick: () -> Unit
) : BindableItem<ItemRecentSearchBinding>() {
    private var startIndex = 0
    private var endIndex = 0

    override fun getLayout(): Int = R.layout.item_recent_search

    override fun initializeViewBinding(view: View): ItemRecentSearchBinding =
        ItemRecentSearchBinding.bind(view)

    override fun bind(viewBinding: ItemRecentSearchBinding, position: Int) {
        viewBinding.apply {
            if (isSuggestion) {
                searchQueryIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        R.drawable.ic_arrow_45deg
                    )
                )
                val newTypeface = ResourcesCompat.getFont(root.context, R.font.raleway_bold)
                val string = SpannableStringBuilder(productTitle).apply {
                    startIndex = productTitle.indexOf(query, ignoreCase = true)
                    endIndex = startIndex + query.length
                    if (startIndex >= 0 && endIndex > 0) {
                        this.setSpan(
                            newTypeface?.let { TypefaceSpan(it) },
                            startIndex,
                            endIndex,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
                searchQuery.text = string
            } else {
                searchQuery.text = query
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