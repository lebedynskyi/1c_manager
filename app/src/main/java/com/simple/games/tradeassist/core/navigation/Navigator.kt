package com.simple.games.tradeassist.core.navigation

import android.app.Activity
import android.content.Context
import androidx.navigation.NavHostController
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

    fun toBack(result: List<Pair<String, Any>>? = null) {
        result?.forEach { (key, value) ->
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

    fun toGods() {
        controller?.navigate(AppRoute.GodsSelectionRoute.route)
    }

    fun toGodsInfo(customerKey: String?, godKey: String) {
        controller?.putArgument(AppRoute.GodsInfoRoute.godsKey, godKey)
        controller?.putArgument(AppRoute.GodsInfoRoute.customerKey, customerKey)
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