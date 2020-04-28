package me.eliduvid.mineSolver

import me.eliduvid.mineSolver.model.CellStatus
import java.lang.Thread.sleep


fun main() {
    val (game, startCoordinates) = generateBasicGame(30, 16, 100)
    solve(game, startCoordinates) { action ->
        clearScreen()
        println(game)
        println(action)
        sleep(10)
    }
    println("start $startCoordinates, " +
            "bombs marked ${game.allCells.count { it.status == CellStatus.MARKED }}/${game.bombs}, " +
            "closed cells ${game.allCells.count { it.status == CellStatus.CLOSED }}")

}

private fun clearScreen() {
    print("\u001b[H\u001b[2J")
}