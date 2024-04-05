@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.simple.games.tradeassist.ui.gods.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Expand
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.PriceData
import com.simple.games.tradeassist.domain.GodEntity
import com.simple.games.tradeassist.ui.base.design.AppTopBar
import com.simple.games.tradeassist.ui.base.design.ContentLoadingContainer
import com.simple.games.tradeassist.ui.base.design.verticalScrollbar
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate

@Composable
fun GodsSelectionScreen(
    state: GodsSelectionViewState,
    onUIEvent: (AppUIEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = R.string.gods_title,
                navigationIcon = R.drawable.ic_arrow_back,
                onNavigationClick = {
                    onUIEvent(AppUIEvent.OnBackClicked)
                },
                menus = {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { onUIEvent(GodsSelectionUIEvent.OnCollapseClick) },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberVectorPainter(image = Icons.Outlined.Expand),
                            contentDescription = null
                        )
                    }
                    val size = state.orderTemplates?.size
                    if (size != null && size > 0) {
                        Box(
                            modifier = Modifier
                                .height(48.dp)
                                .clickable { onUIEvent(GodsSelectionUIEvent.OnDoneClick) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Добавить($size)")
                        }
                    }
                })
        }
    ) {
        GodsSelectionScreenContent(
            state.contentInProgress,
            filterQuery = state.filterQuery,
            godsList = state.godsList,
            ordersList = state.orderTemplates,
            modifier = Modifier.padding(it),
            showAll = state.showAll,
            onQueryChanged = { onUIEvent(GodsSelectionUIEvent.OnFilterQueryChanged(it)) },
            onGodsClicked = { onUIEvent(GodsSelectionUIEvent.OnGodsClicked(it)) },
            onShowAllChanged = { onUIEvent(GodsSelectionUIEvent.OnShowAllToggleChanged(it)) }
        )
        HorizontalDivider(modifier = Modifier.padding(it), thickness = 0.5.dp)
    }
}

@Composable
fun GodsSelectionScreenContent(
    contentInProgress: Boolean,
    godsList: List<TreeNode>,
    ordersList: List<GodOrderTemplate>?,
    filterQuery: String,
    modifier: Modifier = Modifier,
    showAll: Boolean = false,
    onQueryChanged: (String) -> Unit,
    onGodsClicked: (TreeNode) -> Unit,
    onShowAllChanged: (Boolean) -> Unit,
) {
    var refreshTrigger by remember { mutableIntStateOf(0) }
    val scrollState = rememberLazyListState()
    System.err.println("Trigger -> $refreshTrigger, list size -> ${godsList.size}")

    ContentLoadingContainer(
        isContentInProgress = contentInProgress,
        modifier = modifier
    ) {
        Column(Modifier.fillMaxSize()) {
            OutlinedTextField(
                placeholder = {
                    Text(text = stringResource(id = R.string.filter))
                },
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                value = filterQuery, onValueChange = onQueryChanged
            )
            HorizontalDivider(thickness = 0.5.dp)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1F),
                    text = "Показать ВСЕ"
                )
                Switch(modifier = Modifier.padding(horizontal = 12.dp),
                    checked = showAll, onCheckedChange = {
                        onShowAllChanged(it)
                    })
            }
            HorizontalDivider(thickness = 0.5.dp)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScrollbar(scrollState),
                state = scrollState
            ) {
                for (t in godsList) {
                    TreeBranch(t, orders = ordersList) {
                        if (it.content.data.isFolder) {
                            it.expanded = !it.expanded
                            refreshTrigger = refreshTrigger.inc()
                        } else {
                            onGodsClicked(it)
                        }
                    }
                }
            }
        }
    }
}

fun LazyListScope.TreeBranch(
    node: TreeNode,
    orders: List<GodOrderTemplate>?,
    shift: Int = 0,
    onItemClick: (TreeNode) -> Unit,
) {
    val existedOrder = orders?.firstOrNull { it.godEntity.data.refKey == node.content.data.refKey }
    TreeLeaf(node, existedOrder, shift, onItemClick)

    if (node.expanded) {
        for (f in node.children) {
            TreeBranch(f, orders, shift.inc(), onItemClick)
        }
    }
}

fun LazyListScope.TreeLeaf(
    node: TreeNode,
    order: GodOrderTemplate?,
    shift: Int,
    onItemClick: (TreeNode) -> Unit,
) {
    val shiftSize = 12 * shift
    item {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height((56 + if (order != null) 24 else 0).dp)
                .clickable { onItemClick(node) },
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (shift > 0) {
                        Spacer(modifier = Modifier.size((shiftSize).dp))
                    }
                    if (node.content.data.isFolder) {
                        if (node.expanded) {
                            Image(
                                modifier = Modifier.size(20.dp),
                                painter = rememberVectorPainter(image = Icons.Outlined.KeyboardArrowDown),
                                contentDescription = null,
                            )
                        } else {
                            Image(
                                modifier = Modifier.size(20.dp),
                                painter = rememberVectorPainter(image = Icons.AutoMirrored.Outlined.KeyboardArrowRight),
                                contentDescription = null,
                            )
                        }

                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = rememberVectorPainter(image = Icons.Outlined.Folder),
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                            text = node.content.data.description.orEmpty()
                        )
                    } else {
                        Spacer(
                            modifier = Modifier.size(20.dp),
                        )
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = rememberVectorPainter(image = Icons.AutoMirrored.Outlined.InsertDriveFile),
                            contentDescription = null,
                        )

                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                            text = node.content.data.description.orEmpty()
                        )
                        Column {
                            Text(text = "${node.content.availableAmount} ${node.content.measureData?.name ?: ""}")
                            Text(text = "${node.content.availableAmount} ${node.content.measureData?.name ?: ""}")
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                }

                if (order != null) {
                    Text(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = (24 + shiftSize).dp),
                        text = "В заказe ${order.amount} ${order.godEntity.measureData?.name ?: ""} по цене ${order.price} грн"
                    )
                }
            }

            HorizontalDivider(thickness = 0.5.dp)
        }
    }
}

@Preview
@Composable
fun PreviewGodSelectionScreen() {
    TradeAssistTheme {
        GodsSelectionScreen(
            state = GodsSelectionViewState(
                godsList = listOf(
                    TreeNode(
                        GodEntity(GodsData().apply {
                            description = "Папка 1"
                            isFolder = true
                        }), expanded = true, children = mutableListOf(
                            TreeNode(GodEntity(GodsData().apply {
                                description = "Файл 1"
                            }, price = listOf(PriceData().apply { priceTypeName = "Оптовая цена" }, PriceData().apply { priceTypeName = "Розничная цена" }))), TreeNode(GodEntity(GodsData().apply {
                                description = "Файл 2"
                            })), TreeNode(GodEntity(GodsData().apply {
                                description = "Файл 3"
                            })), TreeNode(GodEntity(GodsData().apply {
                                description = "Файл 4"
                            }))
                        )
                    )
                )
            )
        )
    }
}