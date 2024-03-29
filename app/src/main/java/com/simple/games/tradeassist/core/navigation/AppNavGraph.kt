package com.simple.games.tradeassist.core.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.ui.gods.info.GodInfoScreen
import com.simple.games.tradeassist.ui.gods.info.GodInfoUIEvent
import com.simple.games.tradeassist.ui.gods.info.GodInfoViewModel
import com.simple.games.tradeassist.ui.gods.list.GodsSelectionScreen
import com.simple.games.tradeassist.ui.gods.list.GodsSelectionUIEvent
import com.simple.games.tradeassist.ui.gods.list.GodsSelectionViewModel
import com.simple.games.tradeassist.ui.login.LoginScreen
import com.simple.games.tradeassist.ui.login.LoginUIEvent
import com.simple.games.tradeassist.ui.login.LoginViewModel
import com.simple.games.tradeassist.ui.main.MainScreen
import com.simple.games.tradeassist.ui.main.MainUIEvent
import com.simple.games.tradeassist.ui.main.MainViewModel
import com.simple.games.tradeassist.ui.order.OrdersScreen
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
            viewModel.onUIEvent(MainUIEvent.OnScreenLoaded)
        }

        OrdersScreen(state, onUIEvent = viewModel::onUIEvent)
    }

    composable(AppRoute.CreateOrder.route) {
        val viewModel = hiltViewModel<CreateOrderViewModel>()
        val state by viewModel.viewState.collectAsState()

        LaunchedEffect(key1 = Unit) {
            viewModel.onUIEvent(CreateOrderUIEvent.OnScreenLoaded)

//            val selectedGodResult = controller.getResult<String>(AppRoute.GodsSelectionRoute.resultSelectedGodKey)
//            selectedGodResult?.let {
//                viewModel.onUIEvent(CreateOrderUIEvent.OnGodSelected(it))
//            }
        }

        CreateOrderScreen(state, onUIEvent = viewModel::onUIEvent)
    }

    composable(AppRoute.GodsSelectionRoute.route) {
        val viewModel = hiltViewModel<GodsSelectionViewModel>()
        val state by viewModel.viewState.collectAsState()

        LaunchedEffect(key1 = Unit) {
            val customerKey = controller.getArgument<String>(AppRoute.GodsSelectionRoute.customerKey) ?: return@LaunchedEffect
            viewModel.onUIEvent(GodsSelectionUIEvent.OnScreenLoaded(customerKey))
        }

        GodsSelectionScreen(state, onUIEvent = viewModel::onUIEvent)
    }

    composable(AppRoute.GodsInfoRoute.route) {
        val viewModel = hiltViewModel<GodInfoViewModel>()
        val state by viewModel.viewState.collectAsState()

        LaunchedEffect(key1 = Unit) {
            val customer = controller.getArgument<CustomerData>(AppRoute.GodsInfoRoute.argCustomer) ?: return@LaunchedEffect
            val god = controller.getArgument<GodsData>(AppRoute.GodsInfoRoute.argGods) ?: return@LaunchedEffect
            viewModel.onUIEvent(GodInfoUIEvent.OnScreenLoaded(customer, god))
        }

        GodInfoScreen(state, onUIEvent = viewModel::onUIEvent)
        checkNavigation(state.navRoute)
    }


    private fun checkNavigation(approute) {

    }
}