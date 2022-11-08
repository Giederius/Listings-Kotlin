package com.giedriusmecius.listings.ui.checkout.checkoutScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.ui.common.composeStyles.GreyTextColor
import com.giedriusmecius.listings.ui.common.composeStyles.H3
import com.giedriusmecius.listings.ui.common.composeStyles.H3BOLD
import com.giedriusmecius.listings.ui.common.composeStyles.H5
import com.giedriusmecius.listings.ui.common.composeStyles.ListingsPurple
import com.giedriusmecius.listings.ui.common.composeStyles.WarmPurple

@Composable
fun AddressScreen(modifier: Modifier, addresses: List<UserAddress>) {
    val addressScrollState = rememberScrollState()
    val selectedAddress = remember { mutableStateOf(addresses.first()) }
    val listState = rememberLazyListState()
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .scrollable(addressScrollState, Orientation.Vertical)
    ) {
        val (desc, lazyAddress, addAddressBtn) = createRefs()
        Text(
            text = "Select or add a shipping address",
            style = H5,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 24.dp)
                .constrainAs(desc) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        LazyColumn(state = listState, modifier = Modifier.constrainAs(lazyAddress) {

        }) {
            items(addresses) {
                if (it != addresses.last()) {
                    AddressItem(
                        address = it,
                        estimateDeliveryDate = "15th next week",
                        shippingPrice = 5.0,
                        isSelected = it == selectedAddress.value,
                        onSelect = { selectedAddress.value = it },
                        onEditClick = { Log.d("MANO", "onEDITCLICK") }
                    )
                } else {
                    AddressItem(
                        address = it,
                        estimateDeliveryDate = "15th next week",
                        shippingPrice = 5.0,
                        isSelected = it == selectedAddress.value,
                        onSelect = { selectedAddress.value = it },
                        onEditClick = { Log.d("MANO", "onEDITCLICK") }
                    )
                    Button(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        onClick = { Log.d("GIEDRIUS", "add address") }) {
                        Text("Add Address", style = H3BOLD, color = Color.White)
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
    val bgColor = if (isSelected) WarmPurple else Color.White

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
                .padding(top = 32.dp)
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
                color = ListingsPurple, thickness = 1.dp, modifier = Modifier
                    .constrainAs(divider) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(container.bottom)
                    }
                    .padding(top = 32.dp, bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .constrainAs(button) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(divider.bottom)
                    }
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp
                        )
                    )
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

@Preview
@Composable
fun AddressScreenPreview() {
    Scaffold() {
        AddressScreen(modifier = Modifier, emptyList())
    }
}