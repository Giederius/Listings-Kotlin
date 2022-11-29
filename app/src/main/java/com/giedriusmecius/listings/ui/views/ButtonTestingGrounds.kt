package com.giedriusmecius.listings.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.giedriusmecius.listings.R

@Composable
fun ButtonTestingGroundsScreen(modifier: Modifier) {
    ConstraintLayout(modifier.fillMaxSize()) {
        val (firstBtn, secondBtn, thirdBtn, fourthBtn,fifthBtn, sixthBtn, seventhBtn, eightBtn, ninthBtn, tenthBtn) = createRefs()

//        ListingsOutlinedButton(Modifier.padding(top = 10.dp, end = 10.dp).fillMaxWidth()
//            .constrainAs(firstBtn) {
//                top.linkTo(parent.top)
//                start.linkTo(parent.start)
//                end.linkTo(secondBtn.start)
//                width = Dimension.fillToConstraints
//            })
//
//        ListingsOutlinedButton(
//            Modifier.padding(top = 10.dp, start = 10.dp).fillMaxWidth()
//                .constrainAs(secondBtn) {
//                    top.linkTo(parent.top)
//                    start.linkTo(firstBtn.end)
//                    end.linkTo(parent.end)
//                    width = Dimension.fillToConstraints
//                }, false
//        )
//
//        ListingsButtonComposable(Modifier.padding(top = 10.dp, end = 10.dp).fillMaxWidth()
//            .constrainAs(thirdBtn) {
//                top.linkTo(firstBtn.bottom)
//                start.linkTo(parent.start)
//                end.linkTo(secondBtn.start)
//                width = Dimension.fillToConstraints
//            })
//
//        ListingsButtonComposable(Modifier.padding(top = 10.dp, start = 10.dp).fillMaxWidth()
//            .constrainAs(fourthBtn) {
//                top.linkTo(firstBtn.bottom)
//                start.linkTo(thirdBtn.end)
//                end.linkTo(parent.end)
//                width = Dimension.fillToConstraints
//            }, false)
//
//        ListingsSquareButton(Modifier.padding(top = 10.dp, start = 10.dp)
//            .constrainAs(fifthBtn) {
//                top.linkTo(thirdBtn.bottom)
//                start.linkTo(parent.start)
//                end.linkTo(sixthBtn.start)
//                width = Dimension.wrapContent
//            }, R.drawable.ic_back,null,null,true)
//
//        ListingsSquareButton(Modifier.padding(top = 10.dp, start = 10.dp)
//            .constrainAs(sixthBtn) {
//                top.linkTo(thirdBtn.bottom)
//                start.linkTo(fifthBtn.end)
//                end.linkTo(seventhBtn.start)
//                width = Dimension.wrapContent
//            }, null,"w",null,false)
//
//        ListingsSquareButton(Modifier.padding(top = 10.dp, start = 10.dp)
//            .constrainAs(seventhBtn) {
//                top.linkTo(thirdBtn.bottom)
//                start.linkTo(sixthBtn.end)
//                end.linkTo(parent.end)
//                width = Dimension.wrapContent
//            }, null,null, Color.Green,false)
//
//        ListingsOutlinedButton(Modifier.padding(top = 10.dp, start = 10.dp)
//            .constrainAs(eightBtn) {
//                top.linkTo(fifthBtn.bottom)
//                start.linkTo(parent.start)
//                end.linkTo(sixthBtn.start)
//                width = Dimension.wrapContent
//            }, R.drawable.ic_back,null,null,true) {}
//
//        ListingsOutlinedButton(Modifier.padding(top = 10.dp, start = 10.dp)
//            .constrainAs(ninthBtn) {
//                top.linkTo(fifthBtn.bottom)
//                start.linkTo(fifthBtn.end)
//                end.linkTo(seventhBtn.start)
//                width = Dimension.wrapContent
//            }, null,"wweight",null,true) {}
//
//        ListingsOutlinedButton(Modifier.padding(top = 10.dp, start = 10.dp)
//            .constrainAs(tenthBtn) {
//                top.linkTo(fifthBtn.bottom)
//                start.linkTo(sixthBtn.end)
//                end.linkTo(parent.end)
//                width = Dimension.wrapContent
//            }, null,null, Color.Green,true) {}
    }
}