package com.giedriusmecius.listings.ui.profile

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.CC
import com.giedriusmecius.listings.databinding.DialogProfileAddCardBinding
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

        dialog?.window?.setSoftInputMode(4) // VISIBLE KEYBOARD

        with(binding) {
            addCardNumberTextEdit.apply {
                this.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                        // todo handle text
                        addCardNameTextEdit.apply {
                            isGone = false
                            alpha = 0F
                            translationY = 50F
                            requestFocus()
                            animate()
                                .translationY(0F)
                                .alpha(1F)
                                .setDuration(700)
                                .setListener(null)
                        }

                        v.animate()
                            .translationY(-50F)
                            .alpha(0F)
                            .setDuration(700)
                            .setListener(null)
                            .withEndAction { v.isGone = true }
                        true
                    } else {
                        false
                    }
                }
            }

            addCardNameTextEdit.apply {
                this.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                        addCardExpDateTextEdit.apply {
                            isGone = false
                            alpha = 0F
                            translationY = 50F
                            requestFocus()
                            animate()
                                .translationY(0F)
                                .alpha(1F)
                                .setDuration(700)
                                .setListener(null)
                        }

                        v.animate()
                            .translationY(-50F)
                            .alpha(0F)
                            .setDuration(700)
                            .setListener(null)
                            .withEndAction { v.isGone = true }
                        true
                    } else {
                        false
                    }
                }
            }
            addCardExpDateTextEdit.apply {

                this.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                        addCardSecurityTextEdit.apply {
                            isGone = false
                            alpha = 0F
                            translationY = 50F
                            requestFocus()
                            animate()
                                .translationY(0F)
                                .alpha(1F)
                                .setDuration(700)
                                .setListener(null)
                        }

                        v.animate()
                            .translationY(-50F)
                            .alpha(0F)
                            .setDuration(700)
                            .setListener(null)
                            .withEndAction { v.isGone = true }
                        true
                    } else {
                        false
                    }
                }
            }
            addCardSecurityTextEdit.apply {
                this.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                        Toast.makeText(context, "HOORAY!", Toast.LENGTH_SHORT).show()
                        true
                    } else {
                        false
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.addCardNumberTextEdit.requestFocus()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val CARD_NUMBER_CHUNKS = 4
    }
}