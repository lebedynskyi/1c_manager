package com.simple.games.tradeassist.ui.gods.list

import com.simple.games.dexter.ui.base.AppUIEvent
import com.simple.games.tradeassist.core.navigation.AppRoute
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.ui.base.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GodsSelectionViewModel @Inject constructor(
    private val repository: C1Repository
) :
    AppViewModel<GodsSelectionViewState>(GodsSelectionViewState(contentInProgress = true)) {
    override val viewStateCopy: GodsSelectionViewState get() = viewState.value.copy()

    private var loadedGods: List<GodsData> = emptyList()
    private var filteredTree: List<TreeNode> = emptyList()
    private var fullTree: List<TreeNode> = emptyList()

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            is AppUIEvent.OnBackClicked -> handleBackClicked()
            is GodsSelectionUIEvent.OnScreenLoaded -> handleScreenLoaded()
            is GodsSelectionUIEvent.OnGodsClicked -> handleGodsClicked(event.node)
        }
        super.onUIEvent(event)
    }

    private fun handleScreenLoaded() = launch {
        reduce {
            contentInProgress = true
        }
        repository.getGods().onSuccess {
            loadedGods = it
            val godsTree = buildTree(it, )
            reduce {
                godsList = godsTree
            }
        }

        reduce {
            contentInProgress = false
        }
    }

    private fun handleGodsClicked(node: TreeNode) = launch{
        navigate {
            toBack(listOf(AppRoute.GodsSelectionRoute.resultSelectedGodKey to node.content.refKey))
        }
    }

    private fun handleBackClicked() = launch {
        navigate {
            toBack()
        }
    }

    private fun buildTree(gods: List<GodsData>): List<TreeNode> {
        val nodeMap = mutableMapOf<String, TreeNode>()

        // Create nodes and populate the map
        for (god in gods) {
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
        return roots
    }

    private fun sortTreeNode(root: TreeNode): TreeNode {
        root.children.sortWith(compareBy({ !it.content.isFolder }, { it.content.description?.lowercase() }))
        root.children.forEach { sortTreeNode(it) }
        return root
    }
}