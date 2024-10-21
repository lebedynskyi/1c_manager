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

    object CustomersListRoute {
        val route = "CustomerList"
    }

    object CustomersDetailsRoute {
        val route = "CustomerDetail"
        val argCustomer = "ArgCustomer"
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