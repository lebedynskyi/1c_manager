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

    object LoansRoute {
        val route = "Loans"
    }

    object GodsSelectionRoute {
        val route = "GodsSelection"
        val resultSelectedGods = "GodsSelectionResultGodKey"
        val argOrderId = "GodsSelectionArgOrderId"
    }

    object GodsInfoRoute {
        val route = "GodsInfoRoute"
        val argGods = "GodsInfoGodsKey"
        val argCustomerName = "GodsInfoCustomerName"
        val argCustomerKey = "GodsInfoCustomerKey"
        val argAmount = "GodsInfoAmount"
        val argPrice = "GodsInfoPrice"

        val resultOrder = "GodsInfoResultOrder"
    }

    object CreateOrder {
        val route = "CreateOrders"
        val argDraftId = "CreateOrdersDraftId"
    }
}