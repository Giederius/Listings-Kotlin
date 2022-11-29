package com.giedriusmecius.listings.ui.common.composeItems

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.ui.common.composeStyles.H3
import com.giedriusmecius.listings.ui.common.composeStyles.H5
import com.giedriusmecius.listings.ui.common.composeStyles.H5Black
import com.giedriusmecius.listings.ui.views.ListingsButtonComposable
import com.giedriusmecius.listings.ui.views.ListingsOutlinedButton
import com.giedriusmecius.listings.utils.extensions.toCurrency

@Composable
fun cartItem(
    title: String,
    productPrice: Float,
    productImg: String,
    size: String,
    color: String,
    quantity: Int,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onSave: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val (deleteIcon, Img, productTitle, details, price, btnRow) = createRefs()

            Icon(
                painterResource(id = R.drawable.ic_trash_can),
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp)
                    .clickable { onDelete() }
                    .constrainAs(deleteIcon) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )

            Image(
                painter = rememberAsyncImagePainter(productImg),
                contentDescription = null,
                modifier = Modifier
                    .height(126.dp)
                    .width(104.dp)
                    .padding(top = 24.dp, start = 24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .constrainAs(Img) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                contentScale = ContentScale.Crop
            )

            Text(
                text = title,
                style = H3,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .width(200.dp)
                    .constrainAs(productTitle) {
                        top.linkTo(Img.top)
                        bottom.linkTo(Img.bottom)
                        start.linkTo(Img.end)
                        end.linkTo(parent.end)
                    },
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Text(text = "$size | $color | X$quantity",
                style = H5,
                modifier = Modifier
                    .padding(top = 16.dp, start = 24.dp)
                    .constrainAs(details) {
                        start.linkTo(parent.start)
                        top.linkTo(Img.bottom)
                    })

            Text(text = productPrice.toCurrency(), style = H5Black, modifier = Modifier
                .padding(end = 24.dp)
                .constrainAs(price) {
                    bottom.linkTo(details.bottom)
                    end.linkTo(parent.end)
                })

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .constrainAs(btnRow) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(details.bottom)
                    }) {
                ListingsOutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 24.dp, end = 4.dp), null,"Edit",null,true
                ) { onEdit() }
                ListingsButtonComposable(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp, end = 24.dp), null, "Save for later", null, true
                ) {
                    onSave()
                }
            }
        }
    }
}

@Composable
@Preview
fun cartItemPreview() {
//    cartItem()
}