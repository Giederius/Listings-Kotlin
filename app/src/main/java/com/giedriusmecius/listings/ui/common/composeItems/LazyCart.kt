package com.giedriusmecius.listings.ui.common.composeItems

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.giedriusmecius.listings.data.remote.model.product.InCartProduct
import com.giedriusmecius.listings.data.remote.model.product.Product

@Composable
fun LazyCart(
    data: List<InCartProduct>,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onDelete: (InCartProduct) -> Unit,
    onEdit: (InCartProduct) -> Unit,
    onSave: (InCartProduct) -> Unit,
) {
    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        items(data) {
            cartItem(
                title = it.title,
                productPrice = it.price,
                productImg = it.image,
                size = "S-26",
                color = "RED",
                quantity = 1,
                onDelete = { onDelete(it) },
                onEdit = { onEdit(it) },
                onSave = { onSave(it) })
        }
    }
}