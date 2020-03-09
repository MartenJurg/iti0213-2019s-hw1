package ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board

import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.Constants.C
import kotlin.math.abs

class GameBoard<U : InterfacePosition>(private var board: Array<Array<GameSquare>>, private var player1Turn: Boolean):
    InterfaceBoard<GameSquare, U> {

    private var winner: String? = null

    override fun getBoard(): Array<Array<GameSquare>> {
        var newBoard : Array<Array<GameSquare>> = Array(C.SIZE) { Array(
            C.SIZE) { GameSquare() } }
        for (i in 0 until C.SIZE) {
            for (j in 0 until C.SIZE) {
                newBoard[i][j].setString(board[i][j].getString())
            }
        }
        return newBoard
    }

    override fun makeMove(move: InterfaceMove<U>): Boolean {
        if (canMakeMove(move) && !isGameOver()) {
            val playerTag = board[move.from().getRow()][move.from().getCol()].getString()
            board[move.from().getRow()][move.from().getCol()].setString("")
            board[move.to().getRow()][move.to().getCol()].setString(playerTag)
            checkBoardForWinner()
            player1Turn = !player1Turn
            deSelectAll()
            return true
        }
        return false
    }

    override fun canMakeMove(move: InterfaceMove<U>): Boolean {
        if (moveInBoard(move)
            && moveIsDiagonal(move)
            && toMoveIsEmpty(move.to())
            && fromMoveCorrectPlayer(move.from())
            && winner == null) {
            return true
        }
        return false
    }

    private fun fromMoveCorrectPlayer(from: U): Boolean {
        val player = if (player1Turn) C.PLAYER_1_STRING else C.PLAYER_2_STRING
        return board[from.getRow()][from.getCol()].getString() == player
    }

    private fun toMoveIsEmpty(to: U): Boolean {
        return board[to.getRow()][to.getCol()].getString() == ""
    }

    private fun moveIsDiagonal(move: InterfaceMove<U>): Boolean {
        return abs(move.from().getRow() - move.to().getRow()) == 1
                && abs(move.from().getCol() - move.to().getCol()) == 1
    }

    private fun moveInBoard(move: InterfaceMove<U>): Boolean {
        return move.to().getCol() in 0..4
                && move.to().getRow() in 0..4
                && move.from().getCol() in 0..4
                && move.from().getRow() in 0..4
    }

    override fun setPlayer1Turn(player1Turn: Boolean) {
        this.player1Turn = player1Turn
    }

    override fun getPlayer1Turn(): Boolean {
        return player1Turn
    }

    override fun getWinner(): String? {
        return winner
    }

    private fun checkBoardForWinner(): Boolean {
        var player1Count = 0
        var player2Count = 0
        for (i in 0 until C.SIZE) {
            for (j in 0 until C.SIZE) {
                if (i == 0 || (i == 1 && j == 0) || (i == 1 && j == 4)) {
                    if (board[i][j].getString() == C.PLAYER_1_STRING) player1Count++
                } else if (i == 4 || (i == 3 && j == 0) || (i == 3 && j == 4)) {
                    if (board[i][j].getString() == C.PLAYER_2_STRING) player2Count++
                }
            }
        }
        if (player1Count == 7){
            winner = C.PLAYER_1_STRING
            return true
        } else if (player2Count == 7) {
            winner = C.PLAYER_2_STRING
            return true
        }
        return false
    }

    override fun resetBoard() {
        for (i in 0 until C.SIZE) {
            for (j in 0 until C.SIZE) {
                if (i == 0 || (i == 1 && j == 0) || (i == 1 && j == 4)) {
                    board[i][j].setString(C.PLAYER_2_STRING)
                } else if (i == 4 || (i == 3 && j == 0) || (i == 3 && j == 4)) {
                    board[i][j].setString(C.PLAYER_1_STRING)
                } else {
                    board[i][j].setString(C.EMPTY_STRING)

                }
            }
        }
        deSelectAll()
        winner = null
    }

    override fun select(pos: U) {
        board[pos.getRow()][pos.getCol()].select()
    }

    override fun deSelect(pos: U) {
        board[pos.getRow()][pos.getCol()].deSelect()
    }

    private fun deSelectAll() {
        for (i in 0 until C.SIZE) {
            for (j in 0 until C.SIZE) {
                board[i][j].deSelect()
            }
        }
    }

    override fun isGameOver(): Boolean {
        if (winner != null) return true
        else if (!canPlayerMove()) return true
        return false
    }

    private fun canPlayerMove(): Boolean {
        if (player1Turn && getPossibleMoves(C.PLAYER_1_STRING).size == 0) {
            return false
        } else if (!player1Turn && getPossibleMoves(C.PLAYER_2_STRING).size == 0) {
            return false
        }
        return true
    }

    private fun getPossibleMoves(playerString: String): ArrayList<Move<U>> {
        var movesList: ArrayList<Move<U>> = ArrayList<Move<U>>()

        for (i in 0 until C.SIZE) {
            for (j in 0 until C.SIZE) {
                if (board[i][j].getString() == playerString) {
                    var move = Move(Position(i, j), Position(i + 1, j + 1))
                    if (canMakeMove(move as InterfaceMove<U>)) movesList.add(move as Move<U>)
                    move = Move(Position(i, j), Position(i - 1, j + 1))
                    if (canMakeMove(move as InterfaceMove<U>)) movesList.add(move as Move<U>)
                    move = Move(Position(i, j), Position(i + 1, j - 1))
                    if (canMakeMove(move as InterfaceMove<U>)) movesList.add(move as Move<U>)
                    move = Move(Position(i, j), Position(i - 1, j - 1))
                    if (canMakeMove(move as InterfaceMove<U>)) movesList.add(move as Move<U>)
                }
            }
        }
        return movesList
    }

    override fun deepCopy(): InterfaceBoard<GameSquare, U> {
        return GameBoard(getBoard(), player1Turn)
    }
}