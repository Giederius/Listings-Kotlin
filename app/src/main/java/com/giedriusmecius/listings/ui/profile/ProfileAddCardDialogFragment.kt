package com.giedriusmecius.listings.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.CC
import com.giedriusmecius.listings.databinding.DialogProfileAddCardBinding
import com.giedriusmecius.listings.utils.extensions.showKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileAddCardDialogFragment : BottomSheetDialogFragment() {
    private var _binding: DialogProfileAddCardBinding? = null
    private val binding get() = _binding!!
    private val cardTypeCheck = ArrayList<String>()
    private val newCard: CC? = null

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogProfileAddCardBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    // todo add animations to change textinput fields
    // todo fix shit with regex to check everything.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        cardTypeCheck.add("^4[0-9]{6,}$")
//        cardTypeCheck.add("^5[1-5][0-9]{5,}$")

        with(binding) {
            addCardNumberTextEdit.addTextChangedListener {
                Log.d("MANO", it.toString())
            }

            addCardNumberTextEdit.apply {
                showKeyboard()
                onFocusChangeListener = View.OnFocusChangeListener { view, b ->
                    view.animate()
                        .translationY(-50F)
                        .alpha(0F)
                        .setDuration(1000)
                        .setListener(null)
                }

            }

            addCardNameTextEdit.apply {
                isGone = false
                alpha = 0F
                translationY = 200F
                onFocusChangeListener = View.OnFocusChangeListener { view, b ->
                    view.animate()
                        .alpha(1F)
                        .translationY(0F)
                        .setDuration(2000)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val CARD_NUMBER_CHUNKS = 4
    }
}