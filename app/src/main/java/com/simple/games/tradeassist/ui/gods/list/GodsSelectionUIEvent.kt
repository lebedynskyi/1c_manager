package com.simple.games.tradeassist.ui.gods.list

import com.simple.games.dexter.ui.base.AppUIEvent

sealed class GodsSelectionUIEvent : AppUIEvent() {
    class OnScreenLoaded(val customerKey: String) : GodsSelectionUIEvent()
    class OnShowAllToggleChanged(val showAll: Boolean) : GodsSelectionUIEvent()
    class OnFilterQueryChanged(val query: String) : GodsSelectionUIEvent()
    class OnGodsClicked(val node: TreeNode) : GodsSelectionUIEvent()
}