package com.simple.games.tradeassist.core.navigation

import android.app.Activity
import android.content.Context
import androidx.navigation.NavHostController
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.domain.GodEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {
    private var activity: Context? = null
    private var controller: NavHostController? = null

    fun attachController(activity: Activity, controller: NavHostController) {
        this.controller = controller
        this.activity = activity
    }

    fun detachController() {
        this.controller = null
        this.activity = null
    }

    fun toBack(vararg result: Pair<String, Any>) {
        result.forEach { (key, value) ->
            controller?.putResult(key, value)
        }
        controller?.popBackStack()
    }

    fun toMain() {
        controller?.navigate(AppRoute.MainRoute.route) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }

    fun toGodsSelection(customer: CustomerData? = null) {
        controller?.putArgument(AppRoute.GodsSelectionRoute.argCustomer, customer)
        controller?.navigate(AppRoute.GodsSelectionRoute.route)
    }

    fun toGodsInfo(customer: CustomerData?, god: GodEntity, amount: Float? = null, price: Float? = null) {
        controller?.putArgument(AppRoute.GodsInfoRoute.argGods, god)
        controller?.putArgument(AppRoute.GodsInfoRoute.argCustomer, customer)
        controller?.putArgument(AppRoute.GodsInfoRoute.argAmount, amount)
        controller?.putArgument(AppRoute.GodsInfoRoute.argPrice, price)
        controller?.navigate(AppRoute.GodsInfoRoute.route)
    }

    fun toOrders() {
        controller?.navigate(AppRoute.OrdersRoute.route)
    }

    fun toCreateOrder() {
        controller?.navigate(AppRoute.CreateOrder.route)
    }
}

fun <T> NavHostController.putResult(key: String, value: T) {
    previousBackStackEntry?.savedStateHandle?.set(key, value)
}

fun <T> NavHostController.getResult(key: String): T? {
    return currentBackStackEntry?.savedStateHandle?.remove(key)
}

fun <T> NavHostController.putArgument(key: String, value: T) {
    currentBackStackEntry?.savedStateHandle?.set(key, value)
}

fun <T> NavHostController.getArgument(key: String, clear: Boolean = false): T? {
    return if (clear) {
        previousBackStackEntry?.savedStateHandle?.remove(key)
    } else {
        previousBackStackEntry?.savedStateHandle?.get(key)
    }
}