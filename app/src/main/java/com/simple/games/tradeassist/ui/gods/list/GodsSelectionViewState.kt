package com.simple.games.tradeassist.ui.gods.list

import com.simple.games.tradeassist.ui.base.AppViewState
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate

data class GodsSelectionViewState(
    override var contentInProgress: Boolean = false,
    var filterQuery: String = "",
    var filteredContent: List<TreeNode> = emptyList(),
    var godsList: List<TreeNode> = emptyList(),
    var showAll: Boolean = false,
    var orderTemplates: List<GodOrderTemplate>? = null,
) : AppViewState() {
}