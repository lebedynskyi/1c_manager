package com.simple.games.tradeassist.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import com.simple.games.tradeassist.ui.base.design.AppTopBar
import com.simple.games.tradeassist.ui.base.design.ContentLoadingIndicator

@Composable
fun MainScreen(
    state: MainViewState, onUIEvent: (AppUIEvent) -> Unit
) {
    Scaffold(topBar = {
        AppTopBar(R.string.main)
    }) {
        MainScreenContent(modifier = Modifier.padding(it),
            onOrderClick = { onUIEvent(MainUIEvent.OnOrderClick) },
            onGodsClick = { onUIEvent(MainUIEvent.OnGodsClick) },
            onCustomersClicked = { onUIEvent(MainUIEvent.OnCustomersClicked) },
            onSyncClicked = { onUIEvent(MainUIEvent.OnSyncClicked) })
    }

    ContentLoadingIndicator(show = state.requestInProgress, "Загрузка базы данных 1С")
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    onOrderClick: () -> Unit,
    onGodsClick: () -> Unit,
    onCustomersClicked: () -> Unit,
    onSyncClicked: () -> Unit
) {
    Box {
        Column(
            verticalArrangement = Arrangement.Top, modifier = modifier
                .padding(24.dp)
                .fillMaxSize()
                .fillMaxWidth()
        ) {
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), onClick = { onOrderClick() }) {
                Text(text = stringResource(R.string.orders))
            }
            Spacer(modifier = Modifier.size(24.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), onClick = { onGodsClick() }) {
                Text(text = stringResource(R.string.gods_title))
            }
            Spacer(modifier = Modifier.size(24.dp))

            Button(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), onClick = { onCustomersClicked() }) {
                Text(text = stringResource(R.string.customers))
            }
        }

        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = modifier
                .padding(24.dp)
                .fillMaxSize()
                .fillMaxWidth()
        ) {
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), onClick = { onSyncClicked() }) {
                Text(text = stringResource(R.string.full_sync))
            }
        }
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    TradeAssistTheme {
        MainScreen(MainViewState(false)) {

        }
    }
}