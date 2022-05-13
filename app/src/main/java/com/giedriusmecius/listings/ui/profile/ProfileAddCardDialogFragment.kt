package com.giedriusmecius.listings.ui.profile

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.CC
import com.giedriusmecius.listings.data.remote.model.CardType
import com.giedriusmecius.listings.databinding.DialogProfileAddCardBinding
import com.giedriusmecius.listings.utils.extensions.setNavigationResult
import com.giedriusmecius.listings.utils.extensions.showAlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ProfileAddCardDialogFragment : BottomSheetDialogFragment() {
    private var _binding: DialogProfileAddCardBinding? = null
    private val binding get() = _binding!!
    private val cardTypeCheck = ArrayList<CardTypes>()
    private var newCard: CC = CC(null, null, null, null, null)
    private var isExpired: Boolean = false

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogProfileAddCardBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardTypeCheck.add(CardTypes.Visa)
        cardTypeCheck.add(CardTypes.MasterCard)

        dialog?.window?.setSoftInputMode(4) // VISIBLE KEYBOARD

        // todo fix the checks for lengths for every field everywhere after done setting up.

        with(binding) {
            addCardNumberTextEdit.apply {
                this.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(
                        p0: CharSequence?, p1: Int, p2: Int, p3: Int
                    ) {
                        val formattedCardNumber =
                            p0.toString().replace(" ", "").chunked(CARD_NUMBER_CHUNKS)
                                .joinToString(" ")
                        if (formattedCardNumber != p0.toString()) {
                            binding.addCardNumberTextEdit.apply {
                                setText(formattedCardNumber)
                                setSelection(formattedCardNumber.length)
                            }
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        val formattedString = s.toString().replace(" ", "")
                        cardTypeCheck.forEach {
                            for (p in cardTypeCheck) {
                                when (p) {
                                    CardTypes.Visa -> {
                                        if (formattedString.matches(p.regex)) {
                                            newCard = newCard.copy(type = CardType.VISA)
                                            binding.cardContainerFrontSide.cardTypeImg.apply {
                                                isGone = false
                                            }
                                        }
                                    }
                                    CardTypes.MasterCard -> {
                                        if (formattedString.matches(p.regex)) {
                                            newCard = newCard.copy(type = CardType.MASTERCARD)
                                            binding.cardContainerFrontSide.cardTypeImg.apply {
                                                setImageDrawable(resources.getDrawable(R.drawable.icon_mastercard))
                                                isGone = false
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                })
                this.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                        val formattedString = this.text.toString().replace(" ", "")
                        newCard = newCard.copy(number = formattedString.toLong())
                        cardContainerFrontSide.cardNumber.text = this.text
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
                            .setDuration(400)
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
                        cardContainerFrontSide.cardUserName.text = this.text
                        newCard = newCard.copy(name = this.text.toString())
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
                            .setDuration(400)
                            .setListener(null)
                            .withEndAction { v.isGone = true }
                        true
                    } else {
                        false
                    }
                }
            }
            addCardExpDateTextEdit.apply {
                this.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(
                        p0: CharSequence?, p1: Int, p2: Int, p3: Int
                    ) {
                        val expDate = p0.toString()
                        if (expDate.length == 2) {
                            val formattedExpDate = StringBuilder(expDate).insert(2, "/").toString()
                            binding.addCardExpDateTextEdit.apply {
                                setText(formattedExpDate)
                                setSelection(formattedExpDate.length)
                            }
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        isExpired = isValidCard(s.toString())
                    }

                })
                this.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                        val formattedExpDate = this.text
                        if (isExpired) {
                            cardContainerFrontSide.cardExpDate.apply {
                                setTextColor(resources.getColor(R.color.highlightRed))
                                this.startAnimation(
                                    AnimationUtils.loadAnimation(
                                        context,
                                        R.anim.anim_shake
                                    )
                                )
                                Toast.makeText(context, "Card not valid", Toast.LENGTH_SHORT).show()
                            }
                            this.clearComposingText()
                        } else {
                            cardContainerFrontSide.cardExpDate.text = formattedExpDate
                            newCard = newCard.copy(expDate = formattedExpDate.toString())
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
                            rotateCard(cardContainerBack, cardContainerFront)
                            v.animate()
                                .translationY(-50F)
                                .alpha(0F)
                                .setDuration(400)
                                .setStartDelay(100)
                                .setListener(null)
                                .withEndAction { v.isGone = true }
                        }
                        true
                    } else {
                        false
                    }
                }
            }
            addCardSecurityTextEdit.apply {
                this.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                        cardContainerBackside.cardCvv.text = this.text
                        newCard = newCard.copy(ccv = Integer.parseInt(this.text.toString()))
                        Log.d("MANO", newCard.toString())
//                        setResult(newCard)
                        Toast.makeText(context, "Card added!", Toast.LENGTH_SHORT).show()
                        // todo add popup to confirm data or edit
                        showAlertDialog(
                            this.context,
                            onPositiveClick = {
                                Toast.makeText(
                                    context,
                                    "Positive",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dismiss()
                            },
                            onNegativeClick = {
                                Toast.makeText(
                                    context,
                                    "Negative!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
//                        dismiss()
                        true
                    } else {
                        false
                    }
                }
            }
        }
    }

    private fun isValidCard(ccExpDate: String): Boolean {
        val monthFormatter = DateTimeFormatter.ofPattern("MM/yy")
        return try {
            val lastValidMonth = YearMonth.parse(ccExpDate, monthFormatter)
            if (YearMonth.now(ZoneId.systemDefault()).isAfter(lastValidMonth)) {
                println("Credit card has expired")
                true
            } else {
                false
            }
        } catch (e: DateTimeParseException) {
            print(e.message)
            return false
        }
    }

    private fun rotateCard(visibleView: View, invisibleView: View) {
        visibleView.isVisible = true
        val scale = context?.resources?.displayMetrics?.density
        val cameraDist = 8000 * scale!!
        visibleView.cameraDistance = cameraDist
        invisibleView.cameraDistance = cameraDist

        val flipOutAnimatorSet =
            AnimatorInflater.loadAnimator(context, R.animator.flip_out) as AnimatorSet
        val flipInAnimatorSet =
            AnimatorInflater.loadAnimator(context, R.animator.flip_in) as AnimatorSet

        flipOutAnimatorSet.setTarget(invisibleView)
        flipInAnimatorSet.setTarget(visibleView)
        flipOutAnimatorSet.start()
        flipInAnimatorSet.start()
    }

    override fun onResume() {
        super.onResume()
        binding.addCardNumberTextEdit.requestFocus()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setResult(value: CC) {
        setNavigationResult(ProfileFragment.RESULT_KEY, value)
        dismiss()
    }

    companion object {
        private const val CARD_NUMBER_CHUNKS = 4
        const val RESULT_KEY = "profileAddCardResultKey"
    }

    enum class CardTypes(val regex: Regex) {
        Visa("^4[0-9]{6,}$".toRegex()), MasterCard("^5[1-5][0-9]{5,}$".toRegex())
    }
}