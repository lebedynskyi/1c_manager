@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.simple.games.tradeassist.ui.gods.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.simple.games.dexter.ui.base.AppUIEvent
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.ui.base.design.AppTopBar
import com.simple.games.tradeassist.ui.base.design.ContentLoadingContainer

@Composable
fun GodsSelectionScreen(
    state: GodsSelectionViewState,
    onUIEvent: (AppUIEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = R.string.gods_title,
                leftIcon = R.drawable.ic_arrow_back,
                onLeftIconClick = {
                    onUIEvent(AppUIEvent.OnBackClicked)
                })
        }
    ) {
        GodsSelectionScreenContent(
            state.contentInProgress,
            filterQuery = state.filterQuery,
            godsList = state.godsList,
            modifier = Modifier.padding(it),
            onQueryChanged = { onUIEvent(GodsSelectionUIEvent.OnFilterQueryChanged(it)) },
            onGodsClicked = { onUIEvent(GodsSelectionUIEvent.OnGodsClicked(it)) }
        )
        HorizontalDivider(modifier = Modifier.padding(it), thickness = 0.5.dp)
    }
}

@Composable
fun GodsSelectionScreenContent(
    contentInProgress: Boolean,
    godsList: List<TreeNode>,
    filterQuery: String,
    modifier: Modifier = Modifier,
    onQueryChanged: (String) -> Unit,
    onGodsClicked: (TreeNode) -> Unit,
) {
    var refreshTrigger by remember { mutableIntStateOf(0) }
    val scrollState = rememberLazyListState()
    System.err.println("Trigger -> $refreshTrigger, list size -> ${godsList.size}")

    ContentLoadingContainer(
        isContentInProgress = contentInProgress,
        modifier = modifier
    ) {
        OutlinedTextField(
            placeholder = {
                Text(text = stringResource(id = R.string.filter))
            },
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            value = filterQuery, onValueChange = onQueryChanged
        )

        LazyColumn(
            modifier = Modifier
                .padding(top = OutlinedTextFieldDefaults.MinHeight)
                .padding(top = 8.dp)
                .fillMaxSize()
                .verticalScrollbar(scrollState),
            state = scrollState
        ) {
            for (t in godsList) {
                TreeBranch(t) {
                    if (it.content.isFolder) {
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

fun LazyListScope.TreeBranch(
    node: TreeNode,
    shift: Int = 0,
    onItemClick: (TreeNode) -> Unit,
) {
    TreeLeaf(node, shift, onItemClick)

    if (node.expanded) {
        for (f in node.children) {
            TreeBranch(f, shift.inc(), onItemClick)
        }
    }
}

fun LazyListScope.TreeLeaf(
    node: TreeNode,
    shift: Int,
    onItemClick: (TreeNode) -> Unit,
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onItemClick(node) },
        ) {
            Row(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (shift > 0) {
                    Spacer(modifier = Modifier.size((12 * shift).dp))
                }
                if (node.content.isFolder) {
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
                        text = node.content.description.orEmpty()
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
                        text = node.content.description.orEmpty()
                    )
                    Text(text = "${node.content.amount} ${node.content.measure?.name ?: ""}")
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }

            HorizontalDivider(thickness = 0.5.dp)
        }
    }
}

@Composable
fun Modifier.verticalScrollbar(
    state: LazyListState,
    width: Dp = 6.dp,
    color: Color = MaterialTheme.colorScheme.onBackground,
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
    val duration = if (state.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration),
    )

    return drawWithContent {
        drawContent()

        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

        // Draw scrollbar if scrolling or if the animation is still running and lazy column has content
        if (needDrawScrollbar && firstVisibleElementIndex != null) {
            val elementHeight = (this.size.height / state.layoutInfo.totalItemsCount)
            val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
            val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight

            drawRect(
                color = color,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha,
            )
        }
    }
}


@Preview
@Composable
fun GodsSelectionScreenPreview() {
    TradeAssistTheme {
        GodsSelectionScreen(
            GodsSelectionViewState(
                godsList = listOf(
                    TreeNode(GodsData().apply {
                        isFolder = true
                        description = "Длиное название катгеории. Хочу чтоб было 2 линии"
                    }),
                    TreeNode(
                        GodsData().apply {
                            isFolder = true
                            description = "Fodler2"
                        }, expanded = true, children = mutableListOf(
                            TreeNode(GodsData().apply {
                                isFolder = true
                                description = "Fodler3"
                            }),
                            TreeNode(GodsData().apply {
                                description = "File1"
                            }),
                        )
                    ),
                    TreeNode(GodsData().apply {
                        isFolder = true
                        description = "Fodler3"
                    }),
                    TreeNode(GodsData().apply {
                        description =
                            "Длиное название катгеории. Хочу чтоб было 2 линии в притык к колличеству"
                        measure = MeasureData().apply { name = "шт" }
                    }),
                    TreeNode(GodsData().apply {
                        description =
                            "Длиное название катгеории. Хочу чтоб было 2 линии в притык к колличеству"
                    }),
                    TreeNode(GodsData().apply {
                        description = "File3"
                    }),

                    )
            )
        )
    }
}