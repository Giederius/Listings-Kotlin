package com.giedriusmecius.listings.ui.common.dialogs

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.data.local.Size
import com.giedriusmecius.listings.databinding.DialogSizeBinding
import com.giedriusmecius.listings.ui.common.base.BaseDialogFragment
import com.giedriusmecius.listings.ui.common.groupie.SizeItem
import com.giedriusmecius.listings.utils.extensions.setNavigationResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xwray.groupie.GroupieAdapter

class SizeDialogFragment : BaseDialogFragment<DialogSizeBinding>(DialogSizeBinding::inflate) {
    private val navArgs by navArgs<SizeDialogFragmentArgs>()

    val groupie = GroupieAdapter()
    private val selectedSize: Size = Size(0, "", false)

    private val sizes = listOf(
        Size(22, "xxs", false),
        Size(24, "xs", false),
        Size(26, "s", false),
        Size(28, "m", false),
        Size(30, "l", false),
        Size(32, "xl", false),
        Size(34, "xxl", false)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED

        with(binding) {
            sizeRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = groupie
            }

            if (navArgs.size != null) {
                sizes.map {
                    if (it == navArgs.size) {
                        SizeItem(
                            navArgs.size!!.us,
                            navArgs.size!!.eu,
                           true
                        ).also { groupie.add(it) }
                    } else {
                        SizeItem(it.us, it.eu, it.isFavorite).also { groupie.add(it) }
                    }
                }
            } else {
                sizes.map {
                    SizeItem(it.us, it.eu, it.isFavorite).also { groupie.add(it) }
                }
            }

            saveBtn.setOnClickListener {
                setResult(selectedSize)
            }
        }
    }

    private fun onItemClick(size: Size, isSelected: Boolean) {
        val sizeItem = SizeItem(size.us, size.eu, !isSelected).also { }
    }

    fun setResult(value: Size) {
        setNavigationResult(RESULT_KEY, value)
        dismiss()
    }

    companion object {
        const val RESULT_KEY = "sizeDialogFragmentResultKey"
    }
}