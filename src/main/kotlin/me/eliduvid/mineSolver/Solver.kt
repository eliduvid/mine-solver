package me.eliduvid.mineSolver

import me.eliduvid.mineSolver.model.Cell
import me.eliduvid.mineSolver.model.CellStatus
import me.eliduvid.mineSolver.model.Coordinates
import me.eliduvid.mineSolver.model.Game

/**
 * Solver algorithm.
 * @param startCoordinates should be guarantied not to be a bomb (better 0)
 * @param callback will be called after each step of the algorithm with string describing last action in loose format
 */
fun solve(game: Game, startCoordinates: Coordinates, callback: (action: String) -> Unit = {}) {
    game[startCoordinates].open()
    var solved: Boolean
    while (!game.solved.also { solved = it }) {
        var madeSimpleAction = false
        while (true) {
            val foundCell = game.allCells.find { it.canOpenAllAround() } ?: break
            foundCell.closedCellsAround.forEach { it.open() }
            madeSimpleAction = true
            callback("opened simple $foundCell")
        }
        while (true) {
            val foundCell = game.allCells.find { it.canMarkAllAroundAsBombs() } ?: break
            foundCell.closedCellsAround.forEach { it.mark() }
            madeSimpleAction = true
            callback("marked simple $foundCell")
        }

        var madeComplexAction = false
        if (!madeSimpleAction) {
            for (cell in game.allCells) {
                if (cell.status != CellStatus.OPEN) continue
                val intersecting = cell.getIntersecting()
                if (intersecting.isEmpty()) continue
                val nonBombsByIntersectingCells = cell.getNonBombsByIntersectingCells(intersecting)
                if (nonBombsByIntersectingCells.isEmpty()) {
                    val bombsByIntersectingCells = cell.getBombsByIntersectingCells(intersecting)
                    if (bombsByIntersectingCells.isEmpty()) continue
                    bombsByIntersectingCells.forEach { it.mark() }
                    madeComplexAction = true
                    callback("marked ${bombsByIntersectingCells.size} cells complex by $cell")
                    break
                } else {
                    nonBombsByIntersectingCells.forEach { it.open() }
                    madeComplexAction = true
                    callback("opened ${nonBombsByIntersectingCells.size} cells complex by $cell")
                    break
                }
            }
            if (!madeComplexAction) break
        }
    }

    if (solved) game.allCells.filter { it.status == CellStatus.CLOSED }.forEach { it.mark() }
    callback("finished")
}

private fun Cell.canMarkAllAroundAsBombs(): Boolean = status == CellStatus.OPEN &&
        cellNumber == this.closedCellsAround.size + markedBombsAround.size &&
        closedCellsAround.isNotEmpty()


private fun Cell.canOpenAllAround(): Boolean = status == CellStatus.OPEN &&
        cellNumber == markedBombsAround.size &&
        closedCellsAround.isNotEmpty()


private fun Cell.getIntersecting(): List<Cell> = closedCellsAround
    .asSequence()
    .flatMap { it.openCellsAround.asSequence() }
    .distinctBy { it.coordinates }
    .filter { it.coordinates != coordinates }
    .toList()

private fun Cell.getNonBombsByIntersectingCells(intersectingCells: List<Cell>): List<Cell> {
    val needToMarkBombs = cellNumber - markedBombsAround.size
    val closedCellsAround = this.closedCellsAround.toHashSet()
    val result = mutableListOf<Cell>()
    for (cell in intersectingCells) {
        if (cell.cellNumber - cell.markedBombsAround.size != needToMarkBombs) continue
        val otherClosedCellsAround = cell.closedCellsAround.toHashSet()
        if (closedCellsAround.size == otherClosedCellsAround.size) continue

        if (closedCellsAround.containsAll(otherClosedCellsAround)) {
            result += closedCellsAround - otherClosedCellsAround
        } else if (otherClosedCellsAround.containsAll(closedCellsAround)) {
            result += otherClosedCellsAround - closedCellsAround
        }
    }
    return result.distinctBy { it.coordinates }
}

private fun Cell.getBombsByIntersectingCells(intersectingCells: List<Cell>): List<Cell> {
    val needToMarkBombs = cellNumber - markedBombsAround.size
    val closedCellsAround = this.closedCellsAround.toHashSet()
    val result = mutableListOf<Cell>()
    for (cell in intersectingCells) {
        val otherClosedCellsAround = cell.closedCellsAround.toHashSet()
        val intersection = intersection(cell)
        val otherNeedToMarkBombs = cell.cellNumber - cell.markedBombsAround.size
        if (otherNeedToMarkBombs > needToMarkBombs &&
            needToMarkBombs < intersection.size &&
            otherClosedCellsAround.size - intersection.size == otherNeedToMarkBombs - needToMarkBombs
        ) {
            result += otherClosedCellsAround - intersection
        }

        if (needToMarkBombs > otherNeedToMarkBombs &&
            otherNeedToMarkBombs < intersection.size &&
            closedCellsAround.size - intersection.size == needToMarkBombs - otherNeedToMarkBombs
        ) {
            result += closedCellsAround - intersection
        }
    }
    return result.distinctBy { it.coordinates }
}

private fun Cell.intersection(other: Cell): Set<Cell> = closedCellsAround.intersect(other.closedCellsAround)