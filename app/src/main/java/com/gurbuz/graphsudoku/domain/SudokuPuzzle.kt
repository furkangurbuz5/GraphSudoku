package com.gurbuz.graphsudoku.domain

import java.io.Serializable
import java.util.LinkedList

data class SudokuPuzzle(
    val boundary: Int,
    val difficulty: Difficulty,
    val graph: LinkedHashMap<Int, LinkedList<SudokuNode>>
    = TODO(),//buildNewSudoku(boundary, difficulty).graph,
    var elapsedTime: Long = 0L
): Serializable{
    fun getValue(): LinkedHashMap<Int, LinkedList<SudokuNode>> = graph
}