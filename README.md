# Minesweeper solver
...that I wrote in one day.

To build run `greadlew build jar`.

To run built jar run `java -jar build\libs\mine-solver-1.0-SNAPSHOT.jar`

For it to run properly you need terminal that supports [ascii escape sequences](http://ascii-table.com/ansi-escape-sequences.php). E.g. windows `cmd.exe` and your IDE console are not, but new [windows terminal](https://github.com/microsoft/terminal) and majority of Linux terminals (as far as I'm concerned) are. 

There is naive implementation of minesweeper board generation that is almost never solvable for big bombs/board size ratios. For the algorithm to work with other implementation it should implement interfaces from `me.eliduvid.mineSolver.model.Game`.

Algorithm does not take into the account bombs quantity, but with current game implementation it's very rarely relevant :-)

Oh, if it does not know what to do it does nothing and **not** guesses.
