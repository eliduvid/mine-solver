# Minesweeper solver
...that I wrote in one day.

To build run `greadlew build jar`.

To run built jar run `java -jar build\libs\mine-solver-1.0-SNAPSHOT.jar`

There is naive implementation of minesweeper board generation that is almost never solvable for big bombs/board size ratios. For the algorithm to work with other implementation it should implement interfaces from `me.eliduvid.mineSolver.model.Game`.

Algorithm does not take into the account bombs quantity, but with current game implementation it's very rarely relevant :-)

Oh, if it does not know what to do it does nothing and **not** guesses.