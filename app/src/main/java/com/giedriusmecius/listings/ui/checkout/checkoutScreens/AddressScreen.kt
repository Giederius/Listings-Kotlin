package com.giedriusmecius.listings.ui.checkout.checkoutScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.ui.common.composeStyles.DividerPurple
import com.giedriusmecius.listings.ui.common.composeStyles.GreyTextColor
import com.giedriusmecius.listings.ui.common.composeStyles.H2
import com.giedriusmecius.listings.ui.common.composeStyles.H3
import com.giedriusmecius.listings.ui.common.composeStyles.H3BOLD
import com.giedriusmecius.listings.ui.common.composeStyles.LightPurple
import com.giedriusmecius.listings.ui.common.composeStyles.ListingsClickPurple
import com.giedriusmecius.listings.ui.common.composeStyles.ListingsPurple
import com.giedriusmecius.listings.ui.views.ListingsOutlinedButton

@Composable
fun AddressScreen(
    modifier: Modifier,
    addresses: List<UserAddress>,
    selectedAddress: UserAddress,
    onAddressChange: (UserAddress) -> Unit,
    onEditClick: (UserAddress) -> Unit,
    onAddNewAddress: (UserAddress) -> Unit
) {
    val addressScrollState = rememberScrollState()
    var selectedAddr by remember {
        if (selectedAddress.addressLabel.isNotEmpty()) {
            mutableStateOf(selectedAddress)
        } else {
            mutableStateOf(addresses.first())
        }
    }

    val listState = rememberLazyListState()
    val emptyAddress = UserAddress(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
    )

    // pasicheckint kaip tokioj vietoj reiketu elgtis. nes cia gal kokio effect reikia?
    onAddressChange(selectedAddr)
    Log.d("MANOaddressScreen", "${selectedAddr.addressLabel} ${addresses.first().addressLabel}")

    ConstraintLayout(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize()
            .scrollable(addressScrollState, Orientation.Vertical)
    ) {
        val (lazyAddress) = createRefs()
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.constrainAs(lazyAddress) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }.padding(bottom = 100.dp)
        ) {
            items(addresses) {
                when (it) {
                    addresses.first() -> {
                        Text(
                            text = "Select or add a shipping address",
                            style = H2,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 24.dp)
                        )
                        AddressItem(
                            address = it,
                            estimateDeliveryDate = "15th next week",
                            shippingPrice = 5.0,
                            isSelected = it == selectedAddr,
                            onSelect = {
                                selectedAddr = it
                                onAddressChange(it)
                            },
                            onEditClick = {
                                onEditClick(it)
                            }
                        )
                    }
                    addresses.last() -> {
                        AddressItem(
                            address = it,
                            estimateDeliveryDate = "15th next week",
                            shippingPrice = 5.0,
                            isSelected = it == selectedAddr,
                            onSelect = {
                                selectedAddr = it
                                onAddressChange(it)
                            },
                            onEditClick = { onEditClick(it) }
                        )
                        ListingsOutlinedButton(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                                .fillMaxWidth(), null, "Add Address", null, true
                        ) { onAddNewAddress(emptyAddress) }
                    }
                    else -> {
                        AddressItem(
                            address = it,
                            estimateDeliveryDate = "15th next week",
                            shippingPrice = 5.0,
                            isSelected = it == selectedAddr,
                            onSelect = {
                                selectedAddr = it
                                onAddressChange(it)
                            },
                            onEditClick = { onEditClick(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddressItem(
    address: UserAddress,
    estimateDeliveryDate: String,
    shippingPrice: Double,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onEditClick: () -> Unit
) {
    val bgColor = if (isSelected) LightPurple else Color.White
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val btnColor = if (pressed) ListingsClickPurple else LightPurple

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                bgColor,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        val (container, divider, button) = createRefs()
        ConstraintLayout(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .constrainAs(container) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            val (label, address1, address2, radioBtn, estimate, price) = createRefs()
            Text(text = address.addressLabel, style = H3BOLD, modifier = Modifier
                .padding(top = 16.dp)
                .constrainAs(label) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                })

            Text(text = "${address.addressHouseNumber} ${address.addressStreetName}, ${address.county}",
                style = H3,
                color = GreyTextColor,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .constrainAs(address1) {
                        top.linkTo(label.bottom)
                        start.linkTo(parent.start)
                    })

            Text(text = "${address.state}, zip code ${address.zipCode}",
                style = H3,
                color = GreyTextColor,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .constrainAs(address2) {
                        top.linkTo(address1.bottom)
                        start.linkTo(parent.start)
                    })

            Text(text = estimateDeliveryDate,
                style = H3,
                color = ListingsPurple,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .constrainAs(estimate) {
                        top.linkTo(address2.bottom)
                        start.linkTo(parent.start)
                    })

            Text(text = "$shippingPrice shipping",
                style = H3,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .constrainAs(price) {
                        top.linkTo(estimate.top)
                        start.linkTo(estimate.end)
                        bottom.linkTo(estimate.bottom)
                    })

            IconToggleButton(
                checked = isSelected,
                onCheckedChange = { onSelect() },
                modifier = Modifier.constrainAs(radioBtn) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isSelected) {
                            R.drawable.ic_radio_button_selected
                        } else {
                            R.drawable.ic_radio_button
                        }
                    ), tint = ListingsPurple, contentDescription = null
                )
            }
        }
        if (isSelected) {
            Divider(
                color = DividerPurple, thickness = 1.dp, modifier = Modifier
                    .constrainAs(divider) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(container.bottom)
                    }
                    .padding(top = 16.dp)
            )

            Box(
                modifier = Modifier
                    .height(48.dp)
                    .constrainAs(button) {
                        width = Dimension.fillToConstraints
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(divider.bottom)
                    }
                    .indication(interactionSource, indication = null)
                    .background(
                        color = btnColor,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp
                        )
                    )
                    .clickable(interactionSource = interactionSource, null, true, onClick = {
                        onEditClick()
                    }),
                Alignment.Center
            ) {
                Text(text = "Edit")
            }

//            Button(
//                onClick = { onEditClick() }, modifier = Modifier
//                    .constrainAs(button){
//
//                    }
//                    .background(
//                        color = Color.Transparent,
//                        shape = RoundedCornerShape(
//                            topStart = 0.dp,
//                            topEnd = 0.dp,
//                            bottomStart = 12.dp,
//                            bottomEnd = 12.dp
//                        )
//                    )
//            ) {
//                Text(text = "Edit")
//            }
        }
    }
}


@Preview
@Composable
fun PreviewAddressItem() {
    Scaffold {
        AddressItem(
            address = UserAddress(
                "adas",
                "asdas",
                "asad",
                "awwwww",
                "wwwwwww",
                "asdasd",
                "123",
                "asww",
                "wwww",
                "wwearar",
                "wwwwea",
                "2222"
            ),
            estimateDeliveryDate = "Arrival est: Apr 15",
            shippingPrice = 5.0,
            isSelected = true,
            onSelect = {},
            onEditClick = {}
        )
    }
}

//@Preview
//@Composable
//fun AddressScreenPreview() {
//    Scaffold() {
//        AddressScreen(
//            modifier = Modifier,
//            emptyList(),
//            onAddressChange = {},
//            onEditClick = {},
//            onAddNewAddress = {})
//    }
//}