package com.simple.games.tradeassist.ui.order

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import com.simple.games.tradeassist.ui.base.design.AppTopBar

@Composable
fun OrdersScreen(
    state: OrdersViewState,
    onUIEvent: (AppUIEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            Column(Modifier.fillMaxWidth()) {
                AppTopBar(title = R.string.orders, navigationIcon = R.drawable.ic_arrow_back, onNavigationClick = {
                    onUIEvent(AppUIEvent.OnBackClicked)
                })
                HorizontalDivider(thickness = 0.5.dp)
                TabRow(selectedTabIndex = state.selectedTab) {
                    Tab(modifier = Modifier.height(48.dp),
                        selected = false, onClick = {
                            onUIEvent(OrdersUIEvent.OnDraftsClicked)
                        }) {
                        Text(text = stringResource(id = R.string.drafts))
                    }

                    Tab(modifier = Modifier.height(48.dp),
                        selected = false, onClick = {
                            onUIEvent(OrdersUIEvent.OnHistoryClicked)
                        }) {
                        Text(text = stringResource(id = R.string.history))
                    }
                }
            }
        }
    ) {
        OrdersScreenContent(
            isDraft = state.isDrafts,
            modifier = Modifier.padding(it)
        ) {
            onUIEvent(OrdersUIEvent.CreateOrder)
        }
    }

    BackHandler {
        onUIEvent(AppUIEvent.OnBackClicked)
    }
}

@Composable
fun OrdersScreenContent(
    isDraft: Boolean,
    modifier: Modifier = Modifier,
    orders: List<Any> = emptyList(),
    onCreateOrder: () -> Unit = {},
) {
    if (orders.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(text = stringResource(id = R.string.no_orders))
                if (isDraft) {
                    Button(onClick = { onCreateOrder() }) {
                        Text(text = stringResource(id = R.string.create_order))
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewOrdersScreen() {
    TradeAssistTheme {
        OrdersScreen(state = OrdersViewState(0, orders = emptyList())) {

        }
    }
}