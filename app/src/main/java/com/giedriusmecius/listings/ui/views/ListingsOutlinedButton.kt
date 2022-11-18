package com.giedriusmecius.listings.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.giedriusmecius.listings.ui.common.composeStyles.DarkButtonTextColor
import com.giedriusmecius.listings.ui.common.composeStyles.DisabledContentColor
import com.giedriusmecius.listings.ui.common.composeStyles.H5Black
import com.giedriusmecius.listings.ui.common.composeStyles.ListingsButtonOutlineColor

@Composable
fun ListingsOutlinedButton(modifier: Modifier, isEnabled: Boolean = true) {

    val interactionSource = remember { MutableInteractionSource() }
    var textColor = if(isEnabled) DarkButtonTextColor else DisabledContentColor

    TextButton(
        shape = RoundedCornerShape(12.dp),
        enabled = isEnabled,
        colors = ListingOutlinedButtonDefaults(),
        border = BorderStroke(1.dp, ListingsButtonOutlineColor),
        elevation = ButtonDefaults.elevation(0.dp, 1.dp, 0.dp),
        modifier = modifier.height(48.dp).background(Color.White),
        onClick = {}) {
        Text(text = "Button", style = H5Black, color = textColor)
    }
}

@Composable
@Preview
fun listingsOutlinePrew() {
    Surface {
        ListingsOutlinedButton(Modifier)
    }
}