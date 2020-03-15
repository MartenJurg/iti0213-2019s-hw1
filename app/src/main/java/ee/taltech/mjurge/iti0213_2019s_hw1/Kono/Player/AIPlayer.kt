package ee.taltech.mjurge.iti0213_2019s_hw1.Kono.Player

import android.util.Log
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.Constants.C
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board.InterfaceGameSquare
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board.*
import kotlin.Double.Companion.POSITIVE_INFINITY as positiveInfinity
import kotlin.Double.Companion.NEGATIVE_INFINITY as negativeInfinity
import java.io.Serializable
import kotlin.math.max
import kotlin.math.min

class AIPlayer<T: InterfaceGameSquare, U: InterfacePosition>(private var playerString: String):
    InterfacePlayer<T, U>, Serializable {
    override suspend fun getMove(realBoard: InterfaceBoard<T, U>): InterfaceMove<U> {
        var board = realBoard.deepCopy()


        var possibleMoves = getPossibleMoves(board)
        var possibleMovesValuesDictionary = HashMap<Move<U>, Double>()
        for (possibleMove in possibleMoves) {
            Log.d("---", possibleMoves.toString())
            var tempBoard = board.deepCopy()
            tempBoard.makeMove(possibleMove)
            var alpha:Double = negativeInfinity
            var beta:Double = positiveInfinity
            val value = minimax(tempBoard, C.AI_DEPTH, alpha, beta)
            possibleMovesValuesDictionary[possibleMove] = value

        }

        var bestMove: Move<U>? = null
        var bestValue: Double? = null

        for (moveValuePair in possibleMovesValuesDictionary) {
            Log.d("---", moveValuePair.key.toString())
            Log.d("---", moveValuePair.value.toString())
            Log.d("---", "")
            if (bestMove == null) {
                bestMove = moveValuePair.key
                bestValue = moveValuePair.value
            } else if (bestValue != null) {
                if (board.getPlayer1Turn() && bestValue < moveValuePair.value) {
                    bestMove = moveValuePair.key
                    bestValue = moveValuePair.value
                } else if (!board.getPlayer1Turn() && bestValue > moveValuePair.value) {
                    bestMove = moveValuePair.key
                    bestValue = moveValuePair.value
                }
            }
        }
        return bestMove as InterfaceMove<U>
    }

    private fun minimax(board: InterfaceBoard<T, U>, depth: Int, alpha: Double, beta: Double): Double {
        if (depth == 0 || board.isGameOver()) {
            return getValueOfBoard(board)
        }
        var alphaTemp = alpha
        var betaTemp = beta
        var value = 0.0

        if (board.getPlayer1Turn()) {
            var maxEval = negativeInfinity
            for (newBoard in getPossibleBoardsOfPos(board)) {
                var eval = minimax(newBoard, depth - 1, alphaTemp, betaTemp)
                maxEval = max(maxEval, eval)
                alphaTemp = max(alphaTemp, eval)
                if (betaTemp <= alphaTemp) break
                value = maxEval
            }
        } else {


            var minEval = positiveInfinity
            for (newBoard in getPossibleBoardsOfPos(board)) {
                val eval = minimax(newBoard, depth - 1, alphaTemp, betaTemp)
                minEval = min(minEval, eval)
                value = minEval
                betaTemp = min(betaTemp, eval)
                if (betaTemp <= alphaTemp) break

            }
        }

        return value
    }

    private fun getPossibleBoardsOfPos(board: InterfaceBoard<T, U>):ArrayList<InterfaceBoard<T, U>> {
        var moves = getPossibleMoves(board)
        var boards = ArrayList<InterfaceBoard<T, U>>()
        for (move in moves) {
            var tempBoard = board.deepCopy()
            tempBoard.makeMove(move)
            boards.add(tempBoard)
        }
        return boards
    }

    private fun getPossibleMoves(board: InterfaceBoard<T, U>): ArrayList<Move<U>> {
        var movesList: ArrayList<Move<U>> = ArrayList<Move<U>>()
        var playerString: String
        var tempBoard = board.getBoard()

        if (board.getPlayer1Turn()) playerString = C.PLAYER_1_STRING else playerString = C.PLAYER_2_STRING
        for (i in 0 until C.SIZE) {
            for (j in 0 until C.SIZE) {
               if (tempBoard[i][j].getString() == playerString) {
                   var move = Move(Position(i, j), Position(i + 1, j + 1))
                   if (board.canMakeMove(move as InterfaceMove<U>)) movesList.add(move as Move<U>)
                   move = Move(Position(i, j), Position(i - 1, j + 1))
                   if (board.canMakeMove(move as InterfaceMove<U>)) movesList.add(move as Move<U>)
                   move = Move(Position(i, j), Position(i + 1, j - 1))
                   if (board.canMakeMove(move as InterfaceMove<U>)) movesList.add(move as Move<U>)
                   move = Move(Position(i, j), Position(i - 1, j - 1))
                   if (board.canMakeMove(move as InterfaceMove<U>)) movesList.add(move as Move<U>)
               }
            }
        }
        return movesList
    }

    private fun getValueOfBoard(board: InterfaceBoard<T, U>): Double {
        val tempBoard = board.getBoard()
        var valueCount = 0
        for (i in 0 until C.SIZE) {
            for (j in 0 until C.SIZE) {
                if (i == 0 || (i == 1 && j == 0) || (i == 1 && j == 4)) {
                    if (tempBoard[i][j].getString() == C.PLAYER_1_STRING) valueCount++
                } else if (i == 4 || (i == 3 && j == 0) || (i == 3 && j == 4)) {
                    if (tempBoard[i][j].getString() == C.PLAYER_2_STRING) valueCount--
                }
            }
        }
        return valueCount.toDouble()
    }

    override fun onUserClickedLocation(location: U) {
    }
}