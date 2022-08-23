package com.giedriusmecius.listings.ui.common.composeItems

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.ui.common.composeStyles.H5
import com.giedriusmecius.listings.ui.common.composeStyles.H5Grey

@Composable
fun storeItemFullWidth(storeTitle: String, storeDesc: String) {
    ConstraintLayout(
        modifier = Modifier
            .padding(4.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        val (bgImage, storeImg, name, descr) = createRefs()
        Image(
            painterResource(id = R.drawable.sample_image),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(bgImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.matchParent
                }
                .height(120.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )
        Image(
            painterResource(id = R.drawable.ic_delete),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(storeImg) {
                    start.linkTo(bgImage.start)
                    bottom.linkTo(bgImage.bottom)
                    width = Dimension.wrapContent
                }
                .clip(CircleShape)
                .background(Color.Red)
                .height(40.dp)
                .width(40.dp)
        )
        Text(text = storeTitle, style = H5, modifier = Modifier.constrainAs(name) {
            top.linkTo(bgImage.bottom)
            start.linkTo(bgImage.start)
        })
        Text(text = storeDesc, style = H5Grey, modifier = Modifier.constrainAs(descr) {
            top.linkTo(name.bottom)
            start.linkTo(name.start)
        })
    }
}

@Preview
@Composable
fun storeItemFullWidthPrew() {
    Surface() {
        storeItemFullWidth(storeTitle = "Adidas", storeDesc = "things")
    }
}