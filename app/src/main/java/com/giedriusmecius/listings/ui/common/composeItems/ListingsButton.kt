package com.giedriusmecius.listings.ui.common.composeItems

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.giedriusmecius.listings.ui.common.composeStyles.GreyBackgroundColor
import com.giedriusmecius.listings.ui.common.composeStyles.H5Black
import com.giedriusmecius.listings.ui.common.composeStyles.WarmPurple

@Composable
fun ListingsButton(
    text: String,
    modifier: Modifier,
    backgroundColor: Color = Color.Transparent,
    bgOnClickColor: Color = WarmPurple,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val bgColor = if (pressed) bgOnClickColor else backgroundColor
    val newBorder = if (backgroundColor == Color.Transparent) {
        BorderStroke(1.dp, GreyBackgroundColor)
    } else {
        BorderStroke(0.dp, Color.Transparent)
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(interactionSource = interactionSource, indication = null) {
                onClick()
            }
            .background(shape = RoundedCornerShape(12.dp), color = bgColor).border(newBorder),
    ) {
        val title = createRef()
        Text(text = text,
            textAlign = TextAlign.Center,
            style = H5Black,
            modifier = Modifier.padding(vertical = 11.dp).constrainAs(
                title
            ) {
                width = Dimension.fillToConstraints
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
    }

//    Box(
//        modifier = modifier.clip(RoundedCornerShape(12.dp))
//    ) {
//        Button(
//            shape = RoundedCornerShape(12.dp),
//            colors = ButtonDefaults.buttonColors(backgroundColor = bgColor),
//            onClick = { onClick() },
//            modifier = Modifier
//                .fillMaxWidth()
//                .indication(
//                    interactionSource = interactionSource,
//                    indication = rememberRipple(color = bgOnClickColor)
//                ),
//            interactionSource = interactionSource,
//            enabled = isEnabled
//        ) {
//            Text(text = text, style = H5Black, modifier = Modifier.padding(vertical = 11.dp))
//        }
//        OutlinedButton(
//            onClick = { onClick() },
//            colors = ButtonDefaults.buttonColors(backgroundColor = bgColor),
//            modifier = Modifier
//                .fillMaxWidth()
//                .indication(
//                    interactionSource = interactionSource,
//                    null
////                    indication = rememberRipple(color = bgOnClickColor)
//                ),
////            interactionSource = interactionSource,
//            border = newBorder,
//            shape = RoundedCornerShape(12.dp),
//            enabled = isEnabled
//        ) {
//            Text(text = text, style = H5Black, modifier = Modifier.padding(vertical = 11.dp))
//        }
    }
//}

@Preview
@Composable
fun ListingsPreview() {
    Scaffold { ListingsButton("Add btn", Modifier) {} }
}