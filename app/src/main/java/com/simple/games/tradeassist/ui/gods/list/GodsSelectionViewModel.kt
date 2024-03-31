package com.simple.games.tradeassist.ui.gods.list

import com.simple.games.tradeassist.core.navigation.AppRoute
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.domain.C1Repository
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

    private var loadedGods: List<GodsData> = emptyList()
    private var filteredTree: List<TreeNode> = emptyList()
    private var fullTree: List<TreeNode> = emptyList()
    private var customer: CustomerData? = null

    private val orders = mutableListOf<GodOrderTemplate>()

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            is AppUIEvent.OnBackClicked -> handleBackClicked()
            is GodsSelectionUIEvent.OnScreenLoaded -> handleScreenLoaded(event.customer)
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
            toBack(AppRoute.GodsSelectionRoute.resultSelectedGods to orders)
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

    private fun handleScreenLoaded(customer: CustomerData?) = launch {
        if (loadedGods.isNotEmpty()) {
            return@launch
        }

        this.customer = customer
        reduce {
            contentInProgress = true
        }
        repository.getGods().onSuccess {
            loadedGods = it
            filteredTree = buildTree(it, fullMode = false)
            reduce {
                godsList = filteredTree
            }
        }

        reduce {
            contentInProgress = false
        }

        delay(500)
        fullTree = buildTree(loadedGods, fullMode = true)
    }

    private fun handleGodOrderAdded(orderTemplate: GodOrderTemplate) {
        orders.add(orderTemplate)
        reduce {
            this.orderTemplates = mutableListOf<GodOrderTemplate>().apply {
                orders.forEach {
                    add(it)
                }
            }
        }
    }

    private fun handleGodsClicked(node: TreeNode) = launch {
        navigate {
            toGodsInfo(customer, node.content)
        }
    }

    private fun handleBackClicked() = launch {
        navigate {
            toBack()
        }
    }

    private fun buildTree(gods: List<GodsData>, fullMode: Boolean): List<TreeNode> {
        val nodeMap = mutableMapOf<String, TreeNode>()

        // Create nodes and populate the map
        for (god in gods) {
            if (!fullMode && !god.isFolder && god.amount == 0F) {
                continue
            }
            val node = TreeNode(god)
            nodeMap[god.refKey] = node
        }

        // Link child nodes to their parent nodes
        for (god in gods) {
            if (god.parentKey != null) {
                val parentNode = nodeMap[god.parentKey]
                val children = nodeMap[god.refKey]
                parentNode?.children?.add(children ?: continue)
            }
        }

        // Identify root nodes (nodes with no parents)
        val roots = mutableListOf<TreeNode>()
        for ((_, node) in nodeMap) {
            if (gods.none { it.refKey == node.content.refKey } || node.content.parentKey == "00000000-0000-0000-0000-000000000000") {
                sortTreeNode(node)
                roots.add(node)
            }
        }

        roots.sortWith(compareBy({ !it.content.isFolder }, { it.content.description?.lowercase() }))

        if (!fullMode) {
            filterWithFiles(roots)
        }

        return roots
    }

    private fun filterWithFiles(nodes: List<TreeNode>) {
        nodes.forEach {
            if (it.content.isFolder) {
                it.children = it.children.filter { hasFile(it) }.toMutableList()
                filterWithFiles(it.children)
            }
        }
    }

    private fun hasFile(node: TreeNode): Boolean {
        var hasFiles = false
        if (node.content.isFolder) {
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
                { !it.content.isFolder },
                { it.content.description?.lowercase() })
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