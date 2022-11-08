package com.giedriusmecius.listings.ui.common.composeItems
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.Layout
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.text.lerp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.lerp
//import androidx.constraintlayout.compose.ConstraintLayout
//import androidx.constraintlayout.compose.ConstraintSet
//import androidx.constraintlayout.compose.ConstraintSetScope
//import com.giedriusmecius.listings.ui.common.composeStyles.H2
//import com.giedriusmecius.listings.ui.common.composeStyles.H5
//import kotlin.math.roundToInt
//
//
//private val ContentPadding = 8.dp
//private val Elevation = 4.dp
//private val ButtonSize = 24.dp
//private const val Alpha = 0.75f
//
//private val ExpandedPadding = 1.dp
//private val CollapsedPadding = 3.dp
//
//private val ExpandedToolbarHeight = 148.dp
//private val CollapsedToolBarHeight = 52.dp
//
//private val ExpandedTitleStyle = H2
//private val CollapsedTitleStyle = H5
//
//
//@Composable
//fun CollapsableToolbar(
//    progress: Float,
//    onBackBtn: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val titleStyle = with(LocalDensity.current) {
//        lerp(CollapsedTitleStyle, ExpandedTitleStyle, progress)
//    }
//
//    Surface(modifier = modifier, color = Color.Red, elevation = Elevation) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            CollapsableToolbarLayout(progress = progress) {
//
//            }
//        }
//    }
//}
//
//@Composable
//fun CollapsableToolbarLayout(
//    progress: Float,
//    modifier: Modifier = Modifier,
//    content: @Composable () -> Unit
//) {
//    ConstraintLayout(content = content) { measurables, constraints ->
//        lerp(start = newConstraintSet(true), stop = newConstraintSet(false), fraction = progress)
//    }
//
//    Layout(content = content) { measurables, constraints ->
//
//        check(measurables.size == 3)
//
//        val placeables = measurables.map {
//            it.measure(constraints)
//        }
//
//        layout(width = constraints.maxWidth, height = constraints.maxHeight) {
//
//            val expandedHorizontalGuideline = (constraints.maxHeight * 0.4f).roundToInt()
//            val collapsedHorizontalGuideline = (constraints.maxHeight * 0.5f).roundToInt()
//
//            val title = placeables[0]
//            val cartIcon = placeables[1]
//            val backIcon = placeables[2]
//
//            title.placeRelative(
//                y = lerp(
//                    start = ExpandedToolbarHeight,
//                    stop = CollapsedToolBarHeight,
//                    fraction = progress
//                )
//            )
//
//
//        }
//    }
//}
//
//private fun newConstraintSet(isExpanded: Boolean): ConstraintSet {
//    return ConstraintSet {
//        val btn = createRefFor("btn")
//        val cartIcon = createRefFor("")
//
//        constrain(btn) {
//            if (isExpanded) {
//                top.linkTo(parent.bottom)
//            } else {
//                top.linkTo(parent.top)
//            }
//        }
//    }
//}