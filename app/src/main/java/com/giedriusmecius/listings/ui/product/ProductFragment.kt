package com.giedriusmecius.listings.ui.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.viewModels
import coil.compose.rememberAsyncImagePainter
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.databinding.FragmentProductBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.composeStyles.Neutral10

class ProductFragment : BaseFragment<FragmentProductBinding>(FragmentProductBinding::inflate) {
    private val vm by viewModels<ProductViewModel>()

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

//    override fun observeState() {
//        vm.subscribeWithAutoDispose(this) { _, newState ->
//            when (val cmd = newState.command) {
//
//            }
//        }
//    }

    @Composable
    fun MainContainer() {
        Column(modifier = Modifier.background(Color.White)) {
            val data by vm.products.observeAsState(initial = emptyList())
            ProductTopBar()
            ProductMainDetails(data?.first())
            Divider()
        }
    }

    @Composable
    fun Divider() {
        Image(
            painter = painterResource(id = R.drawable.bg_rounded_rect_sm_border),
            contentDescription = null,
            modifier = Modifier.size(width = 56.dp, height = 4.dp).padding(top = 24.dp),
            alignment = Alignment.Center,
            colorFilter = ColorFilter.tint(Neutral10)
        )
    }

    @Preview
    @Composable
    fun DividerPreview(){
        Divider()
    }

    @Composable
    fun ProductMainDetails(data: Product? = null) {
        val imgList = listOf(data?.image, data?.image, data?.image)
        val listState: LazyListState = rememberLazyListState()
        LazyRow(state = listState) {
            items(items = imgList) { img ->
                ProductImage(isFirst = img == imgList.first(), img = img ?: "") {
                    Log.d(
                        "MANO",
                        "clicked img"
                    )
                }
            }
        }
    }

    @Composable
    fun ProductTopBar() {
        ConstraintLayout(
            modifier = Modifier
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

    @Preview
    @Composable
    fun MainContainerPreview() {
        MainContainer()
    }
}