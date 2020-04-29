package me.eliduvid.mineSolver

import me.eliduvid.mineSolver.model.CellStatus
import java.lang.Thread.sleep

const val SPACES = "                                                                "

fun main() {
    val (game, startCoordinates) = generateBasicGame(30, 16, 100)
    clearScreen()
    solve(game, startCoordinates) { action ->
        moveToTop()
        println(game)
        println(action + SPACES)
        sleep(10)
    }
    print("start $startCoordinates, " +
            "bombs marked ${game.allCells.count { it.status == CellStatus.MARKED }}/${game.bombs}, " +
            "closed cells ${game.allCells.count { it.status == CellStatus.CLOSED }}")
}

private fun moveToTop() {
    print("\u001B[0;0H")
}

private fun clearScreen() {
    print("\u001b[H\u001b[2J")
}