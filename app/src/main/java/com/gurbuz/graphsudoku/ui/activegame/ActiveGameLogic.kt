package com.gurbuz.graphsudoku.ui.activegame

import com.gurbuz.graphsudoku.common.BaseLogic
import com.gurbuz.graphsudoku.common.DispatcherProvider
import com.gurbuz.graphsudoku.domain.IGameRepository
import com.gurbuz.graphsudoku.domain.IStatisticsRepository
import com.gurbuz.graphsudoku.domain.SudokuPuzzle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ActiveGameLogic(
    private val container: ActiveGameContainer?,
    private val viewModel: ActiveGameViewModel,
    private val gameRepo: IGameRepository,
    private val statsRepo: IStatisticsRepository,
    private val dispatcher: DispatcherProvider,
) : BaseLogic<ActiveGameEvent>(), CoroutineScope {
    override fun onEvent(event: ActiveGameEvent) {
        when (event) {
            is ActiveGameEvent.OnInput -> onInput(
                event.input,
                viewModel.timerState
            )

            ActiveGameEvent.OnNewGameClicked -> onNewGameClicked()
            ActiveGameEvent.OnStart -> onStart()
            ActiveGameEvent.OnStop -> onStop()
            is ActiveGameEvent.OnTileFocused -> onTileFocused(event.x, event.y)
        }
    }

    private fun onTileFocused(x: Int, y: Int) = launch {

    }

    private fun onStop() {
        if (!viewModel.isCompleteState) launch {
            gameRepo.saveGame(
                elapsedTime = viewModel.timerState.timeOffset,
                onSuccess = {
                    cancelStuff()
                },
                onError = {
                    cancelStuff()
                    container?.showError()
                },
            )
        } else {
            cancelStuff()
        }
    }

    private fun onStart() = launch {
        gameRepo.getCurrentGame(
            onSuccess = { puzzle, isComplete ->
                viewModel.initializeBoardState(
                    puzzle,
                    isComplete
                )
                if (!isComplete) timerTracker = startCoroutineTimer {
                    viewModel.updateTimerState()
                }
            },
            onError = {
                container?.onNewGameClick()
            }
        )
    }

    private fun onNewGameClicked() = launch {
        viewModel.showLoadingState()
        if (!viewModel.isCompleteState) {
            gameRepo.getCurrentGame(
                { puzzle, _ ->
                    updateWithTime(puzzle)
                },
                {
                    container?.showError()
                }
            )
        } else {
            navigateToNewGame()
        }
    }

    private fun updateWithTime(puzzle: SudokuPuzzle) = launch {
        gameRepo.updateGame(
            puzzle.copy(elapsedTime = viewModel.timerState.timeOffset),
            onSuccess = {
                navigateToNewGame()
            },
            onError = {
                container?.showError()
                navigateToNewGame()
            }
        )
    }

    private fun navigateToNewGame() = launch {
        cancelStuff()
        container?.onNewGameClick()
    }

    private fun cancelStuff() {
        if (timerTracker?.isCancelled == true) timerTracker?.cancel()
    }

    private fun onInput(input: Int, elapsedTime: Long) = launch {
        var focusedTile: SudokuTile? = null
        viewModel.boardState.values.forEach {
            if (it.hasFocus) focusedTile = it
        }

        if (focusedTile != null) {
            gameRepo.updateNode(
                x = focusedTile!!.x,
                y = focusedTile!!.y,
                color = input,
                elapsedTime = elapsedTime,
                onSuccess = { isComplete ->
                    {
                        focusedTile?.let {
                            viewModel.updateBoardState(
                                it.x,
                                it.y,
                                input,
                                false
                            )
                        }
                        if (isComplete) {
                            timerTracker?.cancel()
                            checkIfNewRecord()
                        }
                    }
                },
                onError = { container?.showError() }
            )
        }
    }

    private fun checkIfNewRecord() = launch {
        statsRepo.updateStatistic(
            time = viewModel.timerState,
            diff = viewModel.difficulty,
            boundary = viewModel.boundary,
            onSuccess = { isRecord ->
                viewModel.isNewRecordState = isRecord
                viewModel.updateCompleteState()
            },
            onError = {
                container?.showError()
                viewModel.updateCompleteState()
            }
        )
    }

    override val coroutineContext: CoroutineContext
        get() = dispatcher.provideUIContext() + jobTracker

    init {
        jobTracker = Job()
    }

    inline fun startCoroutineTimer(
        crossinline action: () -> Unit
    ) = launch {
        while (true) {
            action()
            delay(1000)
        }
    }

    private var timerTracker: Job? = null

    private val Long.timeOffset: Long
        get() {
            return if (this <= 0) 0
            else this - 1
        }

}
