package com.giedriusmecius.listings.ui.checkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.local.CardType
import com.giedriusmecius.listings.data.local.PaymentMethod
import com.giedriusmecius.listings.data.local.User
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.databinding.FragmentCheckoutBinding
import com.giedriusmecius.listings.ui.checkout.checkoutScreens.AddressScreen
import com.giedriusmecius.listings.ui.checkout.checkoutScreens.DetailsScreen
import com.giedriusmecius.listings.ui.checkout.checkoutScreens.PaymentScreen
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.ui.common.composeStyles.DisabledBackgroundColor
import com.giedriusmecius.listings.ui.common.composeStyles.H5
import com.giedriusmecius.listings.ui.common.composeStyles.H5Black
import com.giedriusmecius.listings.ui.common.composeStyles.ListingsPurple
import com.giedriusmecius.listings.ui.common.composeStyles.WarmPurple
import com.giedriusmecius.listings.ui.profile.ProfileAddAddressDialogFragment
import com.giedriusmecius.listings.ui.profile.ProfileAddCardDialogFragment
import com.giedriusmecius.listings.ui.profile.ProfileState
import com.giedriusmecius.listings.ui.views.ListingsButtonComposable
import com.giedriusmecius.listings.utils.extensions.getNavigationResult
import com.giedriusmecius.listings.utils.extensions.toCurrency
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutFragment : BaseFragment<FragmentCheckoutBinding>(FragmentCheckoutBinding::inflate) {
    private val vm by viewModels<CheckoutViewModel>()
    private val navArgs by navArgs<CheckoutFragmentArgs>()

    var selectedAddress =
        UserAddress(
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_checkout, container, false).apply {
            findViewById<ComposeView>(R.id.checkoutComposeView).setContent {
                Scaffold() {
                    CheckoutMainScreen()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNavBar()
        vm.machine.transition(CheckoutState.Event.ViewCreated)
        listenForAddressDialog()
        listenForCardAdd()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showBottomNavBar()
    }

    @Composable
    private fun CheckoutMainScreen(
//        addressList: List<UserAddress>,
//        paymentMethodList: List<PaymentMethod>,
//        cartItems: List<Product>,
//        selectedAddress: UserAddress
    ) {
        val addressList by vm.userAddresses.observeAsState(initial = emptyList())
        val paymentMethodList by vm.paymentMethods.observeAsState(initial = emptyList())
        val cartItems by vm.cartItems.observeAsState(initial = emptyList())
        val currentUser by vm.currentUser.observeAsState(initial = User())
        val selectedUserAddress by vm.selectedAddress.observeAsState(initial = selectedAddress)
        var selectedPaymentMethod by mutableStateOf(PaymentMethod(0L, "", CardType.VISA, "", 0))
//        var selectedUserAddress by remember { mutableStateOf(selectedAddress) }

        var checkoutProgress by rememberSaveable {
            mutableStateOf(0)
        }

        BackHandler(enabled = checkoutProgress != 0) {
            if (checkoutProgress != 0) {
                checkoutProgress--
            }
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (topBar, container, bottomBar) = createRefs()
            TopBarWithAnimation(
                Modifier
                    .padding(horizontal = 24.dp)
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.wrapContent
                        width = Dimension.matchParent
                    }, checkoutProgress
            ) {
                if (checkoutProgress == 0) {
                    navigateUp()
                } else {
                    checkoutProgress--
                }
            }

            Box(modifier = Modifier
                .fillMaxSize()
                .constrainAs(container) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                    width = Dimension.matchParent
                }) {
                when (checkoutProgress) {
                    0 -> {
                        // address screen
//                        Text(text = "1", style = H2, modifier = Modifier.align(Alignment.Center))
                        if (addressList.isEmpty()) {
                            CircularProgressIndicator(Modifier)
                        } else {
                            Log.d("MANOpreAddressScreen", "${selectedUserAddress.addressLabel} ${selectedAddress.addressLabel}")
                            AddressScreen(
                                modifier = Modifier,
                                addresses = addressList,
                                selectedAddress = if (selectedUserAddress.addressLabel.isNotEmpty()) selectedUserAddress else addressList.first(),
                                onAddressChange = {
//                                    selectedAddress = it
                                    Log.d("MANOonAddressChange", "${it.addressLabel}")
                                    vm.machine.transition(CheckoutState.Event.TappedChangeAddress(it))
                                },
                                onEditClick = {
                                    vm.machine.transition(CheckoutState.Event.TappedAddressEdit(it))
                                },
                                onAddNewAddress = {
                                    vm.machine.transition(
                                        CheckoutState.Event.TappedAddNewAddress(it)
                                    )
                                }
                            )
                        }
                    }
                    1 -> {
                        // payment screen
                        PaymentScreen(Modifier, paymentMethodList, selectedPaymentMethod, {
                            selectedPaymentMethod = it
                        }, {
                            vm.transition(CheckoutState.Event.TappedAddPaymentMethod)
                        })
//                        Text(text = "2", style = H2, modifier = Modifier.align(Alignment.Center))
                    }
                    2 -> {
                        // confirmation screen
                        DetailsScreen(Modifier, cartItems, selectedPaymentMethod, addressList[0])
//                        Text(text = "3", style = H1, modifier = Modifier.align(Alignment.Center))
                    }
                }
            }

            BottomCheckoutBar(Modifier.wrapContentHeight().constrainAs(bottomBar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
                width = Dimension.wrapContent
            }, checkoutProgress, selectedPaymentMethod, selectedAddress) { checkoutProgress++ }
        }
    }

    @Composable
    private fun TopBarWithAnimation(
        modifier: Modifier,
        checkoutProgress: Int,
        onBackPressed: () -> Unit
    ) {
        ConstraintLayout(modifier = modifier) {
            val (backBtn, progressBar) = createRefs()
            Icon(
                painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding((12.dp))
                    .clickable {
                        onBackPressed()
                    })
            TrackableProgressBar(Modifier.constrainAs(progressBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.wrapContent
            }, 2, checkoutProgress)
        }
    }

    @Composable
    private fun BottomCheckoutBar(
        modifier: Modifier,
        checkoutProgress: Int,
        selectedPayment: PaymentMethod,
        selectedAddress: UserAddress,
        onClick: () -> Unit
    ) {
        var isGone = checkoutProgress == 2

        var enabledBtn = when {
            checkoutProgress != 1 && selectedPayment.number == 0L -> true
            checkoutProgress == 1 && selectedPayment.number != 0L -> true
            checkoutProgress != 0 && selectedAddress.addressLabel == "" -> true
            checkoutProgress == 0 && selectedAddress.addressLabel != "" -> true
            else -> false
        }

        AnimatedVisibility(
            modifier = modifier,
            visible = checkoutProgress != 2,
            enter = slideInVertically { fullHeight -> fullHeight },
            exit = slideOutVertically { fullHeight -> fullHeight }) {
            Column(modifier.background(Color.White)) {
                Divider(color = DisabledBackgroundColor, thickness = 1.dp)
                Row(
                    modifier = modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "Subtotal(VAT included)", style = H5)
                    Text(text = navArgs.price.toCurrency(), style = H5Black)
                }

                ListingsButtonComposable(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 16.dp), null, "Continue", null, enabledBtn
                ) { onClick() }
            }
        }
    }

    @Composable
    private fun TrackableProgressBar(modifier: Modifier, numberOfSteps: Int, currentStep: Int) {
        Row(
            modifier = modifier.width(240.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            for (step in 0..numberOfSteps) {
                Step(
                    modifier = Modifier.weight(1F),
                    stepState = when {
                        step < currentStep -> {
                            StepState.Previous
                        }
                        step == currentStep -> {
                            StepState.Current
                        }
                        else -> {
                            StepState.Next
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
    private fun Step(modifier: Modifier, stepState: StepState, isLast: Boolean) {
        val color = stepState.color
        val lineColors = when (stepState) {
            StepState.Current, StepState.Next -> WarmPurple
            StepState.Previous -> ListingsPurple
        }
        val lineMod = if (isLast) {
            Modifier.alpha(0f)
        } else {
            Modifier
        }
        val currentMod = when (stepState) {
            StepState.Current -> {
                Modifier
                    .size(15.dp)
                    .border(shape = CircleShape, width = 2.dp, color = color)
                    .padding(4.dp)
            }
            StepState.Previous -> {
                Modifier.size(15.dp)
            }
            StepState.Next -> {
                Modifier.size(15.dp)
            }
        }

        Row(modifier = modifier, Arrangement.aligned(Alignment.Start), Alignment.CenterVertically) {
            Canvas(
                modifier = currentMod,
                onDraw = {
                    drawCircle(color = color)
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
    @Preview
    fun previewContainer() {
        CheckoutMainScreen()
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(viewLifecycleOwner) { _, newState ->
            when (val cmd = newState.command) {
                is CheckoutState.Command.OpenAddressDialog -> {
                    navigate(
                        CheckoutFragmentDirections.checkoutFragmentToAddressDialog(
                            cmd.address,
                            cmd.isEdit
                        )
                    )
                }
                is CheckoutState.Command.HandleAddressChange -> {
                    Log.d("MANOaddes", "atejo ${selectedAddress.addressLabel}")
                    selectedAddress = cmd.selectedAddress
                }
                is CheckoutState.Command.OpenAddPaymentMethod -> {
                    navigate(CheckoutFragmentDirections.checkoutFragmentToCardDialog())
                }
                else -> {}
            }
        }
    }

    private fun listenForAddressDialog() {
        getNavigationResult<Triple<Boolean, UserAddress, UserAddress>>(
            R.id.checkoutFragment,
            ProfileAddAddressDialogFragment.RESULT_KEY
        ) {
            if (it.first) {
                vm.machine.transition(CheckoutState.Event.ReceivedAddressEdit(it.second, it.third))
                selectedAddress = it.second
            } else {
                vm.machine.transition(CheckoutState.Event.ReceivedNewUserAddress(it.second))
            }
        }
    }

    private fun listenForCardAdd() {
        getNavigationResult<Triple<Boolean, PaymentMethod, PaymentMethod>>(
            // isEdit, newCard, oldCard
            R.id.checkoutFragment,
            ProfileAddCardDialogFragment.RESULT_KEY
        ) {
            if (it.first) {
                // not implemented in design?
//                vm.transition(ProfileState.Event.EditedPaymentMethod(it.second, it.third))
            } else {
                vm.transition(CheckoutState.Event.AddedPaymentMethod(it.second))
            }
        }
    }

    enum class StepState(val color: Color, val hasBorder: Boolean = false) {
        Current(ListingsPurple, true), Next(WarmPurple), Previous(ListingsPurple)
    }
}