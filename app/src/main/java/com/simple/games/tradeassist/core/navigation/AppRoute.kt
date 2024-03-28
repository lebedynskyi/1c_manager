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
    }

    object GodsInfoRoute {
        val route = "GodsInfo"
        val godsKey = "GodsInfoGodsKey"
        val customerKey = "GodsInfoCustomerKey"
    }

    object CreateOrder {
        val route = "CreateOrders"
    }
}