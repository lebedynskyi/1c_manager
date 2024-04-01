@file:OptIn(ExperimentalMaterial3Api::class)

package com.simple.games.tradeassist.ui.order.create

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.ui.base.design.AppTopBar
import com.simple.games.tradeassist.ui.base.design.ContentLoadingIndicator
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate

@Composable
fun CreateOrderScreen(
    state: CreateOrderViewState,
    onUIEvent: (AppUIEvent) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = R.string.create_order,
                navigationIcon = R.drawable.ic_arrow_back,
                onNavigationClick = {
                    onUIEvent(AppUIEvent.OnBackClicked)
                })
        }
    ) {
        HorizontalDivider(modifier = Modifier.padding(it), thickness = 0.5.dp)
        CreateOrderScreenContent(
            state.customerName,
            customers = state.filteredCustomers,
            addGodsEnabled = state.addGodsEnabled,
            gods = state.orderTemplates,
            modifier = Modifier.padding(it),
            onAddGods = { onUIEvent(CreateOrderUIEvent.OnAddGods) },
            onPublishOrder = { onUIEvent(CreateOrderUIEvent.Publish) },
            onCustomerNameChanged = remember {
                {
                    onUIEvent(
                        CreateOrderUIEvent.OnCustomerNameChange(
                            it
                        )
                    )
                }
            },
            onDismissDropDown = remember { { onUIEvent(CreateOrderUIEvent.OnDismissCustomerDropDown) } },
            onCustomerSelected = remember { { onUIEvent(CreateOrderUIEvent.OnCustomerSelected(it)) } },
            onRemoveGod = remember { { onUIEvent(CreateOrderUIEvent.OnGodRemoveClicked(it)) } },
            onEditGod = remember { { onUIEvent(CreateOrderUIEvent.OnGodEditClick(it)) } }
        )
    }

    ContentLoadingIndicator(show = state.refreshInProgress)
}


@ExperimentalMaterial3Api
@Composable
fun CreateOrderScreenContent(
    customerInput: String,
    gods: List<GodOrderTemplate>?,
    addGodsEnabled: Boolean,
    customers: List<CustomerData>,
    modifier: Modifier = Modifier,
    onAddGods: () -> Unit,
    onPublishOrder: () -> Unit,
    onCustomerNameChanged: (String) -> Unit,
    onDismissDropDown: () -> Unit,
    onCustomerSelected: (CustomerData) -> Unit,
    onRemoveGod: (GodOrderTemplate) -> Unit,
    onEditGod: (GodOrderTemplate) -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .scrollable(
                rememberScrollState(),
                orientation = Orientation.Vertical
            )
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = customers.isNotEmpty(),
            onExpandedChange = {
                if (!it) onDismissDropDown()
            }) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),

                value = TextFieldValue(customerInput, selection = TextRange(customerInput.length)),
                onValueChange = {
                    onCustomerNameChanged(it.text)
                }, placeholder = {
                    Text(text = stringResource(id = R.string.customer))
                }, label = {
                    Text(text = stringResource(id = R.string.customer))
                })

            ExposedDropdownMenu(
                focusable = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .exposedDropdownSize(),
                expanded = customers.isNotEmpty(),
                onDismissRequest = {
                    System.err.println("DISMISS REQUST")
                },
            ) {
                customers.forEachIndexed { index, customer ->
                    DropdownMenuItem(text = {
                        Column(
                            modifier = Modifier.height(48.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                                text = customer.description.orEmpty()
                            )

                            if (index != customers.size - 1) {
                                HorizontalDivider(thickness = 0.5.dp)
                            }
                        }
                    }, onClick = {
                        onCustomerSelected(customer)
                    })
                }
            }
        }

        Spacer(Modifier.size(12.dp))
        HorizontalDivider(modifier = Modifier.padding(), thickness = 0.5.dp)
        Spacer(Modifier.size(12.dp))

        if (gods.isNullOrEmpty()) {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.no_gods)
            )
            Spacer(Modifier.size(24.dp))
            Button(modifier = Modifier.fillMaxWidth(),
                enabled = addGodsEnabled,
                onClick = { onAddGods() }) {
                Text(text = stringResource(id = R.string.add_god))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Text(text = stringResource(id = R.string.gods))
                }
                items(gods.size, key = { gods[it].godsData.refKey }) { index ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            val god = gods[index]
                            Text(
                                text = god.godsData.description.orEmpty(),
                                style = MaterialTheme.typography.titleLarge
                            )
                            HorizontalDivider(thickness = 0.5.dp)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(text = "Колличесство:", modifier = Modifier.weight(1F))

                                Text(
                                    modifier = Modifier.padding(12.dp),
                                    textAlign = TextAlign.Center,
                                    text = "${god.amount} ${god.godsData.measure?.name}",
                                )
                            }

                            HorizontalDivider(thickness = 0.5.dp)

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(text = "Цена покупки: ", modifier = Modifier.weight(3F))
                                Text(
                                    modifier = Modifier.padding(12.dp),
                                    textAlign = TextAlign.Center,
                                    text = "${god.price} грн",
                                )
                            }

                            HorizontalDivider(thickness = 0.5.dp)

                            Spacer(Modifier.size(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                                Button(
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        onRemoveGod(god)
                                    }) {
                                    Text(
                                        color = MaterialTheme.colorScheme.onError,
                                        text = stringResource(id = R.string.delete_order_template)
                                    )
                                }
                                Button(
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        onEditGod(god)
                                    }) {
                                    Text(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        text = stringResource(id = R.string.edit_order_template)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = addGodsEnabled,
                        onClick = { onAddGods() }) {
                        Text(text = stringResource(id = R.string.add_god))
                    }
                }

                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = addGodsEnabled,
                        onClick = { onPublishOrder() }) {
                        Text(text = "Опубликовать")
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewCreateOrder() {
    TradeAssistTheme {
        CreateOrderScreen(
            state = CreateOrderViewState(
                orderTemplates = buildList {
                    add(GodOrderTemplate(CustomerData(), GodsData().apply {
                        refKey = "122"
                        description = "Кабель 2х234"
                    }, 2F, 6F))

                    add(GodOrderTemplate(CustomerData(), GodsData().apply {
                        refKey = "123"
                        description = "Кабель ЩВВП длинное описание 300 на 500 мметров 0.5"
                    }, 2F, 6F))
                }
            )
        ) {

        }
    }
}