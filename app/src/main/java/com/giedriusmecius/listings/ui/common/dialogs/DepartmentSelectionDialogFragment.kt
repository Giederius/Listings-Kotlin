package com.giedriusmecius.listings.ui.common.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.navigation.fragment.navArgs
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.DialogDepartmentSelectionBinding
import com.giedriusmecius.listings.ui.common.base.BaseDialogFragment
import com.giedriusmecius.listings.utils.extensions.setNavigationResult

class DepartmentSelectionDialogFragment :
    BaseDialogFragment<DialogDepartmentSelectionBinding>(DialogDepartmentSelectionBinding::inflate) {
    private val navArgs by navArgs<DepartmentSelectionDialogFragmentArgs>()
    var selectedDepartment = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            departmentRadioGroup.apply {
                when (navArgs.departmentName) {
                    "Women" -> this.check(R.id.women)
                    "Men" -> this.check(R.id.men)
                    "Girl" -> this.check(R.id.girl)
                    "Boy" -> this.check(R.id.boy)
                    "" -> this.check(R.id.none)
                }

                setOnCheckedChangeListener { _, checkedId ->
                    when (findViewById<RadioButton>(checkedId).text) {
                        getString(R.string.departmentDialog_women) -> { // women
                            selectedDepartment = "Women"
                        }
                        getString(R.string.departmentDialog_man) -> { // men
                            selectedDepartment = "Men"
                        }
                        getString(R.string.departmentDialog_girl) -> { // girl
                            selectedDepartment = "Girl"
                        }
                        getString(R.string.departmentDialog_boy) -> { // boy
                            selectedDepartment = "Boy"
                        }
                        getString(R.string.departmentDialog_none) -> { // none
                            selectedDepartment = ""
                        }
                    }
                }
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        if (selectedDepartment != "") {
            setResult(selectedDepartment)
        }
    }

    private fun setResult(value: String) {
        setNavigationResult(RESULT_KEY, value)
    }

    companion object {
        const val RESULT_KEY = "departmentSelectionResultKey"
    }
}