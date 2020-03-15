package ee.taltech.mjurge.iti0213_2019s_hw1.Kono.Player

import android.util.Log
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board.InterfaceGameSquare
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board.*
import kotlinx.coroutines.delay

class HumanPlayer<T: InterfaceGameSquare, U: InterfacePosition>(private var playerString: String):
    InterfacePlayer<T, U> {

    private var clickedPos: U? = null

    override suspend fun getMove(board: InterfaceBoard<T, U>): InterfaceMove<U> {
        clickedPos = null
        val tempBoard = board.getBoard()
        var from: Position
        var to: Position
        Log.d("---", "in getmove")


        while (true) {
            from = waitForClickedLocation()
            Log.d("---", tempBoard[from.getRow()][from.getCol()].getString())
            Log.d("---", playerString)
            if (tempBoard[from.getRow()][from.getCol()].getString() == playerString) break
        }
        Log.d("---", "out of first while")
        board.select(from as U)
        Log.d("---", "SELECTED!")
        clickedPos = null
        while (true) {
            Log.d("---", "second while start")
            to = waitForClickedLocation()
            if (board.canMakeMove(Move(from, to) as InterfaceMove<U>)) {
                break
            } else if (from.equals(to)) {
                board.deSelect(from as U)
                clickedPos = null
                return getMove(board)
            }
        }
        Log.d("---", "end")
        return Move(from, to) as InterfaceMove<U>
    }

    override fun onUserClickedLocation(location: U) {
        clickedPos = location
    }

    @Synchronized
    private suspend fun waitForClickedLocation(): Position {
        while (true) {
            if (clickedPos != null) {
                break
            }
            delay(100)
        }
        return clickedPos as Position
    }
}