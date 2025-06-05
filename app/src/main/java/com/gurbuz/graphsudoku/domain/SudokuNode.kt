package com.gurbuz.graphsudoku.domain

import java.io.Serializable

/**
 * A Node in a sudoku puzzle is denoted by:
 * - A value or color, which is an integer denoted by the set of all numbers in the sudoku game
 * - A list of relative x and y value, where:
 *  - top left = x0, y0 (assuming 0 based indexing)
 *  - bottom right = xn-1, yn-1, where n is the largest integer in .. allowed numbers
 *
 * Implements Serializable which allows for reading and writing the sudoku nodes to files, eliminating the need for a database.
 */

data class SudokuNode(
    val x: Int,
    val y: Int,
    val color: Int = 0,
    val readOnly: Boolean = true,
) : Serializable{
    override fun hashCode(): Int {
        return getHash(x, y)
    }


}

internal fun getHash(x: Int, y: Int): Int{
    val newX = x*100 // 9x9 sudoku puzzle can run of out unique hash codes.
    return "$newX$y".toInt()
}
