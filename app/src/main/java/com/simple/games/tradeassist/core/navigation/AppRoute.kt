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
        val resultSelectedGods = "GodsSelectionResultGodKey"
        val argCustomer = "GodsSelectionResultCustomer"

    }

    object GodsInfoRoute {
        val route = "GodsInfoRoute"
        val argGods = "GodsInfoGodsKey"
        val argCustomer = "GodsInfoCustomerKey"
        val argAmount = "GodsInfoAmount"
        val argPrice = "GodsInfoPrice"

        val resultOrder = "GodsInfoResultOrder"
    }

    object CreateOrder {
        val route = "CreateOrders"
    }
}