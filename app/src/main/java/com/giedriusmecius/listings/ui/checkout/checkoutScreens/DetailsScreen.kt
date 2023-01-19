package com.giedriusmecius.listings.ui.checkout.checkoutScreens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.giedriusmecius.listings.data.local.PaymentMethod
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.data.remote.model.product.InCartProduct
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.ui.common.composeStyles.DisabledBackgroundColor
import com.giedriusmecius.listings.ui.common.composeStyles.H2
import com.giedriusmecius.listings.ui.common.composeStyles.H3
import com.giedriusmecius.listings.ui.common.composeStyles.H3BOLD
import com.giedriusmecius.listings.ui.common.composeStyles.H5
import com.giedriusmecius.listings.ui.common.composeStyles.H5Grey
import com.giedriusmecius.listings.ui.common.composeStyles.ListingsPurple
import com.giedriusmecius.listings.ui.common.composeStyles.NormalTextColor
import com.giedriusmecius.listings.ui.common.composeStyles.WarmPurple
import com.giedriusmecius.listings.ui.views.ListingsButtonComposable
import com.giedriusmecius.listings.utils.compose.DashedDivider
import com.giedriusmecius.listings.utils.extensions.calculateTotalPrice
import com.giedriusmecius.listings.utils.extensions.toCurrency

@Composable
fun DetailsScreen(
    modifier: Modifier,
    cartItems: List<Product>,
    paymentMethod: PaymentMethod,
    address: UserAddress
) {
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = modifier.fillMaxSize().verticalScroll(scrollState, true).padding(bottom = 24.dp)
    ) {
        val (itemDetails, priceList, orderInfo, policies) = createRefs()

        CartDetails(cartItems, modifier.constrainAs(itemDetails) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            height = Dimension.wrapContent
            width = Dimension.fillToConstraints
        })
        ItemPriceList(cartItems, modifier.constrainAs(priceList) {
            top.linkTo(itemDetails.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            height = Dimension.wrapContent
            width = Dimension.fillToConstraints
        })
        OrderInfoContainer(modifier.constrainAs(orderInfo) {
            top.linkTo(priceList.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            height = Dimension.wrapContent
            width = Dimension.fillToConstraints
        }, paymentMethod, address)
        PoliciesContainer(modifier.constrainAs(policies) {
            top.linkTo(orderInfo.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            height = Dimension.wrapContent
            width = Dimension.fillToConstraints
        })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CartDetails(cartItems: List<Product>, modifier: Modifier) {
    val listState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(listState)

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        LazyRow(
            state = listState,
            modifier = Modifier.wrapContentSize().height(256.dp),
            flingBehavior = snapBehavior
        ) {
            itemsIndexed(cartItems) { index, item ->
                CartDetailsItem(item)
            }
        }
        ScrollIndicator(listState, cartItems.size)
        Spacer(modifier.padding(top = 12.dp))
        Divider(color = DisabledBackgroundColor, thickness = 1.dp)
    }
}

@Composable
fun ScrollIndicator(listState: LazyListState, size: Int) {
    var selectItemIndex = 1

    when {
        listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset <= LocalConfiguration.current.screenWidthDp * 80 / 100 -> selectItemIndex =
            0
        selectItemIndex != 1 -> selectItemIndex = listState.firstVisibleItemIndex
        selectItemIndex == 1 && listState.firstVisibleItemScrollOffset <= LocalConfiguration.current.screenWidthDp * 80 / 100 -> selectItemIndex =
            listState.firstVisibleItemIndex
        else -> selectItemIndex = listState.firstVisibleItemIndex + 1
    }

    LazyRow(Modifier.padding(vertical = 8.dp)) {
        items(size) { circle ->
            val color = if (selectItemIndex == circle) ListingsPurple else WarmPurple
            Canvas(
                modifier = Modifier.size(16.dp).padding(horizontal = 4.dp),
                onDraw = {
                    drawCircle(color = color)
                }
            )
        }
    }
}

@Composable
fun CartDetailsItem(item: Product) {

    val screenWidth = LocalConfiguration.current.screenWidthDp * 80 / 100
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentHeight().width(screenWidth.dp)
    ) {
        Image(
            rememberAsyncImagePainter(item.image),
            contentDescription = null,
            modifier = Modifier.padding(vertical = 24.dp).size(height = 126.dp, width = 104.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Text(
            text = item.title,
            style = H2,
            overflow = TextOverflow.Clip,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 4.dp).width(200.dp)
        )
        Text(
            text = "26 - S | Blue | ID:0706502",
            style = H5Grey,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp).width(200.dp)
        )
    }
}

@Composable
fun ItemPriceList(cartItems: List<Product>, modifier: Modifier) {
    val totalPrice =
        calculateTotalPrice(cartItems) // cia jauciu perskaiciuos kiekviena karta ant perpiesimo
    val taxes = totalPrice * 0.21
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.padding(top = 24.dp)
    ) {
        for (item in cartItems) {
            TextRowItem(item.title, item.price.toCurrency())
        }
        TextRowItem("Taxes (included)", taxes.toFloat().toCurrency())
        TextRowItem("Shipping", "$5", ListingsPurple)
        DashedDivider(
            modifier = Modifier.padding(horizontal = 24.dp).padding(vertical = 12.dp),
            color = DisabledBackgroundColor,
            thickness = 3
        )
        Row(
            modifier = Modifier.padding(horizontal = 24.dp).padding(top = 8.dp, bottom = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                style = H3,
                color = NormalTextColor,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(200.dp),
                maxLines = 1
            )
            Text(text = totalPrice.plus(5F).toCurrency(), style = H3BOLD, color = NormalTextColor)
        }
        ListingsButtonComposable(
            modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(top = 24.dp),
            null,
            "Confirm and pay",
            null,
            true
        ) {

        }
        Divider(
            color = DisabledBackgroundColor,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}

@Composable
fun TextRowItem(title: String, price: String, textColor: Color = NormalTextColor) {
    Row(
        modifier = Modifier.padding(horizontal = 24.dp).padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = H5,
            color = textColor,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(200.dp),
            maxLines = 1
        )
        Text(text = price, style = H5, color = textColor)
    }
}

@Composable
fun OrderInfoContainer(modifier: Modifier, paymentMethod: PaymentMethod, address: UserAddress) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "Order info", style = H2, modifier = modifier.padding(top = 32.dp))

        OrderInfoTextItem(
            modifier = Modifier.padding(top = 20.dp),
            "Order address",
            "${address.addressStreetName} ${address.addressHouseNumber}, ${address.city}, ${address.county}",
            address.secondAddressLine
        )

        OrderInfoTextItem(
            modifier = Modifier,
            "Receives",
            "${address.firstName} ${address.lastName}"
        )

        OrderInfoTextItem(
            modifier = Modifier,
            "Payment method",
            "${paymentMethod.type} ending in ${paymentMethod.number}"
        )

        Divider(
            color = DisabledBackgroundColor,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 32.dp)
        )
    }
}

@Composable
fun OrderInfoTextItem(
    modifier: Modifier,
    title: String,
    firstLine: String,
    secondLine: String? = null
) {
    Column(modifier = modifier.fillMaxSize().padding(vertical = 8.dp)) {
        Text(text = title, style = H5Grey)
        Text(text = firstLine, style = H5)
        if (secondLine?.isNotEmpty() == true) {
            Text(text = secondLine, style = H5)
        }
    }
}

@Composable
fun PoliciesContainer(modifier: Modifier) {

}
