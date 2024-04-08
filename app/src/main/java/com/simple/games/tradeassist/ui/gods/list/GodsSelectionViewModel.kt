package com.simple.games.tradeassist.ui.gods.list

import com.simple.games.tradeassist.core.navigation.AppRoute
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.domain.GodEntity
import com.simple.games.tradeassist.domain.OrderEntity
import com.simple.games.tradeassist.ui.base.AppViewModel
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class GodsSelectionViewModel @Inject constructor(
    private val repository: C1Repository
) : AppViewModel<GodsSelectionViewState>(GodsSelectionViewState(contentInProgress = true)) {
    override val viewStateCopy: GodsSelectionViewState get() = viewState.value.copy()

    private var loadedGods: List<GodEntity> = emptyList()
    private var filteredTree: List<TreeNode> = emptyList()
    private var fullTree: List<TreeNode> = emptyList()
    private var currentOrder: OrderEntity? = null

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            is AppUIEvent.OnBackClicked -> handleBackClicked()
            is GodsSelectionUIEvent.OnScreenLoaded -> handleScreenLoaded(event.orderId)
            is GodsSelectionUIEvent.OnDoneClick -> handleDoneClicked()
            is GodsSelectionUIEvent.OnCollapseClick -> handleCollapseClicked()
            is GodsSelectionUIEvent.OnShowAllToggleChanged -> handleShowAllToggle(event.showAll)
            is GodsSelectionUIEvent.OnGodsClicked -> handleGodsClicked(event.node)
            is GodsSelectionUIEvent.OnAddGodOrder -> handleGodOrderAdded(event.order)
        }
        super.onUIEvent(event)
    }

    private fun handleShowAllToggle(isFullMode: Boolean) = launch {
        if (isFullMode && fullTree.isEmpty()) return@launch
        if (!isFullMode && filteredTree.isEmpty()) return@launch

        reduce {
            godsList = if (isFullMode) fullTree else filteredTree
            showAll = isFullMode
        }
    }

    private fun handleDoneClicked() = launch {
        navigate {
            toBack()
        }
    }

    private fun handleCollapseClicked() = launch { state ->
        val collapsedTree = buildList {
            addAll(collapseAll(state.godsList))
        }

        reduce {
            godsList = collapsedTree
        }
    }

    private fun handleScreenLoaded(orderId: Long?) = launch {
        if (loadedGods.isNotEmpty()) {
            return@launch
        }

        reduce { contentInProgress = true }

        currentOrder = orderId?.let { repository.getDraft(it) }

        repository.getGods().onSuccess {
            loadedGods = it

            filteredTree = buildTree(it, fullMode = false)
        }

        reduce {
            orderTemplates = currentOrder?.gods
            godsList = filteredTree
            contentInProgress = false
        }

        delay(300)
        fullTree = buildTree(loadedGods, fullMode = true)
    }

    private fun handleGodOrderAdded(orderTemplate: GodOrderTemplate) = launch {
       currentOrder?.let {
           it.gods = buildList {
               addAll(it.gods.orEmpty().filter { it.godEntity.data.refKey != orderTemplate.godEntity.data.refKey })
               add(orderTemplate)
           }

           repository.saveOrder(it)

           reduce {
               orderTemplates = it.gods
           }
       }
    }

    private fun handleGodsClicked(node: TreeNode) = launch {
        navigate {
            val addedGod = currentOrder?.gods?.firstOrNull { it.godEntity.data.refKey == node.content.data.refKey }
            toGodsInfo(node.content, currentOrder?.customerName, currentOrder?.customerKey, addedGod?.amount, addedGod?.price)
        }
    }

    private fun handleBackClicked() = launch {
        navigate {
            toBack()
        }
    }

    private fun buildTree(gods: List<GodEntity>, fullMode: Boolean): List<TreeNode> {
        val nodeMap = mutableMapOf<String, TreeNode>()

        // Create nodes and populate the map
        for (entity in gods) {
            if (!fullMode && !entity.data.isFolder && entity.availableAmount == 0F) {
                continue
            }
            val node = TreeNode(entity)
            nodeMap[entity.data.refKey] = node
        }

        // Link child nodes to their parent nodes
        for (god in gods) {
            if (god.data.parentKey != null) {
                val parentNode = nodeMap[god.data.parentKey]
                val children = nodeMap[god.data.refKey]
                parentNode?.children?.add(children ?: continue)
            }
        }

        // Identify root nodes (nodes with no parents)
        val roots = mutableListOf<TreeNode>()
        for ((_, node) in nodeMap) {
            if (gods.none { it.data.refKey == node.content.data.refKey } || node.content.data.parentKey == "00000000-0000-0000-0000-000000000000") {
                sortTreeNode(node)
                roots.add(node)
            }
        }

        roots.sortWith(compareBy({ !it.content.data.isFolder }, { it.content.data.description?.lowercase() }))

        if (!fullMode) {
            filterWithFiles(roots)
        }

        return roots
    }

    private fun filterWithFiles(nodes: List<TreeNode>) {
        nodes.forEach {
            if (it.content.data.isFolder) {
                it.children = it.children.filter { hasFile(it) }.toMutableList()
                filterWithFiles(it.children)
            }
        }
    }

    private fun hasFile(node: TreeNode): Boolean {
        var hasFiles = false
        if (node.content.data.isFolder) {
            node.children.forEach {
                hasFiles = hasFiles || hasFile(it)
                if (hasFiles) {
                    return true
                }
            }
        } else {
            hasFiles = true
        }
        return hasFiles
    }

    private fun sortTreeNode(root: TreeNode): TreeNode {
        root.children.sortWith(
            compareBy(
                { !it.content.data.isFolder },
                { it.content.data.description?.lowercase() })
        )
        root.children.forEach { sortTreeNode(it) }
        return root
    }
    private fun collapseAll(node: List<TreeNode> ): MutableList<TreeNode>{
        val nodes = node.map {
            it.copy(expanded = false, children = collapseAll(it.children))
        }.toMutableList()
        return nodes
    }
}