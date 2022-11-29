package com.giedriusmecius.listings.ui.checkout.checkoutScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.local.CardType
import com.giedriusmecius.listings.data.local.PaymentMethod
import com.giedriusmecius.listings.ui.common.composeStyles.H2
import com.giedriusmecius.listings.ui.common.composeStyles.H3BOLD
import com.giedriusmecius.listings.ui.common.composeStyles.H5Black
import com.giedriusmecius.listings.ui.common.composeStyles.H5Grey
import com.giedriusmecius.listings.ui.common.composeStyles.ListingsButtonOutlineColor
import com.giedriusmecius.listings.ui.common.composeStyles.ListingsPurple
import com.giedriusmecius.listings.ui.common.composeStyles.WarmPurple
import com.giedriusmecius.listings.ui.views.ListingsOutlinedButton

@Composable
fun PaymentScreen(
    modifier: Modifier,
    paymentMethodList: List<PaymentMethod>,
    selectedCard: PaymentMethod,
    onPaymentMethodChange: (PaymentMethod) -> Unit
) {

    val listState = rememberLazyListState()
    val scrollState = rememberScrollState()

    ConstraintLayout(
        modifier = modifier.fillMaxSize().verticalScroll(scrollState, true)
            .padding(horizontal = 24.dp).padding(bottom = 120.dp)
    ) {
        val (title, itemList, addCardBtn, couponContainer, otherPaymentsContainer) = createRefs()
        Text(text = "Select or add a payment method", style = H2,
            modifier = modifier.constrainAs(title) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }
                .padding(top = 16.dp, bottom = 24.dp))

        LazyRow(
            state = listState, modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .constrainAs(itemList) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.matchParent
                    height = Dimension.wrapContent
                }
                .layout() { measurable, constraints ->
                    val placeable =
                        measurable.measure(constraints.copy(maxWidth = constraints.maxWidth + 48.dp.roundToPx()))
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            this.items(paymentMethodList) { method ->
                PaymentMethodCard(
                    method,
                    selectedCard == method,
                    paymentMethodList.first() == method
                ) {
                    onPaymentMethodChange(method)
                }
            }
        }

        ListingsOutlinedButton(
            modifier = modifier
                .padding(top = 24.dp)
                .constrainAs(addCardBtn) {
                    top.linkTo(itemList.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }, null, "Add card", null, true
        ) {

        }

        AddCouponCodeContainer(modifier
            .padding(top = 24.dp)
            .constrainAs(couponContainer) {
                top.linkTo(addCardBtn.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }.layout { measurable, constraints ->
                val placeable =
                    measurable.measure(constraints.copy(maxWidth = constraints.maxWidth + 48.dp.roundToPx()))
                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            }) { }

        OtherPaymentsContainer(modifier.padding(top = 24.dp).constrainAs(otherPaymentsContainer) {
            top.linkTo(couponContainer.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        })

    }
}

@Composable
fun PaymentMethodCard(
    method: PaymentMethod,
    isSelected: Boolean = false,
    isFirst: Boolean,
    onClick: () -> Unit
) {

    var ccType: Pair<Int, String> = Pair(0, "")
    when (method.type) {
        CardType.VISA -> {
            ccType = Pair(R.drawable.icon_visa, "Visa")
        }
        CardType.MASTERCARD -> {
            ccType = Pair(R.drawable.icon_mastercard, "Visa")
        }
        else -> {}
    }

    val itemPadding = if (isFirst) 24.dp else 0.dp
    val selected = if (isSelected) {
        Pair(BorderStroke(2.dp, ListingsPurple), 5.dp)
    } else {
        Pair(
            BorderStroke(
                0.dp,
                Color.Transparent
            ), 0.dp
        )
    }

    Card(
        modifier = Modifier.padding(start = itemPadding).size(width = 135.dp, height = 215.dp)
            .clickable() {
                onClick()
            },
        shape = RoundedCornerShape(12.dp),
        backgroundColor = WarmPurple,
        border = selected.first,
        elevation = selected.second
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {
            val (icon, cardType, cardNumb) = createRefs()
            Image(
                painter = painterResource(ccType.first),
                modifier = Modifier
                    .size(width = 48.dp, height = 32.dp)
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    },
                contentDescription = null
            )

            Text(
                text = ccType.second,
                style = H5Grey,
                modifier = Modifier.constrainAs(cardType) {
                    start.linkTo(parent.start)
                    bottom.linkTo(cardNumb.top)
                    width = Dimension.wrapContent
                    height = Dimension.fillToConstraints
                }.padding(bottom = 4.dp)
            )
            Text(text = "****5431", style = H3BOLD, modifier = Modifier.constrainAs(cardNumb) {
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                width = Dimension.wrapContent
                height = Dimension.fillToConstraints
            })
        }
    }
}

@Composable
fun AddCouponCodeContainer(modifier: Modifier, onClick: () -> Unit) {
    Column(modifier.fillMaxSize()) {
        Divider(color = ListingsButtonOutlineColor, thickness = 1.dp)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 24.dp)
                .clickable { onClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Enter discount coupon", style = H5Black)
            Image(painter = painterResource(R.drawable.ic_coupon), contentDescription = null)
        }
        Divider(color = ListingsButtonOutlineColor, thickness = 1.dp)

    }
}

@Composable
fun OtherPaymentsContainer(modifier: Modifier) {
    Column(
        modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(text = "Other payment methods", style = H2)

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Paypal", style = H5Black)
            Image(
                painter = painterResource(R.drawable.ic_paypal),
                contentDescription = null,
                Modifier.size(24.dp)
            )
        }

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Cash", style = H5Black)
            Image(
                painter = painterResource(R.drawable.ic_cash),
                contentDescription = null,
                Modifier.size(24.dp)
            )
        }
    }
}

