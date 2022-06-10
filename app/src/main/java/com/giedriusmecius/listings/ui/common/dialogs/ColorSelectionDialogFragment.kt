package com.giedriusmecius.listings.ui.common.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.databinding.DialogColorSelectionFragmentBinding
import com.giedriusmecius.listings.ui.common.base.BaseDialogFragment
import com.giedriusmecius.listings.ui.common.groupie.ColorItem
import com.giedriusmecius.listings.utils.extensions.setNavigationResult
import com.xwray.groupie.GroupieAdapter

class ColorSelectionDialogFragment :
    BaseDialogFragment<DialogColorSelectionFragmentBinding>(DialogColorSelectionFragmentBinding::inflate) {

    private val groupie = GroupieAdapter()
    private val navArgs by navArgs<ColorSelectionDialogFragmentArgs>()
    private var userColor: Pair<String, String> = Pair("", "")

    private val colors = listOf(
        ColorItem("#FFFFFF", "White", false, ::onColorSelected),
        ColorItem("#808080", "Grey", false, ::onColorSelected),
        ColorItem("#000000", "Black", false, ::onColorSelected),
        ColorItem("#FF0000", "Red", false, ::onColorSelected),
        ColorItem("#800000", "Maroon", false, ::onColorSelected),
        ColorItem("#808000", "Olive", false, ::onColorSelected),
        ColorItem("#008000", "Green", false, ::onColorSelected),
        ColorItem("#0000FF", "Blue", false, ::onColorSelected),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userColor = Pair("", navArgs.colorName ?: "")

        with(binding) {
            colorRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = groupie
            }
            groupie.addAll(colors)
            setupView()
        }
    }

    private fun setupView() {
        colors.firstOrNull {
            it.colorName.equals(userColor.second, true)
        }.let {
            val sizeIndex = colors.indexOf(it)
            colors[sizeIndex].isSelected = true
        }
    }

    private fun onColorSelected(selectedColor: String, colorName: String) {
        val updateItems = arrayListOf<Int>()
        colors.forEachIndexed { index, color ->
            if (color.isSelected && color.colorName != colorName) {
                updateItems.add(index)
                color.isSelected = false
            } else if (!color.isSelected && color.colorName == colorName) {
                updateItems.add(index)
                color.isSelected = true
                userColor = Pair(color.color, color.colorName)
            }
        }
        updateItems.forEach { updateIndex ->
            groupie.notifyItemChanged(updateIndex, colors.size)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        if (userColor.first != "") {
            setResult(userColor)
        }
    }

    fun setResult(value: Pair<String, String>) {
        setNavigationResult(RESULT_KEY, value)
    }

    companion object {
        const val RESULT_KEY = "colorSelectionDialogResultKey"
    }

}