package com.simple.games.tradeassist.ui.order

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.orDefault
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.PriceData
import com.simple.games.tradeassist.domain.GodEntity
import com.simple.games.tradeassist.domain.OrderEntity
import com.simple.games.tradeassist.ui.base.design.AppTopBar
import com.simple.games.tradeassist.ui.base.design.ContentLoadingIndicator
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate

@Composable
fun OrdersScreen(
    state: OrdersViewState,
    onUIEvent: (AppUIEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            Column(Modifier.fillMaxWidth()) {
                AppTopBar(
                    title = R.string.orders,
                    navigationIcon = R.drawable.ic_arrow_back,
                    onNavigationClick = {
                        onUIEvent(AppUIEvent.OnBackClicked)
                    }, menus = {
                        if (state.orders.isNotEmpty() && state.selectedTab == 0) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable { onUIEvent(OrdersUIEvent.CreateOrder) },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = rememberVectorPainter(image = Icons.Outlined.Add),
                                    contentDescription = null
                                )
                            }
                        }
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
            orders = state.orders,
            modifier = Modifier.padding(it),
            onCreateOrder = { onUIEvent(OrdersUIEvent.CreateOrder) },
            onDeleteClick = { onUIEvent(OrdersUIEvent.OnDeleteClick(it)) },
            onEditClick = { onUIEvent(OrdersUIEvent.OnEditClick(it)) },
            onPublishClick = { onUIEvent(OrdersUIEvent.OnPublishClick(it)) },
        )
    }

    BackHandler {
        onUIEvent(AppUIEvent.OnBackClicked)
    }

    ContentLoadingIndicator(show = state.requestInProgress)
}

@Composable
fun OrdersScreenContent(
    isDraft: Boolean,
    orders: List<OrderEntity>,
    modifier: Modifier = Modifier,
    onCreateOrder: () -> Unit = {},
    onDeleteClick: (OrderEntity) -> Unit = {},
    onEditClick: (OrderEntity) -> Unit = {},
    onPublishClick: (OrderEntity) -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        if (orders.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.no_orders))
                if (isDraft) {
                    Button(
                        modifier = Modifier.padding(top = 24.dp),
                        onClick = { onCreateOrder() }) {
                        Text(text = stringResource(id = R.string.create_order))
                    }
                }
            }
        } else {
            LazyColumn {
                items(orders.size) {
                    OrderItem(
                        orders[it],
                        onDeleteClick,
                        onEditClick,
                        onPublishClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderItem(
    order: OrderEntity,
    onDeleteClick: (OrderEntity) -> Unit = {},
    onEditClick: (OrderEntity) -> Unit = {},
    onPublishClick: (OrderEntity) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = order.customerName.orDefault("Нет заказчика"),
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider(thickness = 0.5.dp)
            Row {
                Text(text = "Ответственный: ")
                Text(
                    text = order.responsibleName.orDefault("Не указан"),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            HorizontalDivider(thickness = 0.5.dp)
            if (order.refKey.isNullOrBlank()) {
                Column {
                    order.gods.orEmpty().forEach {
                        Text(
                            text = "${it.godEntity.data.description}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Кол.: ${it.amount} ${it.godEntity.measureData?.name}")
                        Text(text = "Цена: ${it.price} грн")
                        Text(text = "Сумма: ${it.sum} грн")
                        HorizontalDivider(thickness = 0.5.dp)
                    }
                }
            }

            Row {
                Text(text = "Общая сумма заказа: ")
                Text(
                    text = "${order.gods.orEmpty().map { it.sum }.sum()} грн",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            HorizontalDivider(thickness = 0.5.dp)

            if (order.refKey.isNullOrBlank()) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        onClick = { onDeleteClick(order) }) {
                        Text(text = "Удалить")
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        onClick = { onEditClick(order) }) {
                        Text(
                            text = "Редактировать",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                Button(modifier = Modifier.fillMaxWidth(), onClick = { onPublishClick(order) }) {
                    Text(text = "Опубликовать")
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewOrdersScreen() {
    TradeAssistTheme {
        OrdersScreen(state = OrdersViewState(0, orders = listOf(
            OrderEntity().apply {
                customerName = "ФОП Скай лаб моторс покупатель"
                responsibleName = "Алина"
                gods = listOf(
                    GodOrderTemplate(
                        GodEntity(
                            GodsData().apply { description = "Кабель ШВВП 2х25" },
                            MeasureData().apply {
                                name = "шт"
                            },
                            price = listOf(PriceData().apply {
                                this.priceValue = 10.6F
                                this.priceTypeName = "Оптовая цена"
                            }, PriceData().apply {
                                this.priceValue = 11.6F
                                this.priceTypeName = "Актальная цена"
                            }),
                            0F
                        ),
                        13F,
                        222F
                    ),
                    GodOrderTemplate(
                        GodEntity(
                            GodsData().apply { description = "Провод 100м бухта 3х6" },
                            MeasureData().apply {
                                name = "шт"
                            },
                            price = listOf(PriceData().apply {
                                this.priceValue = 10.6F
                                this.priceTypeName = "Оптовая цена"
                            }, PriceData().apply {
                                this.priceValue = 11.6F
                                this.priceTypeName = "Актальная цена"
                            }),
                            0F
                        ),
                        5F,
                        130F
                    )
                )
            }
        )))
    }
}