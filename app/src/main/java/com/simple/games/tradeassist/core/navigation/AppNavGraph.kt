package com.simple.games.tradeassist.core.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.simple.games.tradeassist.domain.GodEntity
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate
import com.simple.games.tradeassist.ui.gods.info.GodInfoScreen
import com.simple.games.tradeassist.ui.gods.info.GodInfoUIEvent
import com.simple.games.tradeassist.ui.gods.info.GodInfoViewModel
import com.simple.games.tradeassist.ui.gods.list.GodsSelectionScreen
import com.simple.games.tradeassist.ui.gods.list.GodsSelectionUIEvent
import com.simple.games.tradeassist.ui.gods.list.GodsSelectionViewModel
import com.simple.games.tradeassist.ui.loans.LoansScreen
import com.simple.games.tradeassist.ui.loans.LoansUIEvent
import com.simple.games.tradeassist.ui.loans.LoansViewModel
import com.simple.games.tradeassist.ui.login.LoginScreen
import com.simple.games.tradeassist.ui.login.LoginUIEvent
import com.simple.games.tradeassist.ui.login.LoginViewModel
import com.simple.games.tradeassist.ui.main.MainScreen
import com.simple.games.tradeassist.ui.main.MainUIEvent
import com.simple.games.tradeassist.ui.main.MainViewModel
import com.simple.games.tradeassist.ui.order.OrdersScreen
import com.simple.games.tradeassist.ui.order.OrdersUIEvent
import com.simple.games.tradeassist.ui.order.OrdersViewModel
import com.simple.games.tradeassist.ui.order.create.CreateOrderScreen
import com.simple.games.tradeassist.ui.order.create.CreateOrderUIEvent
import com.simple.games.tradeassist.ui.order.create.CreateOrderViewModel

fun NavGraphBuilder.applicationListNavGraph(
    controller: NavHostController,
) {
    composable(AppRoute.LoginRoute.route) {
        val viewModel = hiltViewModel<LoginViewModel>()
        val state by viewModel.viewState.collectAsState()

        LaunchedEffect(key1 = Unit) {
            viewModel.onUIEvent(LoginUIEvent.OnScreenLoaded)
        }

        LoginScreen(state, onUIEvent = viewModel::onUIEvent)
    }

    composable(AppRoute.MainRoute.route) {
        val viewModel = hiltViewModel<MainViewModel>()
        val state by viewModel.viewState.collectAsState()

        LaunchedEffect(key1 = Unit) {
            viewModel.onUIEvent(MainUIEvent.OnScreenLoaded)
        }

        MainScreen(state, onUIEvent = viewModel::onUIEvent)
    }

    composable(AppRoute.OrdersRoute.route) {
        val viewModel = hiltViewModel<OrdersViewModel>()
        val state by viewModel.viewState.collectAsState()

        LaunchedEffect(key1 = Unit) {
            viewModel.onUIEvent(OrdersUIEvent.OnScreenLoaded)
        }

        OrdersScreen(state, onUIEvent = viewModel::onUIEvent)
    }

    composable(AppRoute.CreateOrder.route) {
        val viewModel = hiltViewModel<CreateOrderViewModel>()
        val state by viewModel.viewState.collectAsState()

        LaunchedEffect(key1 = Unit) {
            val localOrderId = controller.getArgument<Long?>(AppRoute.CreateOrder.argDraftId) ?: return@LaunchedEffect
            val editedGod = controller.getResult<GodOrderTemplate>(AppRoute.GodsInfoRoute.resultOrder)

            viewModel.onUIEvent(CreateOrderUIEvent.OnScreenLoaded(localOrderId, editedGod))
        }

        CreateOrderScreen(state, onUIEvent = viewModel::onUIEvent)
    }

    composable(AppRoute.GodsSelectionRoute.route) {
        val viewModel = hiltViewModel<GodsSelectionViewModel>()
        val state by viewModel.viewState.collectAsState()

        LaunchedEffect(key1 = Unit) {
            val orderId = controller.getArgument<Long?>(AppRoute.GodsSelectionRoute.argOrderId)
            viewModel.onUIEvent(GodsSelectionUIEvent.OnScreenLoaded(orderId))

            controller.getResult<GodOrderTemplate>(AppRoute.GodsInfoRoute.resultOrder)?.let {
                viewModel.onUIEvent(GodsSelectionUIEvent.OnAddGodOrder(it))
            }
        }

        GodsSelectionScreen(state, onUIEvent = viewModel::onUIEvent)
    }

    composable(AppRoute.GodsInfoRoute.route) {
        val viewModel = hiltViewModel<GodInfoViewModel>()
        val state by viewModel.viewState.collectAsState()

        LaunchedEffect(key1 = Unit) {
            val god = controller.getArgument<GodEntity>(AppRoute.GodsInfoRoute.argGods) ?: return@LaunchedEffect
            val customerKey = controller.getArgument<String?>(AppRoute.GodsInfoRoute.argCustomerKey)
            val customerName = controller.getArgument<String?>(AppRoute.GodsInfoRoute.argCustomerName)
            val price = controller.getArgument<Float?>(AppRoute.GodsInfoRoute.argPrice)
            val amount = controller.getArgument<Float?>(AppRoute.GodsInfoRoute.argAmount)
            viewModel.onUIEvent(GodInfoUIEvent.OnScreenLoaded(god, customerName, customerKey, price, amount))
        }

        GodInfoScreen(state, onUIEvent = viewModel::onUIEvent)
    }


    composable(AppRoute.LoansRoute.route) {
        val viewModel = hiltViewModel<LoansViewModel>()
        val state by viewModel.viewState.collectAsState()

        LaunchedEffect(key1 = Unit) {
            viewModel.onUIEvent(LoansUIEvent.OnScreenLoaded)
        }

        LoansScreen(state, onUIEvent = viewModel::onUIEvent)
    }
}