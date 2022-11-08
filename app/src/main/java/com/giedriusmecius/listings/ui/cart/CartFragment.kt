package com.giedriusmecius.listings.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.viewModels
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.databinding.FragmentCartBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.composeItems.LazyCart
import com.giedriusmecius.listings.ui.common.composeStyles.H2
import com.giedriusmecius.listings.ui.common.composeStyles.H3SEMIBOLD
import com.giedriusmecius.listings.ui.common.composeStyles.H5
import com.giedriusmecius.listings.ui.common.composeStyles.H5Black
import com.giedriusmecius.listings.ui.common.composeStyles.H5White
import com.giedriusmecius.listings.utils.extensions.toCurrency
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.min

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {
    private val vm by viewModels<CartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false).apply {
            findViewById<ComposeView>(R.id.composeCartView).setContent {
                mainContainer()
            }
        }
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(this) { _, newState ->
            when (val cmd = newState.command) {
                is CartState.Command.StartCheckout -> {
                    navigate(CartFragmentDirections.cartFragmentToCheckoutFragment())
                }
            }
        }
    }

    @Composable
    private fun mainContainer() {
        val columnState = rememberScrollState()
        val scrollState = rememberLazyListState()
        val scrollOffset: Float = min(
            1f,
            1 - (scrollState.firstVisibleItemScrollOffset / 400f + scrollState.firstVisibleItemIndex)
        )
        val data by vm.products.observeAsState(initial = emptyList())
        val totalPrice by remember {
            derivedStateOf { calculateTotalPrice(data) }
        }

        val interactionSource = remember { MutableInteractionSource() }
        val pressed by interactionSource.collectIsPressedAsState()

//        val showGradient = remember {
//            scrollState.firstVisibleItemIndex + scrollState.firstVisibleItemScrollOffset
//        }

//        val evaluator = remember { ArgbEvaluator() }


        ConstraintLayout(
            Modifier
                .scrollable(
                    state = columnState,
                    enabled = true,
                    orientation = Orientation.Vertical
                )
                .padding(bottom = 32.dp)
        ) {
            val (topBar, cartItems, price, checkoutBtn) = createRefs()
            topBar(
                scrollOffset,
                Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(cartItems.top)
                })

            LazyCart(
                data = data,
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(cartItems) {
                        top.linkTo(topBar.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(price.top)
                        height = Dimension.fillToConstraints
                    },
//                    .drawWithContent {
//                        val gradient =
//                            Brush.verticalGradient(
//                                colors = listOf(
//                                    Color.Transparent,
//                                    getColorAtProgress(
//                                        progress = scrollOffset,
//                                        start = GreyBackgroundColor,
//                                        end = Color.Transparent,
//                                        evaluator = evaluator
//                                    )
//                                ),
//                                startY = size.height / 2,
//                                endY = size.height
//                            )
//                        drawRect(gradient, blendMode = BlendMode.SrcAtop)
//                        drawContent()

//                            onDrawWithContent {
//                                drawContent()
//                                drawRect(gradient, blendMode = BlendMode.Screen)
//                            }
//                    },
                listState = scrollState,
                onDelete = ::removeFromCart,
                onEdit = { Log.d("MANO", "edt") },
                onSave = { Log.d("MANO", "save") })

            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .constrainAs(price) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(checkoutBtn.top)
                    }
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "Subtotal(VAT included)", style = H5)
                Text(text = totalPrice.toCurrency(), style = H5Black)
            }

            Button(
                interactionSource = interactionSource,
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .indication(
                        interactionSource = interactionSource,
                        indication = rememberRipple(color = Color.Red)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .constrainAs(checkoutBtn) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                enabled = true,
                onClick = { vm.machine.transition(CartState.Event.TappedCheckout) }
            ) {
                Text("Continue to checkout", style = H5White)
            }
        }
    }

    private fun calculateTotalPrice(data: List<Product>?): Float {
        var price = 0F
        data?.forEach {
            price += it.price
        }
        return price
    }

    @Composable
    private fun topBar(scrollOffset: Float, modifier: Modifier) {
        var collapsed by remember { mutableStateOf(ToolbarStates.Expanded) }
        val toolbarSize by animateDpAsState(
            targetValue = max(
                collapsed.minHeight,
                collapsed.maxHeight * scrollOffset
            )
        )
        val toolbarPadding by animateDpAsState(targetValue = max(0.dp, 8.dp * scrollOffset))
        if (toolbarSize == 0.dp && collapsed == ToolbarStates.Expanded) {
            collapsed = ToolbarStates.Collapsed
        }

        ConstraintLayout(
            modifier = modifier
                .padding(top = toolbarPadding)
                .height(toolbarSize)
                .fillMaxWidth()
        ) {
            val (backBtn, cartImg, title) = createRefs()
            Icon(
                painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 29.dp)
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        if (collapsed != ToolbarStates.Expanded) {
                            bottom.linkTo(parent.bottom)
                        }
                    }
            )
            if (collapsed == ToolbarStates.Expanded) {
                Image(
                    painter = painterResource(id = R.drawable.ic_shopping_cart),
                    contentDescription = null,
                    modifier = Modifier
                        .size(26.dp)
                        .constrainAs(cartImg) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            }
            Text(
                text = "Shopping cart",
                style = if (collapsed == ToolbarStates.Expanded) {
                    H2
                } else {
                    H3SEMIBOLD
                },
                modifier = Modifier
                    .padding(
                        top = if (collapsed == ToolbarStates.Expanded) {
                            32.dp
                        } else {
                            0.dp
                        }
                    )
                    .constrainAs(title) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        if (collapsed == ToolbarStates.Expanded) {
                            top.linkTo(cartImg.bottom)
                        } else {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    })
        }
    }

    @Composable
    @Preview
    private fun previewMainContainer() {
        mainContainer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNavBar()
        vm.transition(CartState.Event.ViewCreated)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showBottomNavBar()
    }

    private fun removeFromCart(item: Product) {
        vm.machine.transition(CartState.Event.DeletedProduct(item))
    }

    enum class ToolbarStates(val maxHeight: Dp, val minHeight: Dp) {
        Collapsed(52.dp, 0.dp), Expanded(176.dp, 0.dp), Hidden(0.dp, 0.dp)
    }

}