package com.simple.games.tradeassist.ui.customers.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.ui.base.design.AppTopBar
import kotlinx.coroutines.launch

@Composable
fun CustomersScreen(
    viewState: CustomersViewState,
    onUIEvent: (AppUIEvent) -> Unit = {}
) {
    Scaffold(topBar = {
        AppTopBar(title = R.string.customers,
            navigationIcon = R.drawable.ic_arrow_back,
            onNavigationClick = {
                onUIEvent(AppUIEvent.OnBackClicked)
            })
    }) { padding ->
        LoansScreenContent(
            viewState.customers,
            modifier = Modifier.padding(padding),
            onCustomerClicked = {
                onUIEvent(CustomersUIEvent.OnCustomerClicked(it))
            }
        )
    }
}

@Composable
fun LoansScreenContent(
    customers: List<CustomerData>,
    modifier: Modifier,
    onCustomerClicked: (CustomerData) -> Unit,
) {
    var input by remember {
        mutableStateOf("")
    }

    var filtered by remember {
        mutableStateOf(customers)
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        TextField(
            value = input,
            label = { Text(text = "Поиск...") },
            onValueChange = {
                input = it
                scope.launch {
                    filtered = customers.filter { it.description?.contains(input, true) == true }
                }
            },
            trailingIcon = {
                Image(
                    rememberVectorPainter(Icons.Filled.Close), contentDescription = null,
                    modifier = Modifier.clickable {
                        input = ""
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        CustomersList(
            if (input.isEmpty()) customers else filtered,
            onCustomerClicked
        )
    }
}

@Composable
fun CustomersList(
    customers: List<CustomerData>,
    onCustomerClicked: (CustomerData) -> Unit
) {
    LazyColumn {
        items(customers.size, key = { customers[it].refKey }) {
            val customer = customers[it]
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onCustomerClicked(customer) }
                    .height(52.dp)
            ) {
                Text(
                    text = customer.description.orEmpty(),
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                )
            }
            HorizontalDivider(thickness = 0.5.dp)
        }
    }
}

@Preview
@Composable
fun PreviewCustomerScreen() {
    TradeAssistTheme {
        CustomersScreen(
            CustomersViewState(
                listOf(
                    CustomerData().apply {
                        refKey = "key1"
                        description = "Hellom 1"
                    },
                    CustomerData().apply {
                        refKey = "key2"
                        description = "ФОП Красава Бла бла"
                    },
                    CustomerData().apply {
                        refKey = "key3"
                        description = "ФОП торгаш на рынке возле шаурмы"
                    },
                )
            )
        )
    }
}