package com.giedriusmecius.listings.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun ButtonTestingGroundsScreen(modifier: Modifier) {
    ConstraintLayout(modifier.fillMaxSize()) {
        val (firstBtn, secondBtn) = createRefs()

        ListingsOutlinedButton(Modifier.padding(top = 10.dp, end = 10.dp).fillMaxWidth()
            .constrainAs(firstBtn) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(secondBtn.start)
                width = Dimension.fillToConstraints
            })

        ListingsOutlinedButton(Modifier.padding(top = 10.dp, start = 10.dp).fillMaxWidth()
            .constrainAs(secondBtn) {
                top.linkTo(parent.top)
                start.linkTo(firstBtn.end)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }, false
        )
    }
}