@file:OptIn(ExperimentalMaterial3Api::class)

package com.simple.games.tradeassist.ui.gods.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.format
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.GodOrderData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.PriceData
import com.simple.games.tradeassist.domain.entity.GodEntity
import com.simple.games.tradeassist.ui.KeyBoard
import com.simple.games.tradeassist.ui.base.design.AppTopBar
import com.simple.games.tradeassist.ui.base.design.ContentLoadingIndicator
import kotlinx.coroutines.launch

@Composable
fun GodInfoScreen(
    state: GodInfoViewState, onUIEvent: (AppUIEvent) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var showAmountKeyboard by remember {
        mutableStateOf(false)
    }

    var showPriceKeyboard by remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        AppTopBar(title = R.string.god_info,
            navigationIcon = R.drawable.ic_arrow_back,
            onNavigationClick = {
                onUIEvent(AppUIEvent.OnBackClicked)
            },
            menus = {
                if (state.addBtnEnabled) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { onUIEvent(GodInfoUIEvent.OnAddClick) },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberVectorPainter(image = Icons.Outlined.Done),
                            contentDescription = null
                        )
                    }
                }
            })
    }) {
        GodInfoScreenContent(
            state.amount,
            state.price,
            state.marga,
            state.addBtnEnabled,
            state.godsEntity,
            state.orderHistory,
            state.historyName,
            state.debtAmount,
            modifier = Modifier.padding(it),
            onAmountPressed = { showAmountKeyboard = true },
            onPricePressed = { showPriceKeyboard = true },
            onAddClicked = { onUIEvent(GodInfoUIEvent.OnAddClick) })
    }

    ContentLoadingIndicator(show = state.requestInProgress)

    val amountSheet = rememberModalBottomSheetState()
    if (showAmountKeyboard) {
        ModalBottomSheet(
            sheetState = amountSheet,
            onDismissRequest = { showAmountKeyboard = false }) {
            KeyBoard("Колличесство", value = state.amount) {
                onUIEvent(GodInfoUIEvent.OnAmountChanged(it))
                scope.launch {
                    amountSheet.hide()
                    showAmountKeyboard = false
                }
            }
        }
    }

    val priceSheet = rememberModalBottomSheetState()
    if (showPriceKeyboard) {
        ModalBottomSheet(
            sheetState = priceSheet,
            onDismissRequest = { showPriceKeyboard = false }
        ) {
            KeyBoard("Цена", value = state.price) {
                onUIEvent(GodInfoUIEvent.OnPriceChanged(it))
                scope.launch {
                    priceSheet.hide()
                    showPriceKeyboard = false
                }
            }
        }
    }
}

@Composable
fun GodInfoScreenContent(
    amount: Float?,
    price: Float?,
    marga: Float?,
    addBtnEnabled: Boolean,
    godEntity: GodEntity?,
    orderHistory: List<Pair<String, GodOrderData>>?,
    historyName: String?,
    debtForCustomer: Double? = null,
    modifier: Modifier = Modifier,
    onAmountPressed: () -> Unit = {},
    onPricePressed: () -> Unit = {},
    onAddClicked: () -> Unit = {}
) {
    if (godEntity == null) {
        return
    }

    Box(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            Row(
                Modifier.height(72.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(64.dp),
                    painter = rememberVectorPainter(image = Icons.Outlined.Image),
                    contentDescription = null
                )

                Text(
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    text = godEntity.data.description.orEmpty()
                )
            }

            HorizontalDivider(thickness = 0.5.dp)

            Text(text = "На складе: ${godEntity.availableAmount}${godEntity.measureData?.name}")
            HorizontalDivider(thickness = 0.5.dp)

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                godEntity.price.forEachIndexed { index, it ->
                    Row {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1F),
                            text = "${it.priceTypeName}: ${it.priceValue} грн"
                        )

                        if (index == godEntity.price.size - 1 && marga != null) {
                            Text(text = "Р=$marga%")
                        }
                    }
                }
            }

            HorizontalDivider(thickness = 0.5.dp)

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Колличесство:", modifier = Modifier.weight(3F))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .defaultMinSize(72.dp)
                        .clickable {
                            onAmountPressed()
                        }
                        .border(
                            width = 0.5.dp,
                            color = MaterialTheme.colorScheme.surfaceTint,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = amount?.toString() ?: "",
                        maxLines = 1
                    )
                }
            }

            HorizontalDivider(thickness = 0.5.dp)

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Цена покупки: ", modifier = Modifier.weight(3F))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .defaultMinSize(72.dp)
                        .clickable {
                            onPricePressed()
                        }
                        .border(
                            width = 0.5.dp,
                            color = MaterialTheme.colorScheme.surfaceTint,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = price?.toString() ?: "",
                        maxLines = 1
                    )
                }
            }
            debtForCustomer?.let {
                HorizontalDivider(thickness = 0.5.dp)
                Row {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        text = "Текущий долг:"
                    )
                    Text(
                        text = debtForCustomer.format("грн"),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            historyName?.let {
                HorizontalDivider(thickness = 0.5.dp)
                Text(text = "История покупок для $historyName:")
                if (orderHistory.isNullOrEmpty()) {
                    Text(text = "Нет истории")
                } else {
                    for ((date, god) in orderHistory) {
                        Row {
                            Text(
                                modifier = Modifier.weight(1F), text = date.substring(0, 10)
                            )

                            Text(text = "${god.price} грн (${god.sum} грн / ${god.amount}${godEntity.measureData?.name})")
                        }
                    }
                }
            }
        }

        Button(modifier = Modifier.fillMaxWidth(),
            enabled = addBtnEnabled,
            onClick = { onAddClicked() }) {
            Text(
                modifier = Modifier.padding(6.dp), text = "Добавить"
            )
        }
    }
}

@Preview
@Composable
fun GodInfoScreenPreview() {
    TradeAssistTheme {
        GodInfoScreen(
            state = GodInfoViewState(
                price = 12.4F,
                amount = 123.3F,
                godsEntity = GodEntity(
                    GodsData().apply {
                        description =
                            "Длинное название товара для стройки и заметания двора. Супер дупер тряпк швабра. Ну очень длинно енвазиние"
                    },
                    measureData = MeasureData().apply {
                        name = "шт"
                    },
                    availableAmount = 123F,
                    price = listOf(PriceData().apply {
                        this.priceValue = 10.6F
                        this.priceTypeName = "Оптовая цена"
                    }, PriceData().apply {
                        this.priceValue = 11.6F
                        this.priceTypeName = "Актальная цена"
                    })
                ),
                orderHistory = buildList {
                    add("2024-03-28 16:57:09" to GodOrderData().apply {
                        price = 22F
                        amount = 133F
                        sum = 1233.0F
                    })

                    add("2024-03-28 16:57:09" to GodOrderData().apply {
                        price = 22F
                        amount = 133F
                        sum = 123.0F
                    })
                    add("2024-03-28 16:57:09" to GodOrderData().apply {
                        price = 22F
                        amount = 133F
                        sum = 333.0F
                    })
                },
            )
        )
    }
}