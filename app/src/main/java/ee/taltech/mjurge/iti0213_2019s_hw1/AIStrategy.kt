package ee.taltech.mjurge.iti0213_2019s_hw1

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AIStrategy() {

    lateinit var boardStrings: ArrayList<ArrayList<String>>
    lateinit var bestMove: HashMap<Pair<Int,Int>, Pair<Int,Int>>
    private var myPlayingButtonString = C.PLAYER_1_STRING
    private var enemysPlayingButtonString = C.PLAYER_2_STRING
    private var possibleMoves = HashMap<HashMap<Pair<Int,Int>, Pair<Int,Int>>, Int>()
    private var movableButtons = ArrayList<Pair<Int, Int>>()

    fun getMove(board: ArrayList<ArrayList<String>>, player1Turn: Boolean): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        boardStrings = board
        declareButtonStrings(player1Turn)
        possibleMoves = getPossibleMoves()
        MCTS()
        return getBestMove()
    }

    fun MCTS() {
        for (move in possibleMoves) {
            for (i in 0..200) {
                var result = simulate(boardStrings.toArray() as ArrayList<ArrayList<String>>, move.key)
                possibleMoves[move.key] = move.value + result
            }
        }
    }

    fun simulate(board: ArrayList<ArrayList<String>>, move: HashMap<Pair<Int,Int>, Pair<Int,Int>>): Int {
        return 1
    }

    fun getBestMove(): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        var bestScore = -10000
        for (move in possibleMoves) {
            if (!this::bestMove.isInitialized) {
                bestMove = move.key
                bestScore = move.value
            } else if (bestScore < move.value) {
                bestMove = move.key
                bestScore = move.value
            }
        }
        return Pair(bestMove.keys.first(), bestMove[bestMove.keys.first()] as Pair<Int, Int>)
    }

    fun getMovableButtonsCords(): ArrayList<Pair<Int, Int>> {
        var movables = ArrayList<Pair<Int, Int>>()
        for (i in 0 until C.SIZE) {
            for (j in 0 until C.SIZE) {
                if (boardStrings[i][j] == myPlayingButtonString) movables.add(Pair(i, j))
            }
        }
        return movables
    }

    fun getPossibleMoves(): HashMap<HashMap<Pair<Int,Int>, Pair<Int,Int>>, Int> {
        movableButtons = getMovableButtonsCords()
        var allMoves = HashMap<HashMap<Pair<Int,Int>, Pair<Int,Int>>, Int>()
        for (cordPair in movableButtons) {
            for (move in getMovesForButton(cordPair)) {
                allMoves[move as HashMap<Pair<Int, Int>, Pair<Int, Int>>] = 0
            }
        }
        return allMoves
    }

    fun getMovesForButton(cords: Pair<Int, Int>): HashMap<Pair<Int, Int>, Pair<Int, Int>> {
        var possibleMoves = HashMap<Pair<Int, Int>, Pair<Int, Int>>()
        var move = Pair(cords.first + 1, cords.second + 1)
        if (checkMovesPossibility(cords, move)) possibleMoves[cords] = move
        move = Pair(cords.first + 1, cords.second - 1)
        if (checkMovesPossibility(cords, move)) possibleMoves[cords] = move
        move = Pair(cords.first - 1, cords.second + 1)
        if (checkMovesPossibility(cords, move)) possibleMoves[cords] = move
        move = Pair(cords.first - 1, cords.second - 1)
        if (checkMovesPossibility(cords, move)) possibleMoves[cords] = move
        return possibleMoves
    }

    fun checkMovesPossibility(firstCords: Pair<Int, Int>, secondCords: Pair<Int, Int>): Boolean {
        if (areCordsInPlayField(secondCords)
            && boardStrings[secondCords.first][secondCords.second] == "") {
            return true
        }
        return false
    }

    fun areCordsInPlayField(cords: Pair<Int, Int>): Boolean {
        return cords.first in 0..4 && cords.second in 0..4
    }

    private fun declareButtonStrings(player1Turn: Boolean) {
        if (player1Turn) {
            myPlayingButtonString = C.PLAYER_1_STRING
            enemysPlayingButtonString = C.PLAYER_2_STRING
        } else {
            myPlayingButtonString = C.PLAYER_2_STRING
            enemysPlayingButtonString = C.PLAYER_1_STRING
        }
    }
}