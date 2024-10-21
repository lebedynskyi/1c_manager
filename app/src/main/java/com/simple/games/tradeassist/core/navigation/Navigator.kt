package com.simple.games.tradeassist.core.navigation

import android.app.Activity
import android.content.Context
import androidx.navigation.NavHostController
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.domain.entity.GodEntity
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

    fun toGodsSelection(orderId: Long? = null) {
        controller?.putArgument(AppRoute.GodsSelectionRoute.argOrderId, orderId)
        controller?.navigate(AppRoute.GodsSelectionRoute.route)
    }

    fun toGodsInfo(god: GodEntity, customerName: String? = null, customerKey: String? = null, amount: Float? = null, price: Float? = null) {
        controller?.putArgument(AppRoute.GodsInfoRoute.argGods, god)
        controller?.putArgument(AppRoute.GodsInfoRoute.argCustomerKey, customerKey)
        controller?.putArgument(AppRoute.GodsInfoRoute.argCustomerName, customerName)
        controller?.putArgument(AppRoute.GodsInfoRoute.argAmount, amount)
        controller?.putArgument(AppRoute.GodsInfoRoute.argPrice, price)
        controller?.navigate(AppRoute.GodsInfoRoute.route)
    }

    fun toOrders() {
        controller?.navigate(AppRoute.OrdersRoute.route)
    }

    fun toCreateOrder(localId: Long) {
        controller?.putArgument(AppRoute.CreateOrder.argDraftId, localId)
        controller?.navigate(AppRoute.CreateOrder.route)
    }

    fun toCustomers(){
        controller?.navigate(AppRoute.CustomersListRoute.route)
    }

    fun toCustomerDetails(customerData: CustomerData){
        controller?.putArgument(AppRoute.CustomersDetailsRoute.argCustomer, customerData)
        controller?.navigate(AppRoute.CustomersDetailsRoute.route)
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