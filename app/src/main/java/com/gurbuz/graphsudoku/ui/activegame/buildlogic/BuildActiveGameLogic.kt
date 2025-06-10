package com.gurbuz.graphsudoku.ui.activegame.buildlogic

import android.content.Context
import com.gurbuz.graphsudoku.common.DispatcherProvider
import com.gurbuz.graphsudoku.common.ProductionDispatcherProvider
import com.gurbuz.graphsudoku.persistence.GameRepositoryImpl
import com.gurbuz.graphsudoku.persistence.LocalGameStorageImpl
import com.gurbuz.graphsudoku.persistence.LocalSettingsStorageImpl
import com.gurbuz.graphsudoku.persistence.LocalStatisticsStorageImpl
import com.gurbuz.graphsudoku.persistence.settingsDataStore
import com.gurbuz.graphsudoku.ui.activegame.ActiveGameContainer
import com.gurbuz.graphsudoku.ui.activegame.ActiveGameLogic
import com.gurbuz.graphsudoku.ui.activegame.ActiveGameViewModel

internal fun buildActiveGameLogic(
    container: ActiveGameContainer,
    viewModel: ActiveGameViewModel,
    context: Context
): ActiveGameLogic{
    return ActiveGameLogic(
        container,
        viewModel,
        gameRepo =GameRepositoryImpl(
            LocalGameStorageImpl(context.filesDir.path),
            LocalSettingsStorageImpl(context.settingsDataStore)
        ),
        statsRepo = LocalStatisticsStorageImpl(
            context.statsDataStore
        ),
        dispatcher = ProductionDispatcherProvider
    )
}