package com.simple.games.tradeassist.ui.loans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.ui.base.design.AppTopBar

@Composable
fun LoansScreen(
    viewState: LoansViewState,
    onUIEvent: (AppUIEvent) -> Unit = {}
) {
    Scaffold(topBar = {
        AppTopBar(title = R.string.loans,
            navigationIcon = R.drawable.ic_arrow_back,
            onNavigationClick = {
                onUIEvent(AppUIEvent.OnBackClicked)
            })
    }) {
        LoansScreenContent(modifier = Modifier.padding(it))
    }
}

@Composable
fun LoansScreenContent(modifier: Modifier) {
    Column(modifier = modifier) {
        HorizontalDivider(thickness = 0.5.dp)
    }
}

@Preview
@Composable
fun PreviewLoandsScreen() {
    TradeAssistTheme {
        LoansScreen(LoansViewState(2))
    }
}