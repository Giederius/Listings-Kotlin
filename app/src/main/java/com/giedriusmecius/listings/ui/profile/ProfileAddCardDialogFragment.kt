package com.giedriusmecius.listings.ui.profile

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.local.CardType
import com.giedriusmecius.listings.data.local.PaymentMethod
import com.giedriusmecius.listings.databinding.DialogProfileAddCardBinding
import com.giedriusmecius.listings.ui.common.base.BaseDialogFragment
import com.giedriusmecius.listings.utils.extensions.animateLeave
import com.giedriusmecius.listings.utils.extensions.animateShowUp
import com.giedriusmecius.listings.utils.extensions.setNavigationResult
import com.giedriusmecius.listings.utils.extensions.showToast
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ProfileAddCardDialogFragment :
    BaseDialogFragment<DialogProfileAddCardBinding>(DialogProfileAddCardBinding::inflate) {
    private val cardTypeCheck = ArrayList<CardTypes>()
    private var card = PaymentMethod(null, null, null, null, null)
    private var isExpired: Boolean = false
    private val navArgs by navArgs<ProfileAddCardDialogFragmentArgs>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cardTypeCheck.add(CardTypes.Visa)
        cardTypeCheck.add(CardTypes.MasterCard)

        dialog?.window?.setSoftInputMode(5) // VISIBLE KEYBOARD

        // todo fix the checks for lengths for every field everywhere after done setting up.

        if (navArgs.cardInfo != null && navArgs.isEdit) {
            setupEditPaymentMethod(navArgs.cardInfo!!)
            card = navArgs.cardInfo!!
        }

        with(binding) {
            setupEditClicks()

            addCardNumberTextEdit.apply {
                if (navArgs.isEdit) {
                    setText(cardContainerFrontSide.cardNumber.text)
                }
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
                                            card = card.copy(type = CardType.VISA)
                                            binding.cardContainerFrontSide.cardTypeImg.apply {
                                                isGone = false
                                            }
                                        }
                                    }
                                    CardTypes.MasterCard -> {
                                        if (formattedString.matches(p.regex)) {
                                            card = card.copy(type = CardType.MASTERCARD)
                                            binding.cardContainerFrontSide.cardTypeImg.apply {
                                                setImageDrawable(
                                                    ContextCompat.getDrawable(
                                                        context,
                                                        R.drawable.icon_mastercard
                                                    )
                                                )
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
                        card = card.copy(number = formattedString.toLong())
                        cardContainerFrontSide.cardNumber.text = this.text
                        addCardNameTextEdit.animateShowUp()
                        textInputLabel.text = getString(R.string.addCardDialog_nameOnCardlabel)
                        v.animateLeave()
                        true
                    } else {
                        false
                    }
                }
            }

            addCardNameTextEdit.apply {
                if (navArgs.isEdit) {
                    setText(cardContainerFrontSide.cardUserName.text)
                }
                this.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                        cardContainerFrontSide.cardUserName.text = this.text
                        card = card.copy(name = this.text.toString())
                        addCardExpDateTextEdit.animateShowUp()
                        v.animateLeave()
                        textInputLabel.text = getString(R.string.addCardDialog_expDatelabel)
                        true
                    } else {
                        false
                    }
                }
            }

            addCardExpDateTextEdit.apply {
                if (navArgs.isEdit) {
                    setText(cardContainerFrontSide.cardExpDate.text)
                }
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
                    when {
                        keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN -> {
                            val formattedExpDate = this.text
                            if (isExpired) {
                                cardContainerFrontSide.cardExpDate.apply {
                                    setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.highlightRed
                                        )
                                    )
                                    this.startAnimation(
                                        AnimationUtils.loadAnimation(
                                            context,
                                            R.anim.anim_shake
                                        )
                                    )
                                    showToast(context, "Card not valid")
                                }
                                false
                            } else {
                                cardContainerFrontSide.cardExpDate.apply {
                                    text = formattedExpDate
                                    setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.grayedOutTextColor
                                        )
                                    )
                                }
                                card = card.copy(expDate = formattedExpDate.toString())
                                addCardSecurityTextEdit.animateShowUp()
                                rotateCard(cardContainerBack, cardContainerFront)
                                v.animateLeave()
                                textInputLabel.text = getString(R.string.addCardDialog_ccvlabel)

                                cardContainerFrontSide.apply {
                                    cardExpDate.isGone = true
                                    cardNumber.isGone = true
                                    cardUserName.isGone = true
                                }
                                true
                            }

                        }
                        keyCode == KeyEvent.KEYCODE_DEL && event?.action == KeyEvent.ACTION_DOWN -> {
                            this.apply {
                                clearComposingText()
                                text.clear()
                            }
                            true
                        }
                        else -> false
                    }
                }
            }

            addCardSecurityTextEdit.apply {
                if (navArgs.isEdit) {
                    setText(cardContainerBackside.cardCvv.text)
                }
                this.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                        cardContainerBackside.cardCvv.text = this.text
                        card = card.copy(ccv = Integer.parseInt(this.text.toString()))

                        // todo add popup to confirm data or edit
                        setResult(card)
//                        showAlertDialog(
//                            this.context, "Is everything in order?",
//                            "Number: ${card.number}\nName: ${card.name}\nExp. date: ${card.expDate}\nCCV: ${card.ccv}",
//                            onPositiveClick = {
//
//                                showToast(context, "Card added!")
//                            },
//                            onNegativeClick = {
//                                showToast(context, "Negative")
//                            })
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

    private fun setupEditPaymentMethod(method: PaymentMethod) {
        with(binding) {
            cardContainerBackside.cardCvv.text = method.ccv.toString()
            cardContainerFrontSide.apply {
                cardNumber.text =
                    method.number.toString().chunked(CARD_NUMBER_CHUNKS).joinToString(" ")
                cardUserName.text = method.name
                cardExpDate.text = method.expDate
                cardTypeImg.apply {
                    when (method.type) {
                        CardType.VISA -> isGone = false
                        CardType.MASTERCARD -> setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.icon_mastercard
                            )
                        )
                    }
                }
            }
        }
    }

    private fun setupEditClicks() {
        with(binding) {
            cardContainerFrontSide.cardNumber.setOnClickListener {
                textInputLabel.text = getString(R.string.addCardDialog_cardNumberLabel)
                val text = cardContainerFrontSide.cardNumber.text
                if (addCardNumberTextEdit.isVisible) {
                    when {
                        text.contains("[0-9]".toRegex()) -> {
                            addCardNumberTextEdit.apply {
                                setText(text)
                                setSelection(text.length)
                            }
                        }
                        else -> addCardNumberTextEdit.setText("")
                    }
                } else {
                    setupHideAndShow(
                        addCardNumberTextEdit,
                        addCardNameTextEdit,
                        addCardExpDateTextEdit
                    )
                }
            }

            cardContainerFrontSide.cardUserName.setOnClickListener {
                textInputLabel.text = getString(R.string.addCardDialog_nameOnCardlabel)
                val text = cardContainerFrontSide.cardUserName.text
                if (addCardNameTextEdit.isVisible) {
                    when {
                        text != "NAME" -> {
                            addCardNameTextEdit.apply {
                                setText(text)
                                setSelection(text.length)
                            }
                        }
                        else -> addCardNameTextEdit.setText("")
                    }
                } else {
                    setupHideAndShow(
                        addCardNameTextEdit,
                        addCardExpDateTextEdit,
                        addCardNumberTextEdit
                    )
                }
            }

            cardContainerFrontSide.cardExpDate.setOnClickListener {
                textInputLabel.text = getString(R.string.addCardDialog_expDatelabel)
                if (addCardExpDateTextEdit.isVisible) {
                    addCardExpDateTextEdit.setText("")
                } else {
                    setupHideAndShow(
                        addCardExpDateTextEdit,
                        addCardNameTextEdit,
                        addCardNumberTextEdit
                    )
                }
            }

            cardContainerBackside.cardCvv.setOnClickListener {
                textInputLabel.text = getString(R.string.addCardDialog_ccvlabel)
                val text = cardContainerBackside.cardCvv.text
                if (text.contains("[0-9]".toRegex())) {
                    addCardSecurityTextEdit.apply {
                        setText(text)
                        setSelection(text.length)
                    }
                } else {
                    addCardSecurityTextEdit.setText("")
                }
            }
        }
    }

    private fun setupHideAndShow(viewToShow: View, viewToHide1: View, viewToHide2: View) {
        when {
            viewToHide1.isVisible -> viewToHide1.animateLeave()
            viewToHide2.isVisible -> viewToHide2.animateLeave()
        }
        viewToShow.animateShowUp()
    }

    private fun isValidCard(ccExpDate: String): Boolean {
        val monthFormatter = DateTimeFormatter.ofPattern("MM/yy")
        return try {
            val lastValidMonth = YearMonth.parse(ccExpDate, monthFormatter)
            if (YearMonth.now(ZoneId.systemDefault()).isAfter(lastValidMonth)) {
                context?.let { showToast(it, "Credit card has expired") }
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

    private fun setResult(value: PaymentMethod) {
        setNavigationResult(RESULT_KEY, value)
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