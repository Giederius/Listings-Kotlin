package com.giedriusmecius.listings.ui.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.layoutId
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.compose.rememberAsyncImagePainter
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.databinding.FragmentProductBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.composeStyles.H5
import com.giedriusmecius.listings.ui.common.composeStyles.H5Black
import com.giedriusmecius.listings.ui.common.composeStyles.H5Grey
import com.giedriusmecius.listings.ui.common.composeStyles.H5White
import com.giedriusmecius.listings.ui.common.composeStyles.ListingsPurple
import com.giedriusmecius.listings.ui.common.composeStyles.Neutral10
import com.giedriusmecius.listings.ui.views.ListingsButtonComposable
import com.giedriusmecius.listings.ui.views.ListingsOutlinedButton
import com.giedriusmecius.listings.utils.extensions.toCurrency
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : BaseFragment<FragmentProductBinding>(FragmentProductBinding::inflate) {
    private val vm by viewModels<ProductViewModel>()
    private val args by navArgs<ProductFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product, container, false).apply {
            findViewById<ComposeView>(R.id.productFragmentCompose).setContent {
                MainContainer()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.transition(ProductState.Event.ViewCreated(args.productID))
        (activity as MainActivity).hideBottomNavBar()
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(this) { _, newState ->
            when (val cmd = newState.command) {
                is ProductState.Command.ShowAddedToCartIndicator -> {
                    Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                }
                is ProductState.Command.ShowErrorAddingToCartIndicator -> {

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showBottomNavBar()
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun MainContainer() {
        val product by vm.product.observeAsState()
        val inCartItems by vm.inCart.observeAsState()

        Column(
            modifier = Modifier.background(Color.White),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // cia gal butu visai cool pasidaryti kaip yra galvijuos, su papildomais state(loading, success, error), kad UI butu galima geriau tvarkyt?
            AnimatedContent(targetState = product) {
                when (product) {
                    null -> {
                        CircularProgressIndicator(Modifier)
                    }
                    else -> {
                        ProductFragmentScreen(product!!, inCartItems!!, Modifier.fillMaxSize())
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun ProductFragmentScreen(product: Product, inCartItems: Int, modifier: Modifier) {
        val isCartActive by remember { mutableStateOf(inCartItems >= 1) }
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topbar, bottomBar, atcIndicator, productDetails) = createRefs()

            ProductTopBar(modifier = Modifier.constrainAs(topbar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            })
            TopProductScreen(product, Modifier.constrainAs(productDetails) {
                top.linkTo(topbar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(bottomBar.top)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
//            AnimatedContent(targetState = isCartActive, modifier = Modifier.constrainAs(atcIndicator) {
//                bottom.linkTo(bottomBar.top)
//                start.linkTo(parent.start)
//                end.linkTo(parent.end)
//            }) {
//                AddedToCartIndicator(inCartItems, modifier = Modifier.constrainAs(atcIndicator) {
//                    bottom.linkTo(bottomBar.top)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                })
//            }
            ProductBottomBar(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .constrainAs(bottomBar) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }, isCartActive
            ) {
                vm.transition(ProductState.Event.AddedToCart)
            }
        }
    }

    @Composable
    fun TopProductScreen(product: Product, modifier: Modifier) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProductMainDetails(product)
        }
    }

    @OptIn(ExperimentalMotionApi::class)
    @Composable
    fun ProductMainDetails(product: Product? = null) {
        val imgList = listOf(product?.image, product?.image, product?.image)
        val listState: LazyListState = rememberLazyListState()
        val scrollState = rememberScrollState()
        val progress = (scrollState.value.toFloat() / 100).takeIf { it <= 1 } ?: 1F
        val context = LocalContext.current
        val motionSceneContent = remember {
            context.resources
                .openRawResource(R.raw.product_motion_scene)
                .readBytes()
                .decodeToString()
        }

//        MotionLayout(motionScene = MotionScene(motionSceneContent), progress = progress) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LazyRow(state = listState, modifier = Modifier.layoutId("imgLazyRow")) {
                items(items = imgList) { img ->
                    ProductImage(isFirst = img == imgList.first(), img = img ?: "") {
                        Log.d(
                            "MANO",
                            "clicked img"
                        )
                    }
                }
            }
            Divider(
                modifier = Modifier.width(56.dp).height(4.dp).layoutId("divider"),
                color = Neutral10
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).layoutId("titleIMG"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = product?.title ?: "", style = H5)
                Image(
                    painter = rememberAsyncImagePainter(""),
                    modifier = Modifier.clip(CircleShape).size(48.dp),
                    contentDescription = null
                )
            }
            Text(
                text = product?.price?.toCurrency() ?: "",
                style = H5Black,
                modifier = Modifier.padding(start = 24.dp, bottom = 16.dp).layoutId("price")
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                    .padding(bottom = 36.dp).layoutId("buttonRow"),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ListingsOutlinedButton(
                    modifier = Modifier.height(48.dp).padding(end = 3.5.dp).weight(1F),
                    null,
                    text = "Color",
                    color = Color.Blue,
                    false,
                ) {

                }
                ListingsOutlinedButton(
                    modifier = Modifier.height(48.dp).padding(start = 3.5.dp).weight(1F),
                    null,
                    text = "Size",
                    null,
                    true,
                ) {

                }
            }
        }
//        }

    }

    @Composable
    fun ProductTopBar(modifier: Modifier) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            val (backIcon, saveIcon, moreIcon) = createRefs()
            Icon(
                painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(12.dp)
                    .constrainAs(backIcon) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }.clickable {
                        navigateUp()
                    })
            Icon(
                painterResource(id = R.drawable.ic_bookmark),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(12.dp)
                    .constrainAs(saveIcon) {
                        end.linkTo(moreIcon.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    })
            Icon(
                painterResource(id = R.drawable.ic_more_horizontal),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(12.dp)
                    .constrainAs(moreIcon) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    })
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun ProductBottomBar(modifier: Modifier, isCartActive: Boolean, onATC: () -> Unit) {

        AnimatedContent(
            targetState = isCartActive,
            modifier = modifier.padding(horizontal = 24.dp)
        ) {
            when (isCartActive) {
                true -> {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ListingsOutlinedButton(
                            modifier = Modifier.height(48.dp).padding(end = 3.5.dp).weight(1F),
                            null,
                            text = "Remove",
                            color = null,
                            true,
                        ) {

                        }
                        ListingsButtonComposable(
                            modifier = Modifier.height(48.dp).padding(start = 3.5.dp).weight(1F),
                            null,
                            letter = "Add to cart",
                            null,
                            true,
                        ) {

                        }
                    }
                }
                false -> {
                    ListingsButtonComposable(
                        modifier.fillMaxWidth(),
                        null,
                        "Add to card",
                        null,
                        true
                    ) {
                        onATC()
                    }
                }
            }

        }
    }

    @Composable
    fun ProductImage(isFirst: Boolean, img: String, onClick: () -> Unit) {
        if (isFirst) {
            Image(
                painter = rememberAsyncImagePainter(img),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(width = 375.dp, height = 396.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                    .clickable { onClick() }
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(img),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(width = 375.dp, height = 396.dp)
                    .clickable { onClick() }
            )
        }
    }

    @Composable
    fun AddedToCartIndicator(inCartItems: Int, modifier: Modifier) {
//        Row(
//            modifier = Modifier.background(color = ListingsPurple).height(64.dp).fillMaxWidth()
//                .clickable {
//                    navigate(ProductFragmentDirections.globalCartFragmentAction())
//                }) {
//            Image(painter = painterResource(R.drawable.ic_shopping_cart), contentDescription = null)
//            Text(text = "Shopping cart", style = H5White)
//            Image(painter = painterResource(R.drawable.icon_arrow_right), contentDescription = null)
//        }

        ConstraintLayout(
            modifier = modifier.background(color = ListingsPurple).height(64.dp).fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clickable {
                    navigate(ProductFragmentDirections.globalCartFragmentAction())
                }) {
            val (cartIcon, text, btn, arrow) = createRefs()
            Image(
                painter = painterResource(R.drawable.ic_shopping_cart),
                contentDescription = null,
                modifier = Modifier.constrainAs(cartIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(text.start)
                }
            )
            Text(text = "Shopping cart", style = H5White, modifier = Modifier.constrainAs(text) {
                start.linkTo(cartIcon.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })

            Text(
                text = resources.getQuantityString(R.plurals.inCartProductCount, inCartItems),
                style = H5Grey,
                color = ListingsPurple,
                modifier = Modifier.background(
                    color = Neutral10,
                    shape = RoundedCornerShape(4.dp)
                ).constrainAs(btn) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(arrow.start)
                }
            )
            Image(
                painter = painterResource(R.drawable.icon_arrow_right),
                contentDescription = null,
                modifier = Modifier.constrainAs(arrow) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })
        }
    }

    @Preview
    @Composable
    fun MainContainerPreview() {
        MainContainer()
    }
}