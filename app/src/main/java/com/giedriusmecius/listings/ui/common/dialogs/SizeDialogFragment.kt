package com.giedriusmecius.listings.ui.common.dialogs

import android.content.DialogInterface
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
    private var userSize: Size? = null

    private val sizes = listOf(
        SizeItem(22, "xxs", false, ::onSizeSelected),
        SizeItem(24, "xs", false, ::onSizeSelected),
        SizeItem(26, "s", false, ::onSizeSelected),
        SizeItem(28, "m", false, ::onSizeSelected),
        SizeItem(30, "l", false, ::onSizeSelected),
        SizeItem(32, "xl", false, ::onSizeSelected),
        SizeItem(34, "xxl", false, ::onSizeSelected)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userSize = navArgs.size

        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED

        with(binding) {
            sizeRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = groupie
            }
            groupie.addAll(sizes)
                setupView()
        }
    }

    private fun setupView() {
        if (userSize?.us != 0) {
            sizes.firstOrNull() {
                it.us == userSize?.us
            }.let {
                val sizeIndex = sizes.indexOf(it)
                sizes[sizeIndex].isSelected = true
            }
        }
    }

    private fun onSizeSelected(selectedSize: Int) {
        val updateItems = arrayListOf<Int>()
        sizes.forEachIndexed { index, size ->
            if (size.isSelected && size.us != selectedSize) {
                updateItems.add(index)
                size.isSelected = false
            } else if (!size.isSelected && size.us == selectedSize) {
                updateItems.add(index)
                size.isSelected = true
                userSize = Size(size.us, size.eu)
            }
        }
        updateItems.forEach { updateIndex ->
            groupie.notifyItemChanged(updateIndex, sizes.size)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        if (userSize != navArgs.size) {
            setResult(userSize!!)
        }
    }

    fun setResult(value: Size) {
        setNavigationResult(RESULT_KEY, value)
        dismiss()
    }

    companion object {
        const val RESULT_KEY = "sizeDialogFragmentResultKey"
    }
}