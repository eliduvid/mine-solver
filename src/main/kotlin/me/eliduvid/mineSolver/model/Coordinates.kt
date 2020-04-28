package me.eliduvid.mineSolver.model

data class Coordinates(val x: Int, val y: Int) {
    operator fun plus(coordinateVector: CoordinateVector): Coordinates = Coordinates(
        x + coordinateVector.x,
        y + coordinateVector.y
    )
}

data class CoordinateVector(val x: Int, val y: Int)

val standardCoordinateVectors = arrayOf(
    CoordinateVector(1, 1),
    CoordinateVector(1, -1),
    CoordinateVector(1, 0),
    CoordinateVector(-1, 1),
    CoordinateVector(-1, -1),
    CoordinateVector(-1, 0),
    CoordinateVector(0, 1),
    CoordinateVector(0, -1)
)