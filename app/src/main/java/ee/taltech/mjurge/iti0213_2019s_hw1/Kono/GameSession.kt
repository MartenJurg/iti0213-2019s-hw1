package ee.taltech.mjurge.iti0213_2019s_hw1.Kono

import android.util.Log
import android.widget.TextView
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.Constants.C
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.Player.AIPlayer
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.Player.HumanPlayer
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.Player.InterfacePlayer
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.Serializable

class GameSession(private val boardViews: ArrayList<ArrayList<TextView>>,
                  private var gameMode: String,
                  private var player1Turn: Boolean): Serializable {

    private var player1: InterfacePlayer<InterfaceGameSquare,InterfacePosition>
    private var player2: InterfacePlayer<InterfaceGameSquare,InterfacePosition>
    var isGamePlaying = false
    val board: GameBoard<Position> = GameBoard(createBoardList(), player1Turn)

    init {
        when (gameMode) {
            C.AIVsAI -> {
                player1 = AIPlayer(C.PLAYER_1_STRING)
                player2 = AIPlayer(C.PLAYER_2_STRING)
            }
            C.PlayerVsPlayer -> {
                player1 = HumanPlayer(C.PLAYER_1_STRING)
                player2 = HumanPlayer(C.PLAYER_2_STRING)
            }
            else -> {
                player1 = HumanPlayer(C.PLAYER_1_STRING)
                player2 = AIPlayer(C.PLAYER_2_STRING)
            }
        }
    }

    suspend fun play(player1Turn: Boolean) {
        board.setPlayer1Turn(player1Turn)
        board.resetBoard()
        isGamePlaying = true
        playGameLoop()
    }

    suspend fun keepPlaying() {
        playGameLoop()
    }

    suspend private fun playGameLoop() {
        while (isGamePlaying) {
            if (board.isGameOver()) break
            val move = if (board.getPlayer1Turn())
                player1.getMove(board as InterfaceBoard<InterfaceGameSquare, InterfacePosition>)
            else player2.getMove(board as InterfaceBoard<InterfaceGameSquare, InterfacePosition>)

            if (isGamePlaying) board.makeMove(move as InterfaceMove<Position>)
            if (board.isGameOver()) {
                isGamePlaying = false
            }
        }
    }

    private fun createBoardList(): Array<Array<GameSquare>> {
        var gameBoard : Array<Array<GameSquare>> = Array(C.SIZE) { Array(
            C.SIZE) { GameSquare() } }
        for (i in 0 until C.SIZE) {
            for (j in 0 until C.SIZE) {
                gameBoard[i][j].setString(boardViews[i][j].text.toString())
                gameBoard[i][j].initPosition(Position(i, j))
            }
        }
        return gameBoard
    }

    fun onButtonClick(location: Position) {
        if (board.getPlayer1Turn()) {
            player1.onUserClickedLocation(location)
        } else {
            player2.onUserClickedLocation(location)
        }
    }

    fun kill() {
        isGamePlaying = false
    }
}