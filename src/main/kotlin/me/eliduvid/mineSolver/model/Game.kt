package me.eliduvid.mineSolver.model

import java.lang.RuntimeException

// This interfaces should be implemented for solver algorithm to work on your implementation of minesweeper

interface Game {
    /**
     * Board width
     */
    val x: Int

    /**
     * Board height
     */
    val y: Int

    /**
     * Quantity of bombs on board
     */
    val bombs: Int

    /**
     * List of all cells on the board (no order required)
     */
    val allCells: List<Cell>

    /**
     * Are coordinates are valid for a cell on this board
     */
    fun hasCell(coordinates: Coordinates): Boolean =
        (coordinates.x >= 0) and (coordinates.x < x) and (coordinates.y >= 0) and (coordinates.y < y)

    operator fun get(coordinates: Coordinates): Cell

    /**
     * Has game been already solved
     */
    val solved: Boolean
        get() = allCells.count { it.status == CellStatus.OPEN } == x * y - bombs
    val board: List<List<Cell>>
}

abstract class BaseGame: Game {
    override fun toString(): String = board.asSequence()
        .map { row ->
            row.asSequence()
                .map {
                    when (it.status) {
                        CellStatus.MARKED -> "*"
                        CellStatus.CLOSED -> "_"
                        else -> it.cellNumber.toString()
                    }
                }
                .joinToString(" ")
        }
        .joinToString("\n")
}

interface Cell {
    val status: CellStatus

    /**
     * Number of bombs in proximity to this cell
     */
    val cellNumber: Int

    /**
     * Open closed cell.
     * Opening cell with bomb should fail the game (throw YouLoose)
     * Opening already opened cell should do nothing
     */
    fun open()

    /**
     * Mark cell as bomb
     * Marking open cell should throw exception
     * Marking already marked cell should do nothing
     * Marking non-bomb cell should work
     */
    fun mark()

    /**
     * Cells in proximity to this one (with no regard to status)
     */
    val cellsAround: List<Cell>
    val closedCellsAround
        get() = cellsAround.filter { it.status == CellStatus.CLOSED }
    val openCellsAround: List<Cell>
        get() = cellsAround.filter { it.status == CellStatus.OPEN }
    val markedBombsAround: List<Cell>
        get() = cellsAround.filter { it.status == CellStatus.MARKED }

    /**
     * Coordinates of the cell on the board
     */
    val coordinates: Coordinates

}

enum class CellStatus {
    CLOSED,
    OPEN,
    MARKED
}

/**
 * Should be thrown when trying to open cell with bomb
 */
class YouLoose(coordinates: Coordinates) :
    RuntimeException("cell $coordinates is a bomb, so you loose")