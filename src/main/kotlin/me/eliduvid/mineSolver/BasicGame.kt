package me.eliduvid.mineSolver

import me.eliduvid.mineSolver.model.*
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

fun generateBasicGame(x: Int, y: Int, bombs: Int): Pair<Game, Coordinates> {
    val actualGame = BasicGame(x, y, bombs)
    return actualGame to actualGame.allCells.shuffled()
        .find { cell -> !cell.isBomb and cell.cellsAround.all { !it.isBomb } }!!.coordinates
}

private class BasicGame(
    override val x: Int,
    override val y: Int,
    override val bombs: Int
) : Game, BaseGame() {
    override val board: List<List<BasicCell>> = List(y) { y -> List(x) { x -> BasicCell(this, Coordinates(x, y)) } }

    init {
        board.flatten().shuffled().slice(0 until bombs).forEach { it.isBomb = true }
    }

    override val allCells: List<BasicCell>
        get() = board.flatten()

    override operator fun get(coordinates: Coordinates): BasicCell = board[coordinates.y][coordinates.x]

}

private data class BasicCell(val basicGame: BasicGame, override val coordinates: Coordinates) : Cell {
    override val cellsAround: List<BasicCell>
        get() = standardCoordinateVectors.asSequence()
            .map { coordinates + it }
            .filter { basicGame.hasCell(it) }
            .map { basicGame[it] }
            .toList()

    var isBomb: Boolean = false
    override val cellNumber: Int
        get() {
            if (status == CellStatus.CLOSED) throw IllegalStateException("Can't get number of closed cell")
            if (isBomb) throw IllegalStateException("There's no number to bomb")
            return cellsAround.count { it.isBomb }
        }
    override var status: CellStatus = CellStatus.CLOSED
        private set

    override fun open() {
        if (status == CellStatus.MARKED) throw IllegalArgumentException("can't open already marked cell")
        if (isBomb) throw YouLoose(coordinates)
        status = CellStatus.OPEN
    }

    override fun mark() {
        if (status == CellStatus.OPEN) throw IllegalArgumentException("can't mark open cell")
        status = CellStatus.MARKED
    }

    override fun toString(): String {
        return "Cell(${when(status) {
            CellStatus.CLOSED -> "_"
            CellStatus.OPEN -> cellNumber.toString()
            CellStatus.MARKED -> "*"
        }}, $coordinates)"
    }
}