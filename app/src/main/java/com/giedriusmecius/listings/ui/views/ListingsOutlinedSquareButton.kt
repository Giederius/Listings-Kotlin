package com.giedriusmecius.listings.ui.views

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.ui.common.composeStyles.DarkButtonTextColor
import com.giedriusmecius.listings.ui.common.composeStyles.DisabledContentColor
import com.giedriusmecius.listings.ui.common.composeStyles.H5Black
import com.giedriusmecius.listings.ui.common.composeStyles.ListingsButtonOutlineColor
import com.giedriusmecius.listings.ui.common.composeStyles.WarmPurple


@Composable
fun ListingsOutlinedButton(
    modifier: Modifier,
    @DrawableRes icon: Int? = null,
    text: String? = null,
    color: Color? = null,
    isEnabled: Boolean,
    onBtnClick: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    var iconColor = if (isEnabled) DarkButtonTextColor else DisabledContentColor
    var mod = if (text != null && text.length > 1) modifier.height(48.dp) else modifier.size(48.dp)

    OutlinedButton(
        shape = RoundedCornerShape(12.dp),
        modifier = mod.indication(
            interactionSource = interactionSource,
            indication = rememberRipple(color = WarmPurple, radius = 10.dp)
        ),
        colors = ListingOutlinedButtonDefaults(),
        border = BorderStroke(1.dp, ListingsButtonOutlineColor),
        elevation = ButtonDefaults.elevation(0.dp, 1.dp, 0.dp),
        enabled = isEnabled,
        interactionSource = interactionSource,
        onClick = { onBtnClick() }) {
        when {
            icon != null -> {
                Image(
                    painterResource(icon),
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(iconColor),
                    contentDescription = null
                )
            }
            text != null -> {
                Text(text = text, style = H5Black, color = iconColor)
            }
            color != null -> {
                Canvas(modifier = Modifier.size(20.dp), onDraw = {
                    drawCircle(color = color)
                })
            }
            else -> {
                Image(
                    painterResource(R.drawable.ic_radio_button),
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(iconColor),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
fun ListingsOutlinedSquareBtnPrew() {
    Surface {
        ListingsOutlinedButton(Modifier, null, null, null, false) {}
    }
}