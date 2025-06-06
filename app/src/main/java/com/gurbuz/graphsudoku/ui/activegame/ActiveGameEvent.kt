package com.gurbuz.graphsudoku.ui.activegame

sealed class ActiveGameEvent {
    data class OnInput(val input: Int) : ActiveGameEvent()
    data class OnTileFocused(val x: Int) : ActiveGameEvent()
    object OnNewGameClicked : ActiveGameEvent()
    object OnStart : ActiveGameEvent()
    object OnStop : ActiveGameEvent()
}