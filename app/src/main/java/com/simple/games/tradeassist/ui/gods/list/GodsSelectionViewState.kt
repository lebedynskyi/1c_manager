package com.simple.games.tradeassist.ui.gods.list

import com.simple.games.tradeassist.ui.base.AppViewState

data class GodsSelectionViewState(
    override var contentInProgress: Boolean = false,
    var filterQuery: String = "",
    var godsList: List<TreeNode> = emptyList()
) : AppViewState() {
}