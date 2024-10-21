@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)

package com.simple.games.tradeassist.ui.gods.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
    var searchActive by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = R.string.gods_title,
                navigationIcon = R.drawable.ic_arrow_back,
                onNavigationClick = {
                    if (searchActive) {
                        searchActive = false
                    } else {
                        onUIEvent(AppUIEvent.OnBackClicked)
                    }
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
                })
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SearchBar(
                modifier = Modifier.padding(it),
                query = state.filterQuery,
                placeholder = { Text(text = "Поиск товаров") },
                onQueryChange = { onUIEvent(GodsSelectionUIEvent.OnFilterQueryChanged(it)) },
                onSearch = { },
                active = searchActive,
                onActiveChange = { searchActive = it }
            ) {
                LazyColumn {
                    state.filteredContent?.forEach { node ->
                        item {
                            val existedOrder =
                                state.orderTemplates?.firstOrNull { it.godEntity.data.refKey == node.content.data.refKey }
                            TreeLeafComposable(node, existedOrder, 0) {
                                onUIEvent(GodsSelectionUIEvent.OnGodsClicked(it))
                            }
                        }
                    }
                }
            }

            GodsSelectionScreenContent(
                state.contentInProgress,
                godsList = state.godsList,
                ordersList = state.orderTemplates,
                showAll = state.showAll,
                onGodsClicked = { onUIEvent(GodsSelectionUIEvent.OnGodsClicked(it)) },
                onShowAllChanged = { onUIEvent(GodsSelectionUIEvent.OnShowAllToggleChanged(it)) },
                onAddClicked = { onUIEvent(GodsSelectionUIEvent.OnDoneClick) }
            )
        }
        HorizontalDivider(modifier = Modifier.padding(it), thickness = 0.5.dp)
    }
}

@Composable
fun GodsSelectionScreenContent(
    contentInProgress: Boolean,
    godsList: List<TreeNode>,
    ordersList: List<GodOrderTemplate>?,
    modifier: Modifier = Modifier,
    showAll: Boolean = false,
    onGodsClicked: (TreeNode) -> Unit,
    onShowAllChanged: (Boolean) -> Unit,
    onAddClicked: () -> Unit,
) {
    var refreshTrigger by remember { mutableIntStateOf(0) }
    val scrollState = rememberLazyListState()

    ContentLoadingContainer(
        isContentInProgress = contentInProgress,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(Modifier.size(12.dp))

            HorizontalDivider(thickness = 0.5.dp)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Показать ВСЕ",
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1F)
                )
                Switch(modifier = Modifier.padding(horizontal = 12.dp),
                    checked = showAll, onCheckedChange = {
                        onShowAllChanged(it)
                    })
            }
            HorizontalDivider(thickness = 0.5.dp)
            Box(contentAlignment = Alignment.BottomCenter) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScrollbar(scrollState),
                    state = scrollState
                ) {
                    System.err.println("Trigger -> $refreshTrigger, list size -> ${godsList.size}")

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

                    item {
                        Spacer(modifier = Modifier.size(64.dp))
                    }
                }

                val size = ordersList?.size ?: 0
                if (size > 0) {
                    Button(modifier = Modifier.padding(bottom = 12.dp),
                        onClick = {
                            onAddClicked()
                        }) {
                        Text(text = "Добавить в заказ ($size)")
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

@Composable
fun TreeLeafComposable(
    node: TreeNode,
    order: GodOrderTemplate?,
    shift: Int = 0,
    onItemClick: (TreeNode) -> Unit,
) {
    val shiftSize = 10 * shift
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(node) },
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.heightIn(64.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
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
                        modifier = Modifier.weight(1f),
                        text = node.content.data.description.orEmpty()
                    )
                    if (order != null) {
                        Column {
                            Text(text = "${order.amount} ${order.godEntity.measureData?.name ?: ""}")
                            Text(text = "${order.price} грн")
                        }
                    }

                    Spacer(modifier = Modifier.size(8.dp))
                }
            }

            if (!node.content.data.isFolder) {
                Text(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = (24 + shiftSize).dp),
                    text = "На складе ${node.content.availableAmount} ${node.content.measureData?.name}"
                )
            }

            node.content.price.forEach {
                Text(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = (24 + shiftSize).dp),
                    text = "${it.priceTypeName}: ${it.priceValue} грн"
                )
            }
        }

        HorizontalDivider(thickness = 0.5.dp)
    }
}

fun LazyListScope.TreeLeaf(
    node: TreeNode,
    order: GodOrderTemplate?,
    shift: Int,
    onItemClick: (TreeNode) -> Unit,
) {
    item {
        TreeLeafComposable(node = node, order = order, shift = shift, onItemClick = onItemClick)
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
                            refKey = "asdsad"
                            description = "Папка 1"
                            isFolder = true
                        }), expanded = true, children = mutableListOf(
                            TreeNode(
                                GodEntity(
                                    GodsData().apply {
                                        refKey = "123213"
                                        description =
                                            "Файл 1 длинное название кабеля ШВВП бла бла бла"
                                    },
                                    price = listOf(PriceData().apply {
                                        priceTypeName = "Оптовая цена"
                                        priceValue = 123F
                                    }, PriceData().apply {
                                        priceTypeName = "Розничная цена"
                                        priceValue = 145F
                                    })
                                )
                            ), TreeNode(GodEntity(GodsData().apply {
                                refKey = "123"
                                description = "Файл 2"
                            })), TreeNode(GodEntity(GodsData().apply {
                                refKey = "1233"
                                description = "Файл 3"
                            })), TreeNode(GodEntity(GodsData().apply {
                                refKey = "1233"
                                description = "Файл 4"
                            }))
                        )
                    )
                ),

                orderTemplates = listOf(
                    GodOrderTemplate(
                        GodEntity(
                            GodsData().apply {
                                refKey = "123213"
                                description =
                                    "Файл 1 длинное название кабеля ШВВП бла бла бла"
                            },
                            price = listOf(PriceData().apply {
                                priceTypeName = "Оптовая цена"
                                priceValue = 123F
                            }, PriceData().apply {
                                priceTypeName = "Розничная цена"
                                priceValue = 145F
                            })
                        ), 123F, 555F
                    )
                )
            )
        )
    }
}