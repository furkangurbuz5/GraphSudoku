package com.gurbuz.graphsudoku.ui.activegame

import android.graphics.drawable.Icon
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gurbuz.graphsudoku.R
import com.gurbuz.graphsudoku.ui.components.AppToolbar
import com.gurbuz.graphsudoku.ui.textColorDark
import com.gurbuz.graphsudoku.ui.textColorLight

enum class ActiveGameScreenState {
    LOADING,
    ACTIVE,
    COMPLETE
}

@Composable
fun ActiveGameScreen(
    onEventHandler: (ActiveGameEvent) -> Unit,
    viewModel: ActiveGameViewModel
) {
    val contentTransitionState = remember {
        MutableTransitionState(
            ActiveGameScreenState.LOADING
        )
    }

    viewModel.subContentState = {
        contentTransitionState.targetState = it
    }

    val transition = updateTransition(contentTransitionState) //TODO: deprecated, change this

    val loadingAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }
    ) {
        if (it == ActiveGameScreenState.LOADING) 1f else 0f
    }

    val activeAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }
    ) {
        if (it == ActiveGameScreenState.ACTIVE) 1f else 0f
    }

    val completeAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }
    ) {
        if (it == ActiveGameScreenState.COMPLETE) 1f else 0f
    }

    Column(
        Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxHeight()
    ) {
        AppToolbar(
            modifier = Modifier.wrapContentHeight(),
            title = stringResource(R.string.app_name),
            ) {
            NewGameIcon(onEventHandler = onEventHandler)
        }

        Box(
            modifier = Modifier.fillMaxHeight()
                .padding(top = 4.dp),
            contentAlignment = Alignment.TopCenter
        ){
            when(contentTransitionState.currentState){
                ActiveGameScreenState.LOADING -> TODO()
                ActiveGameScreenState.ACTIVE -> TODO()
                ActiveGameScreenState.COMPLETE -> TODO()
            }
        }
    }
}

@Composable
fun NewGameIcon(onEventHandler: (ActiveGameEvent) -> Unit) {
    Icon(
        imageVector = Icons.Filled.Add,
        tint = if (MaterialTheme.colors.isLight) textColorLight else textColorDark,
        contentDescription = null,
        modifier = Modifier
            .clickable {
                onEventHandler.invoke(ActiveGameEvent.OnNewGameClicked)
            }
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .height(36.dp)
    )
}