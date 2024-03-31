@file:OptIn(ExperimentalMaterial3Api::class)

package com.simple.games.tradeassist.ui.base.design

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.theme.TradeAssistTheme

@Composable
fun AppTopBar(
    @StringRes title: Int,
    @DrawableRes navigationIcon: Int? = null,
    onNavigationClick: (() -> Unit)? = null,
    menus: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(text = stringResource(title))
        },
        navigationIcon = {
            if (navigationIcon != null) {
                Box(modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        onNavigationClick?.invoke()
                    }, contentAlignment = Alignment.Center) {
                    Image(
                        colorFilter = ColorFilter.tint(TopAppBarDefaults.topAppBarColors().titleContentColor),

                        painter = painterResource(id = navigationIcon),
                        contentDescription = null
                    )
                }
            }
        },
        actions = menus
    )
}

@Composable
@Preview
fun PreviewAppTopBar() {
    TradeAssistTheme {
        AppTopBar(title = R.string.orders)
    }
}

@Composable
@Preview
fun PreviewAppTopBarWithIcon() {
    TradeAssistTheme {
        AppTopBar(title = R.string.orders, R.drawable.ic_arrow_back)
    }
}