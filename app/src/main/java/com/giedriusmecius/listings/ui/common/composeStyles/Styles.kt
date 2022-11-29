package com.giedriusmecius.listings.ui.common.composeStyles

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.giedriusmecius.listings.R

val H1 = TextStyle(
    color = NormalTextColor,
    fontSize = 32.sp,
    fontFamily = Raleway,
    fontWeight = FontWeight.Black
)
val H2 = TextStyle(
    color = NormalTextColor,
    fontSize = 20.sp,
    fontFamily = FontFamily(Font(R.font.raleway_regular)),
    fontWeight = FontWeight.Medium
)
val H3 = TextStyle(
    color = NormalTextColor,
    fontSize = 18.sp,
    fontFamily = FontFamily(Font(R.font.raleway_regular)),
    fontWeight = FontWeight.Medium
)
val H3SEMIBOLD = TextStyle(
    color = NormalTextColor,
    fontSize = 18.sp,
    fontFamily = Raleway,
    fontWeight = FontWeight.SemiBold
)
val H3BOLD = TextStyle(
    color = NormalTextColor,
    fontSize = 18.sp,
    fontFamily = Raleway,
    fontWeight = FontWeight.Bold
)
val H5Black = TextStyle(
    color = NormalTextColor,
    fontSize = 16.sp,
    fontFamily = Raleway,
    fontWeight = FontWeight.Black
)
val H5 = TextStyle(
    color = NormalTextColor,
    fontSize = 16.sp,
    fontFamily = FontFamily(Font(R.font.raleway_regular)),
    fontWeight = FontWeight.Medium
)

val H5White = TextStyle(
    color = Color.White,
    fontSize = 16.sp,
    fontFamily = FontFamily(Font(R.font.raleway_medium)),
)

val H5Grey = TextStyle(
    color = GreyedOutTextColor,
    fontSize = 16.sp,
    fontFamily = Raleway,
    fontWeight = FontWeight.Normal
)