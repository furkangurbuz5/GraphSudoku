package com.gurbuz.graphsudoku.common

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object ProductionDispatcherProvider : DispatcherProvider {
    override fun provideUIContext(): CoroutineContext {
        return Dispatchers.Main
    }

    override fun provideIOContext(): CoroutineContext {
        return Dispatchers.Unconfined
    }

}