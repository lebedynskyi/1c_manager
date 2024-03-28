package com.simple.games.tradeassist.ui.gods.list

import com.simple.games.dexter.ui.base.AppUIEvent

sealed class GodsSelectionUIEvent : AppUIEvent() {
    object OnScreenLoaded : GodsSelectionUIEvent()
    class OnFilterQueryChanged(val query: String) : GodsSelectionUIEvent()
    class OnGodsClicked(val node: TreeNode) : GodsSelectionUIEvent()
}