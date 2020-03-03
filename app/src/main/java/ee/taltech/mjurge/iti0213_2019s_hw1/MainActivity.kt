package ee.taltech.mjurge.iti0213_2019s_hw1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs


class MainActivity : AppCompatActivity(),  View.OnClickListener  {

    companion object {
        const val SIZE = 5
        const val PlayerVsPlayer = "Player vs Player"
        const val PlayerVsAI = "Player vs AI"
        const val AIVsAI = "AI vs AI"
        const val Player1 = "X"
        const val Player2 = "O"
        const val Restart = "Restart"
        const val Start = "Start"
        const val ButtonStartIndexColumn = 6
        const val ButtonEndIndexColumn = 7
        const val ButtonStartIndexRow = 7
        const val ButtonEndIndexRow = 8
    }

    val gameModes = arrayOf(PlayerVsAI, PlayerVsPlayer, AIVsAI)

    private var isGamePlaying = false
    private var player1Turn = true
    private var isPlayerTurn = true
    private var isButtonChosen = false

    lateinit var firstChosenButton: Button
    lateinit var secondChosenButton: Button
    lateinit var winner: String

    lateinit var whosTurnTextView : TextView
    lateinit var gameModeSpinner: Spinner
    lateinit var startingPlayerSwitch: Switch
    var gameMode = PlayerVsAI

    private var buttonsTextViews = ArrayList<ArrayList<Button>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameModeSpinner = findViewById(R.id.spinnerGameMode)
        startingPlayerSwitch = findViewById(R.id.switchStartingPlayer)
        gameModeSpinner.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gameModes)

        whosTurnTextView = findViewById<TextView>(R.id.textViewWhosTurn)

        for (i in 0 until SIZE) {
            buttonsTextViews.add(ArrayList<Button>())

            for (j in 0 until SIZE) {
                var buttonId  = "button$i$j"
                var resId = resources.getIdentifier(buttonId, "id", packageName)
                buttonsTextViews[i].add(findViewById<Button>(resId))
                buttonsTextViews[i][j].setOnClickListener(this)
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onClick(v: View?) {
        if (!isGamePlaying || !isPlayerTurn) return
        var buttonText = ((v as Button).text.toString())
        if (!isButtonChosen) {
            if (player1Turn && buttonText == Player1) {
                firstChosenButton = v
                v.setBackgroundResource(R.drawable.clicked_playground_tile)
                isButtonChosen = true
                return
            } else if (!player1Turn && buttonText == Player2) {
                firstChosenButton = v
                v.setBackgroundResource(R.drawable.clicked_playground_tile)
                isButtonChosen = true
                return
            }
        } else if (isButtonChosen && firstChosenButton.equals(v as Button) ) {
            v.setBackgroundResource(R.drawable.playground_tile)
            isButtonChosen = false
            return
        } else {
            secondChosenButton = v
            if (isChosenButtonMovable()) {
                makeMove()
                isButtonChosen = false
                firstChosenButton.setBackgroundResource(R.drawable.playground_tile)

                if (checkBoardForWinner()) {
                    changeTurnTextViewToWinner()
                    isGamePlaying = false
                    findViewById<TextView>(R.id.buttonStartGame).text = Start
                } else {
                    player1Turn = !player1Turn
                    changeTurnTextView()
                }
            }
        }
    }

    fun checkBoardForWinner(): Boolean {
        var player1Count = 0
        var player2Count = 0
        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                if (i == 0 || (i == 1 && j == 0) || (i == 1 && j == 4)) {
                    if (buttonsTextViews[i][j].text == Player1) player1Count++
                } else if (i == 4 || (i == 3 && j == 0) || (i == 3 && j == 4)) {
                    if (buttonsTextViews[i][j].text == Player2) player2Count++
                }
            }
        }
        if (player1Count == 7){
            winner = Player1
            return true
        } else if (player2Count == 7) {
            winner = Player2
            return true
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    fun changeTurnTextView() {
        whosTurnTextView.text = "Currently " + (if (player1Turn) Player1 else Player2) + "`s turn"
    }

    @SuppressLint("SetTextI18n")
    fun changeTurnTextViewToWinner() {
        var winnerString = ""
        if (winner == Player1) winnerString = "Player 1"
        else winnerString = "Player 2"
        whosTurnTextView.text = winnerString + " won the game!"
    }

    fun makeMove() {
        if (player1Turn) secondChosenButton.text = Player1
        else secondChosenButton.text = Player2
        firstChosenButton.text = ""
    }

    fun isChosenButtonMovable(): Boolean {
        var firstButtonName =
            firstChosenButton.context.resources.getResourceEntryName(firstChosenButton.id)
        var firstButtonCol = firstButtonName.substring(ButtonStartIndexColumn, ButtonEndIndexColumn).toInt()
        var firstButtonRow = firstButtonName.substring(ButtonStartIndexRow, ButtonEndIndexRow).toInt()

        var secondButtonName =
            secondChosenButton.context.resources.getResourceEntryName(secondChosenButton.id)
        var secondButtonCol = secondButtonName.substring(ButtonStartIndexColumn, ButtonEndIndexColumn).toInt()
        var secondButtonRow = secondButtonName.substring(ButtonStartIndexRow, ButtonEndIndexRow).toInt()

        if (abs(firstButtonCol - secondButtonCol) == 1
            && abs(firstButtonRow - secondButtonRow) == 1
            && secondChosenButton.text.toString() == "") {
            return true
        }
        return false
    }

    private fun resetBoard() {
        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                if (i == 0 || (i == 1 && j == 0) || (i == 1 && j == 4)) {
                    buttonsTextViews[i][j].text = Player2
                } else if (i == 4 || (i == 3 && j == 0) || (i == 3 && j == 4)) {
                    buttonsTextViews[i][j].text = Player1
                } else {
                    buttonsTextViews[i][j].text = ""
                }
            }
        }
    }

    fun onClickStart(v: View?) {
        if (!isGamePlaying) {
            gameMode = gameModeSpinner.selectedItem.toString()
            player1Turn = !startingPlayerSwitch.isChecked
            isGamePlaying = true
            resetBoard()
            changeTurnTextView()
            (v as Button).text = Restart
        } else {
            gameMode = gameModeSpinner.selectedItem.toString()
            player1Turn = !startingPlayerSwitch.isChecked
            changeTurnTextView()
            resetBoard()
            isButtonChosen = false
            if (this::firstChosenButton.isInitialized) {
                firstChosenButton.setBackgroundResource(R.drawable.playground_tile)
            }
        }

    }


}
