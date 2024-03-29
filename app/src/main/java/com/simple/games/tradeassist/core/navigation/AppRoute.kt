package com.simple.games.tradeassist.core.navigation

sealed class AppRoute {
    object LoginRoute {
        val route = "Login"
    }

    object MainRoute {
        val route = "Main"
    }

    object OrdersRoute {
        val route = "Orders"
    }

    object GodsSelectionRoute {
        val route = "GodsSelection"
        val resultSelectedGodKey = "GodsSelectionResultGodKey"
        val customerKey = "GodsSelectionResultCustomerKey"

    }

    object GodsInfoRoute {
        val route = "GodsInfo"
        val godsKey = "GodsInfoGodsKey"
        val customerKey = "GodsInfoCustomerKey"

        val resultCustomerKey = "GodsInfoResultCustomerKey"
        val resultGodKey = "GodsInfoResultGodKey"
        val resultAmount = "GodsInfoResultAmount"
        val resultPrice = "GodsInfoResultPrice"
    }

    object CreateOrder {
        val route = "CreateOrders"
    }
}