@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.simple.games.tradeassist.ui.order.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.PriceData
import com.simple.games.tradeassist.data.api.response.ResponsibleData
import com.simple.games.tradeassist.domain.entity.GodEntity
import com.simple.games.tradeassist.ui.base.design.AppTopBar
import com.simple.games.tradeassist.ui.base.design.ContentLoadingIndicator
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate

@Composable
fun CreateOrderScreen(
    state: CreateOrderViewState,
    onUIEvent: (AppUIEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = R.string.creation_order,
                navigationIcon = R.drawable.ic_arrow_back,
                onNavigationClick = {
                    onUIEvent(AppUIEvent.OnBackClicked)
                }, menus = {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { onUIEvent(CreateOrderUIEvent.SaveOrder) },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberVectorPainter(image = Icons.Outlined.Done),
                            contentDescription = null
                        )
                    }
                })
        }
    ) {
        HorizontalDivider(modifier = Modifier.padding(it), thickness = 0.5.dp)
        CreateOrderScreenContent(
            state.customerName,
            responsbleName = state.responsibleName,
            customers = state.filteredCustomers,
            addGodsEnabled = state.addGodsEnabled,
            gods = state.orderTemplates,
            responsibleList = state.responsible,
            comment = state.orderComment,
            modifier = Modifier.padding(it),
            onAddGods = { onUIEvent(CreateOrderUIEvent.OnAddGods) },
            onCommentChanged = {onUIEvent(CreateOrderUIEvent.OnCommentChanged(it))},
            onCustomerNameChanged = { onUIEvent(CreateOrderUIEvent.OnCustomerNameChange(it)) },
            onDismissDropDown = { onUIEvent(CreateOrderUIEvent.OnDismissCustomerDropDown) },
            onCustomerSelected = { onUIEvent(CreateOrderUIEvent.OnCustomerSelected(it)) },
            onRemoveGod = { onUIEvent(CreateOrderUIEvent.OnGodRemoveClicked(it)) },
            onEditGod = { onUIEvent(CreateOrderUIEvent.OnGodEditClick(it)) },
            onResponsibleSelected = { onUIEvent(CreateOrderUIEvent.OnResponsibleSelected(it)) }
        )
    }

    ContentLoadingIndicator(show = state.refreshInProgress)
}


@ExperimentalMaterial3Api
@Composable
fun CreateOrderScreenContent(
    customerInput: String,
    responsbleName: String?,
    gods: List<GodOrderTemplate>?,
    addGodsEnabled: Boolean,
    comment: String?,
    customers: List<CustomerData>,
    responsibleList: List<ResponsibleData>?,
    modifier: Modifier = Modifier,
    onAddGods: () -> Unit,
    onCommentChanged: (String) -> Unit,
    onCustomerNameChanged: (String) -> Unit,
    onDismissDropDown: () -> Unit,
    onCustomerSelected: (CustomerData) -> Unit,
    onRemoveGod: (GodOrderTemplate) -> Unit,
    onEditGod: (GodOrderTemplate) -> Unit,
    onResponsibleSelected: (ResponsibleData) -> Unit
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
        CustomersDropDownMenu(
            customers,
            customerInput,
            onCustomerNameChanged,
            onDismissDropDown,
            onCustomerSelected
        )

        Spacer(modifier = Modifier.size(24.dp))

        ResponsibleDropDownMenu(
            responsbleName,
            responsibleList,
            onResponsibleSelected
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = comment.orEmpty(),
            onValueChange = { onCommentChanged(it) },
            placeholder = {
                Text(text = stringResource(id = R.string.comment))
            }, label = {
                Text(text = stringResource(id = R.string.comment))
            })


        Spacer(Modifier.size(12.dp))
        HorizontalDivider(modifier = Modifier.padding(), thickness = 0.5.dp)
        Spacer(Modifier.size(12.dp))

        if (gods.isNullOrEmpty()) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.no_gods)
                )
                Spacer(Modifier.size(12.dp))
                Button(enabled = addGodsEnabled,
                    onClick = { onAddGods() }) {
                    Text(text = stringResource(id = R.string.add_god))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Text(text = stringResource(id = R.string.gods))
                }
                items(gods.size, key = { gods[it].godEntity.data.refKey }) { index ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            val god = gods[index]
                            Text(
                                text = god.godEntity.data.description.orEmpty(),
                                style = MaterialTheme.typography.titleLarge
                            )
                            HorizontalDivider(thickness = 0.5.dp)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(text = "Колличесство:", modifier = Modifier.weight(1F))

                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    textAlign = TextAlign.Center,
                                    text = "${god.amount} ${god.godEntity.measureData?.name}",
                                )
                            }

                            HorizontalDivider(thickness = 0.5.dp)

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(text = "Цена покупки: ", modifier = Modifier.weight(3F))
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    textAlign = TextAlign.Center,
                                    text = "${god.price} грн",
                                )
                            }

                            HorizontalDivider(thickness = 0.5.dp)

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(text = "Вся сумма: ", modifier = Modifier.weight(3F))
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    textAlign = TextAlign.Center,
                                    text = "${god.sum} грн",
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
            }
        }
    }
}

@Composable
private fun ResponsibleDropDownMenu(
    responsibleName: String?,
    responsibleList: List<ResponsibleData>?,
    onResponsibleSelected: (ResponsibleData) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = it })
    {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .defaultMinSize(minHeight = OutlinedTextFieldDefaults.MinHeight)
                .fillMaxWidth()
                .clickable { expanded = true }
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .border(
                    OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                    MaterialTheme.colorScheme.outline,
                    shape = OutlinedTextFieldDefaults.shape
                )
        ) {
            Text(
                modifier = Modifier.padding(start = 12.dp),
                color = if (responsibleName.isNullOrBlank()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                text = if (responsibleName.isNullOrBlank()) "Назначить ответственного" else "Ответственный: $responsibleName"
            )
        }

        ExposedDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .exposedDropdownSize(),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            responsibleList?.forEachIndexed { index, responsible ->
                DropdownMenuItem(text = {
                    Column(
                        modifier = Modifier.height(48.dp)
                    ) {
                        Column(
                            Modifier
                                .weight(1F)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = responsible.name.orEmpty())
                        }

                        if (index != responsibleList.size - 1) {
                            HorizontalDivider(thickness = 0.5.dp)
                        }
                    }
                }, onClick = {
                    expanded = false
                    onResponsibleSelected(responsible)
                })
            }
        }
    }
}

@Composable
private fun CustomersDropDownMenu(
    suggestingCustomers: List<CustomerData>,
    customerInput: String,
    onCustomerNameChanged: (String) -> Unit,
    onDismissDropDown: () -> Unit,
    onCustomerSelected: (CustomerData) -> Unit,
) {
    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = suggestingCustomers.isNotEmpty(),
        onExpandedChange = { if (!it) onDismissDropDown() }) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable),
            value = customerInput,
            onValueChange = { onCustomerNameChanged(it) },
            placeholder = {
                Text(text = stringResource(id = R.string.customer))
            }, label = {
                Text(text = stringResource(id = R.string.chose_customer))
            })

        ExposedDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .exposedDropdownSize(),
            expanded = suggestingCustomers.isNotEmpty(),
            onDismissRequest = {},
        ) {
            suggestingCustomers.forEachIndexed { index, customer ->
                DropdownMenuItem(text = {
                    Column {
                        Column(
                            modifier = Modifier.height(48.dp)
                        ) {
                            Column(
                                Modifier
                                    .weight(1F)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = customer.description.orEmpty())
                            }

                            if (index != suggestingCustomers.size - 1) {
                                HorizontalDivider(thickness = 0.5.dp)
                            }
                        }
                    }
                }, onClick = {
                    onCustomerSelected(customer)
                })
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
                filteredCustomers = listOf(CustomerData().apply { description = "Helo world" }),
                orderTemplates = buildList {
                    add(
                        GodOrderTemplate(
                            GodEntity(
                            data = GodsData().apply {
                                refKey = "122"
                                description = "Кабель 2х234"
                            },
                            measureData = MeasureData().apply {
                                name = "шт"
                            }, price = listOf(PriceData().apply {
                                this.priceValue = 10.6F
                                this.priceTypeName = "Оптовая цена"
                            }, PriceData().apply {
                                this.priceValue = 11.6F
                                this.priceTypeName = "Актальная цена"
                            }), availableAmount = 6F
                        ),
                            amount = 213F, price = 12313F
                        )
                    )
                }
            )
        )
    }
}