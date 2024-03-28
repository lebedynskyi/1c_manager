package com.simple.games.tradeassist.ui.gods.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.dexter.ui.base.AppUIEvent
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.GodOrderData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.ui.base.design.AppTopBar
import com.simple.games.tradeassist.ui.base.design.ContentLoadingIndicator

@Composable
fun GodInfoScreen(
    state: GodInfoViewState,
    onUIEvent: (AppUIEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppTopBar(title = R.string.god_info, leftIcon = R.drawable.ic_arrow_back) {
                onUIEvent(AppUIEvent.OnBackClicked)
            }
        }
    ) {
        GodInfoScreenContent(
            state.godsData,
            state.orderHistory,
            modifier = Modifier.padding(it)
        )
    }

    ContentLoadingIndicator(show = state.requestInProgress)
}

@Composable
fun GodInfoScreenContent(
    godsData: GodsData?,
    orderHistory: List<Pair<String, GodOrderData>>?,
    modifier: Modifier
) {
    if (godsData == null) {
        return
    }

    var amount by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Box(
        modifier = modifier.padding(horizontal = 12.dp).padding(bottom = 24.dp),
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
                    text = godsData.description.orEmpty()
                )
            }

            HorizontalDivider(thickness = 0.5.dp)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "На складе: ${godsData.amount}${godsData.measure?.name}")
                Text(text = "Базовая цена: ???")
            }

            HorizontalDivider(thickness = 0.5.dp)

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Колличесство:", modifier = Modifier.weight(3F))
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    value = amount,
                    onValueChange = {
                        amount = it
                    })
            }

            HorizontalDivider(thickness = 0.5.dp)

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Цена покупки: ", modifier = Modifier.weight(3F))
                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    value = price, onValueChange = {
                        price = it
                    })

            }

            HorizontalDivider(thickness = 0.5.dp)
            Text(text = "История ФОП Стас Артомонов центральный рынок")

            for ((date, god) in orderHistory.orEmpty()) {
                Row {
                    Text(
                        modifier = Modifier.weight(1F),
                        text = date.substring(0, 10)
                    )

                    Text(text = "${god.price} грн (${god.sum} грн / ${god.amount})")
                }
            }
        }
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ }) {
            Text(
                modifier = Modifier.padding(6.dp),
                text = "Добавить"
            )
        }
    }
}

@Preview
@Composable
fun GodInfoScreenPreview() {
    TradeAssistTheme {
        GodInfoScreen(state = GodInfoViewState(
            godsData = GodsData().apply {
                amount = 123F
                measure = MeasureData().apply { this.name = "м" }
                description =
                    "Длинное название товара для стройки и заметания двора. Супер дупер тряпк швабра. Ну очень длинно енвазиние"
            },
            orderHistory = buildList {
                add("2024-03-28 16:57:09" to GodOrderData().apply {
                    price = 22F
                    amount = 133
                    sum = 12333.0F
                })

                add("2024-03-28 16:57:09" to GodOrderData().apply {
                    price = 22F
                    amount = 133
                    sum = 12333.0F
                })
                add("2024-03-28 16:57:09" to GodOrderData().apply {
                    price = 22F
                    amount = 133
                    sum = 12333.0F
                })
            }
        ))
    }
}