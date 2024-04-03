package com.simple.games.tradeassist.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.ui.base.design.AppTopBar
import com.simple.games.tradeassist.ui.base.design.ContentLoadingIndicator

@Composable
fun MainScreen(
    state: MainViewState,
    onUIEvent: (AppUIEvent) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(R.string.main)
        }
    ) {
        MainScreenContent(
            modifier = Modifier.padding(it),
            onOrderClick = { onUIEvent(MainUIEvent.OnOrderClick) },
            onGodsClick = { onUIEvent(MainUIEvent.OnGodsClick) }
        )
    }

    ContentLoadingIndicator(show = state.requestInProgress, "Загрузка базы данных 1С")
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    onOrderClick: () -> Unit,
    onGodsClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = { onOrderClick() }
        ) {
            Text(text = stringResource(R.string.orders))
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = { onGodsClick() }
        ) {
            Text(text = stringResource(R.string.gods_title))
        }
    }
}