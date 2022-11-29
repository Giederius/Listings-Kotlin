package com.giedriusmecius.listings.ui.views

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.ui.common.composeStyles.DisabledContentColor
import com.giedriusmecius.listings.ui.common.composeStyles.H5Black


@Composable
fun ListingsSquareButton(
    modifier: Modifier,
    @DrawableRes icon: Int? = null,
    letter: String? = null,
    color: Color? = null,
    isEnabled: Boolean
) {

    val iconColor = if (isEnabled) Color.White else DisabledContentColor

    Button(
        shape = RoundedCornerShape(12.dp),
        colors = ListingButtonDefaults(),
        modifier = modifier.size(48.dp, 48.dp),
        enabled = isEnabled,
        onClick = {}) {

        when {
            icon != null -> {
                Image(
                    painterResource(icon),
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(iconColor),
                    contentDescription = null
                )
            }
            letter != null -> {
                Text(text = letter, style = H5Black, color = iconColor)
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


@Composable
@Preview
fun ListingsButtonSquarePreview() {
    Surface {
        ListingsSquareButton(Modifier, isEnabled = true)
    }
}