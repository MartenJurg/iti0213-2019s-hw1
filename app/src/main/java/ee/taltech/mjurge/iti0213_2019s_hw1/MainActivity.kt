package ee.taltech.mjurge.iti0213_2019s_hw1

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.Constants.C
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.GameSession
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board.GameSquare
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board.Position
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(),  View.OnClickListener {

    private var gameModes = arrayOf(C.PlayerVsAI, C.PlayerVsPlayer, C.AIVsAI)
    private var gameMode = C.PlayerVsAI

    private lateinit var whosTurnTextView : TextView
    private lateinit var startGameButton : TextView
    private lateinit var gameModeSpinner: Spinner
    private lateinit var startingPlayerSwitch: Switch
    private var handler: Handler = Handler()
    private var player1Turn = true
    private var gameSession: GameSession? = null
    private var buttonsTextViews = ArrayList<ArrayList<TextView>>()
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initTextFieldsAndButtons()
        makeButtonTextViews()

        gameSession = savedInstanceState?.getSerializable(C.GAME_SESSION_KEY) as GameSession?
        if (gameSession != null) {
            updateBoard(gameSession!!.board.getBoard())
            startGameButton.text = C.RESTART_STRING
            coroutineScope.launch {
                gameSession!!.keepPlaying()

            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putSerializable(C.GAME_SESSION_KEY, gameSession)
        }
        super.onSaveInstanceState(outState)
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
        if (gameSession != null) {
            gameSession!!.kill()
        }
        gameSession = GameSession(
            buttonsTextViews,
            gameMode,
            player1Turn
        )
        startGameButton.text = C.RESTART_STRING
        updateBoard(gameSession!!.board.getBoard())
        coroutineScope.launch {
            gameSession!!.play(player1Turn)
        }
    }

    override fun onClick(v: View) {
        val idString = v.resources.getResourceName(v.id)
        val col = idString.substring(idString.lastIndex).toInt()
        val row = idString.substring(idString.lastIndex - 1, idString.lastIndex).toInt()
        if (gameSession != null && gameSession!!.isGamePlaying) {
            gameSession!!.onButtonClick(Position(row, col))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTextViewWhosTurn() {
        val tempBoard = gameSession!!.board
        if (tempBoard.isGameOver()) {
            if (tempBoard.getWinner() == null) {
                whosTurnTextView.text = C.DRAW_STRING
            } else {
                var winnerString = ""
                winnerString = if (tempBoard.getWinner() == C.PLAYER_1_STRING) C.PLAYER_1 else C.PLAYER_2
                whosTurnTextView.text = winnerString + C.WON_GAME_ENDING
                startGameButton.text = C.START_STRING
            }
        } else {
            whosTurnTextView.text = C.WHOS_TURN_BEGINNING + (if (tempBoard.getPlayer1Turn()) C.PLAYER_1_STRING else C.PLAYER_2_STRING) + C.WHOS_TURN_ENDING
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
            updateBoard(gameSession!!.board.getBoard())
            updateTextViewWhosTurn()
        }, C.BOARD_REFRESH_DELAY)
    }
}
