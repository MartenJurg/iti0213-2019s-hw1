package ee.taltech.mjurge.iti0213_2019s_hw1

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.mjurge.iti0213_2019s_hw1.board.Position
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(),  View.OnClickListener {

    companion object {
        var isGamePlaying = false
        var gameModes = arrayOf(C.PlayerVsAI, C.PlayerVsPlayer, C.AIVsAI)
        var gameMode = C.PlayerVsAI
    }

    private lateinit var whosTurnTextView : TextView
    private lateinit var startGameButton : TextView
    private lateinit var gameModeSpinner: Spinner
    private lateinit var startingPlayerSwitch: Switch
    private var handler: Handler = Handler()
    private var player1Turn = true
    private lateinit var gameSession: GameSession
    private var buttonsTextViews = ArrayList<ArrayList<TextView>>()
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initTextFieldsAndButtons()
        makeButtonTextViews()
    }

    private fun initTextFieldsAndButtons() {
        gameModeSpinner = findViewById(R.id.spinnerGameMode)
        startingPlayerSwitch = findViewById(R.id.switchStartingPlayer)
        gameModeSpinner.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gameModes)

        whosTurnTextView = findViewById<TextView>(R.id.textViewWhosTurn)
        startGameButton = findViewById<TextView>(R.id.buttonStartGame)
    }

    private fun makeButtonTextViews() {
        for (i in 0 until C.SIZE) {
            buttonsTextViews.add(ArrayList<TextView>())
            for (j in 0 until C.SIZE) {
                var buttonId  = "button$i$j"
                var resId = resources.getIdentifier(buttonId, "id", packageName)
                buttonsTextViews[i].add(findViewById<Button>(resId))
                buttonsTextViews[i][j].setOnClickListener(this)
            }
        }
    }

    fun onClickStart(v: View?) {
        gameMode = gameModeSpinner.selectedItem.toString()
        player1Turn = !startingPlayerSwitch.isChecked
        if (isGamePlaying) {
            gameSession.restart()
            coroutineScope.launch {
                gameSession.play(player1Turn)
            }
        } else {
            gameSession = GameSession(buttonsTextViews, gameMode, player1Turn)
            updateBoard(gameSession.board.getBoard())
            coroutineScope.launch {
                gameSession.play(player1Turn)
            }

        }
    }


    override fun onClick(v: View) {


        val id = v.id
        val idString = v.resources.getResourceName(id)
        val col = idString.substring(idString.lastIndex).toInt()
        val row = idString.substring(idString.lastIndex - 1, idString.lastIndex).toInt()
        Log.d("---", row.toString())
        Log.d("---", col.toString())
        if (isGamePlaying) {
            gameSession.onButtonClick(Position(row, col))
        }
    }

    private fun findViewPos(v: View): Position? {
        for (i in 0 until C.SIZE) {
            for (j in 0 until C.SIZE) {
                buttonsTextViews[i][j].id == v.id
                return Position(i, j)
            }
        }
        return null
    }

    @SuppressLint("SetTextI18n")
    private fun updateTextViewWhosTurn() {
        val tempBoard = gameSession.board
        if (tempBoard.getWinner() == C.PLAYER_1_STRING || gameSession.board.getWinner() == C.PLAYER_2_STRING) {
            var winnerString = ""
            winnerString = if (tempBoard.getWinner() == C.PLAYER_1_STRING) "Player 1" else "Player 2"
            whosTurnTextView.text = winnerString + " won the game!"
            startGameButton.text = C.Start
        } else {
            whosTurnTextView.text = "Currently " + (if (tempBoard.getPlayer1Turn()) C.PLAYER_1_STRING else C.PLAYER_2_STRING) + "`s turn"
        }
    }

    private fun updateBoard(tempBoard: Array<Array<GameSquare>>) {
        for (i in 0 until C.SIZE) {
            for (j in 0 until C.SIZE) {
                buttonsTextViews[i][j].text = tempBoard[i][j].getString()
                if (tempBoard[i][j].isSelected()) {
                    buttonsTextViews[i][j].setBackgroundResource(R.drawable.clicked_playground_tile)
                } else {
                    buttonsTextViews[i][j].setBackgroundResource(R.drawable.playground_tile)
                }
            }
        }

        handler.postDelayed({
            updateBoard(gameSession.board.getBoard())
            updateTextViewWhosTurn()
        }, C.BOARD_REFRESH_DELAY)
    }
}
