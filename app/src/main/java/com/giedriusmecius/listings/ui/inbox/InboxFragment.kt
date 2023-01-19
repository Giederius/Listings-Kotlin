package com.giedriusmecius.listings.ui.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.databinding.FragmentInboxBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.composeStyles.GreyBackgroundColor
import com.giedriusmecius.listings.ui.common.composeStyles.H2BOLD
import com.giedriusmecius.listings.ui.common.composeStyles.H5
import com.giedriusmecius.listings.ui.common.composeStyles.H5Grey
import com.giedriusmecius.listings.ui.common.composeStyles.ListingsPurple
import com.giedriusmecius.listings.ui.common.composeStyles.WarmPurple

class InboxFragment : BaseFragment<FragmentInboxBinding>(FragmentInboxBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inbox, container, false).apply {
            findViewById<ComposeView>(R.id.composeInboxView).setContent {
                Scaffold(backgroundColor = GreyBackgroundColor) {
                    InboxMainScreen()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).hideBottomNavBar()
    }


    @Composable
    fun InboxMainScreen() {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize().background(color = GreyBackgroundColor)
        ) {
            val (close, mailIcon, orderDetailsContainer) = createRefs()

            Image(
                painterResource(R.drawable.ic_x),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(48.dp)
                    .padding(12.dp)
                    .clickable {
                        navigateUp()
                    }
                    .constrainAs(close) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    })

            Image(
                painterResource(R.drawable.ic_inbox),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(32.dp)
                    .constrainAs(mailIcon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.wrapContent
                        width = Dimension.wrapContent
                    })

            OrderDetailsContainer("orderDetails", Modifier
                .padding(top = 32.dp)
                .constrainAs(orderDetailsContainer) {
                    top.linkTo(mailIcon.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.wrapContent
                    width = Dimension.wrapContent
                })
        }
    }

    @Composable
    fun OrderDetailsContainer(orderDetails: Any, modifier: Modifier) {
        Column(
            modifier.padding(horizontal = 24.dp)
                .fillMaxWidth()
                .background(shape = RoundedCornerShape(16.dp), color = Color.White)
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 16.dp)
        ) {
            TrackableProgressBar2(Modifier, 3, 2)

            Text(
                text = "Your package is on it's way",
                style = H2BOLD,
//                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(top = 24.dp)
            )

            Text(
                text = "Arrival estimate: May 12",
                style = H5,
                modifier = Modifier
            )
            InboxOrderDetailItem(modifier = Modifier, order = "hey")
        }
//        ConstraintLayout(
//            modifier.padding(horizontal = 24.dp)
//                .size(337.dp)
//                .fillMaxWidth()
//                .background(shape = RoundedCornerShape(16.dp), color = Color.White)
//                .padding(horizontal = 24.dp)
//                .padding(top = 32.dp, bottom = 16.dp)
//        ) {
//            val (progress, title, arrivalDate, orderRow, moreInfo, cancelOrder) = createRefs()
//            TrackableProgressBar2(Modifier.constrainAs(progress) {
//                top.linkTo(parent.top)
//                start.linkTo(parent.start)
//                end.linkTo(parent.end)
//            }, 3, 2)
//
//            Text(
//                text = "Your package is on it's way",
//                style = H2BOLD,
////                textAlign = TextAlign.Start,
//                modifier = Modifier
//                    .padding(top = 24.dp)
//                    .constrainAs(title) {
//                        start.linkTo(parent.start)
//                        top.linkTo(progress.bottom)
//                        end.linkTo(parent.end)
//                        width = Dimension.matchParent
//                        height = Dimension.wrapContent
//                    })
//
//            Text(
//                text = "Arrival estimate: May 12",
//                style = H5,
//                modifier = Modifier.constrainAs(title) {
//                    start.linkTo(parent.start)
//                    top.linkTo(title.bottom)
//                    end.linkTo(parent.end)
//                    width = Dimension.wrapContent
//                    height = Dimension.wrapContent
//                })
//        }
    }

    @Composable
    private fun TrackableProgressBar2(modifier: Modifier, numberOfSteps: Int, currentStep: Int) {
        Row(
            modifier = modifier.width(279.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            for (step in 0..numberOfSteps) {
                var lastWidth = 15.dp / numberOfSteps
                val mod: Modifier = if (step != numberOfSteps) {
                    val width = (279.dp / numberOfSteps) - lastWidth
                    Modifier.width(width)
                } else {
                    Modifier.width(15.dp)
                }
                Step2(
                    modifier = mod,
                    stepState = when {
                        step < currentStep -> {
                            StepState2.Previous
                        }
                        step == currentStep -> {
                            StepState2.Current
                        }
                        else -> {
                            StepState2.Next
                        }
                    },
                    isLast = step == numberOfSteps
//                    isComplete = step < currentStep,
//                    isCurrent = step == currentStep,
//                    isFirst = step == 0
                )
            }
        }
    }

    @Composable
//    private fun Step(modifier: Modifier, isComplete: Boolean, isCurrent: Boolean, isFirst: Boolean) {
    private fun Step2(modifier: Modifier, stepState: StepState2, isLast: Boolean) {
        val color = stepState.color
        val lineColors = when (stepState) {
            StepState2.Previous -> ListingsPurple
            StepState2.Next, StepState2.Current -> WarmPurple
        }
        val lineMod = if (isLast) {
            Modifier.alpha(0f)
        } else {
            Modifier
        }
        val currentMod = Modifier.size(15.dp)

        Row(modifier = modifier, Arrangement.aligned(Alignment.Start), Alignment.CenterVertically) {
            Canvas(
                modifier = currentMod,
                onDraw = {
                    drawCircle(color = color)
//                    if (!isLast) {
//                        drawLine(color = color, start = Offset.Zero, end = Offset.Zero, strokeWidth = 3F)
//                    }
                }
            )
            if (!isLast) {
                // kazkodel uzima vieta bet nepasinaikina lopas pilnai. nes 15dp currwntmod
                Divider(
                    modifier = lineMod,
                    color = lineColors,
                    thickness = 3.dp
                )
            }
        }
    }

    @Composable
    fun InboxOrderDetailItem(modifier: Modifier, order: String) {
        ConstraintLayout(modifier = modifier) {
            val (img, title, detatils) = createRefs()
            Image(
                painter = rememberAsyncImagePainter(order),
                contentDescription = null,
                modifier = Modifier.constrainAs(img) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                })

            Text(text = "adfasdf", style = H5, modifier = Modifier.constrainAs(title) {
                top.linkTo(img.top)
                bottom.linkTo(detatils.top)
                start.linkTo(img.end)
            })

            Text(text = "sssss", style = H5Grey, modifier = Modifier.constrainAs(detatils) {
                top.linkTo(title.bottom)
                bottom.linkTo(img.bottom)
                start.linkTo(img.end)
            })
        }
    }

    enum class StepState2(val color: Color) {
        Current(ListingsPurple), Next(WarmPurple), Previous(ListingsPurple)
    }
}