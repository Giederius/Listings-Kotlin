package com.giedriusmecius.listings.ui.views

import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.giedriusmecius.listings.ui.common.composeStyles.DarkButtonTextColor
import com.giedriusmecius.listings.ui.common.composeStyles.DisabledBackgroundColor
import com.giedriusmecius.listings.ui.common.composeStyles.DisabledContentColor
import com.giedriusmecius.listings.ui.common.composeStyles.ListingsClickPurple
import com.giedriusmecius.listings.ui.common.composeStyles.ListingsPurple

@Composable
fun ListingOutlinedButtonDefaults(): ButtonColors {
    return ButtonDefaults.buttonColors(
        backgroundColor = Color.White,
        contentColor = DarkButtonTextColor,
        disabledBackgroundColor = DisabledBackgroundColor,
        disabledContentColor = DisabledContentColor,
    )
}

@Composable
fun ListingButtonDefaults(): ButtonColors {
    return ButtonDefaults.buttonColors(
        backgroundColor = ListingsPurple,
        contentColor = ListingsClickPurple,
        disabledBackgroundColor = DisabledBackgroundColor,
        disabledContentColor = DisabledContentColor,
    )
}

