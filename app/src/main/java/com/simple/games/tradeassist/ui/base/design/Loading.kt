@file:OptIn(ExperimentalMaterial3Api::class)

package com.simple.games.tradeassist.ui.base.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ContentLoadingIndicator(show: Boolean) {
    if (!show) {
        return
    }

    Dialog(
        onDismissRequest = { },
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(Color.Transparent)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                strokeWidth = 4.dp,
            )
        }
    }
}

@Composable
fun ContentLoadingContainer(
    modifier: Modifier = Modifier,
    isContentInProgress: Boolean = false,
    isRefreshEnabled: Boolean = false,
    isRefreshInProgress: Boolean = false,
    onPullToRefresh: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState(enabled = { isRefreshInProgress })

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        content.invoke(this)

        // TODO not working
        if (isRefreshEnabled) {
            PullToRefreshContainer(
                modifier = Modifier.fillMaxSize(),
                state = pullToRefreshState
            )
        }

        if (isContentInProgress) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Transparent,
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        strokeWidth = 4.dp,
                    )
                }
            }
        }
    }
}
