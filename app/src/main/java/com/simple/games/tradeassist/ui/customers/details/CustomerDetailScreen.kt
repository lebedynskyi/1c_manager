package com.simple.games.tradeassist.ui.customers.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.format
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.ui.base.design.AppTopBar
import com.simple.games.tradeassist.ui.base.design.ContentLoadingContainer

@Composable
fun CustomerDetailScreen(
    viewState: CustomerDetailViewState,
    onUIEvent: (AppUIEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppTopBar(title = R.string.customer,
                navigationIcon = R.drawable.ic_arrow_back,
                onNavigationClick = {
                    onUIEvent(AppUIEvent.OnBackClicked)
                })
        }
    ) { padding ->
        ContentLoadingContainer(
            modifier = Modifier.padding(padding),
            isContentInProgress = viewState.contentInProgress
        ) {
            CustomerDetailScreenContent(
                viewState,
                onUIEvent = onUIEvent
            )
        }
    }
}

@Composable
fun CustomerDetailScreenContent(
    viewState: CustomerDetailViewState,
    modifier: Modifier = Modifier,
    onUIEvent: (CustomerDetailUIEvent) -> Unit = {}
) {
    Column(modifier = modifier) {
        HorizontalDivider()

        Spacer(Modifier.size(24.dp))

        Text(
            style = MaterialTheme.typography.titleMedium,
            text = viewState.customerData?.description.orEmpty(),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.size(32.dp))

//        HorizontalDivider()
//        Row(modifier = Modifier.padding(horizontal = 12.dp).height(48.dp), verticalAlignment = Alignment.CenterVertically) {
//            Text(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f),
//                text = "Увеличение долга: ",
//            )
//            Text(
//                text = viewState.debtEntity?.totalPlus.format("грн"),
//            )
//        }
//        HorizontalDivider()
//
//        Row(modifier = Modifier.padding(horizontal = 12.dp).height(48.dp), verticalAlignment = Alignment.CenterVertically) {
//            Text(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f),
//                text = "Уменьшение долга:"
//            )
//            Text(
//                text = viewState.debtEntity?.totalMinus.format("грн"),
//            )
//        }
//        HorizontalDivider()

        Row(modifier = Modifier.padding(horizontal = 12.dp).height(48.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = "Долг сейчас:",
            )
            Text(
                text = ((viewState.debtEntity?.totalPlus ?: 0.0) - (viewState.debtEntity?.totalMinus
                    ?: 0.0)).format("грн"),
            )
        }
        HorizontalDivider()
    }
}

@Preview
@Composable
fun PreviewCustomerDetailScreen() {
    TradeAssistTheme {
        CustomerDetailScreen(
            CustomerDetailViewState(
                customerData = CustomerData().apply {
                    description = "ФОП Галина Петросян"
                }
            )
        )
    }
}