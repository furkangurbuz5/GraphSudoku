package com.gurbuz.graphsudoku.persistence

import com.gurbuz.graphsudoku.domain.GameStorageResult
import com.gurbuz.graphsudoku.domain.IGameDataStorage
import com.gurbuz.graphsudoku.domain.SudokuPuzzle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

private const val FILE_NAME = "game_state.txt"

class LocalGameStorageImpl(
    fileStorageDirectory: String,
    private val pathToStorageFile: File = File(fileStorageDirectory, FILE_NAME)
) : IGameDataStorage {
    override suspend fun updateGame(game: SudokuPuzzle): GameStorageResult =
        withContext(Dispatchers.IO) {
            try {
                updateGameData(game)
                GameStorageResult.OnSuccess(game)
            } catch (e: Exception) {
                GameStorageResult.OnError(e)
            }
        }

    private fun updateGameData(game: SudokuPuzzle){
        try {
            val fileOutputStream = FileOutputStream(pathToStorageFile)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(game)
            objectOutputStream.close()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateNode(
        x: Int,
        y: Int,
        elapsedTime: Long
    ): GameStorageResult {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentGame(): GameStorageResult {
        TODO("Not yet implemented")
    }

}