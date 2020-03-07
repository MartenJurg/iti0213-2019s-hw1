package ee.taltech.mjurge.iti0213_2019s_hw1.Player

import ee.taltech.mjurge.iti0213_2019s_hw1.GameSquare
import ee.taltech.mjurge.iti0213_2019s_hw1.InterfaceGameSquare
import ee.taltech.mjurge.iti0213_2019s_hw1.board.InterfaceBoard
import ee.taltech.mjurge.iti0213_2019s_hw1.board.InterfaceMove
import ee.taltech.mjurge.iti0213_2019s_hw1.board.InterfacePosition
import ee.taltech.mjurge.iti0213_2019s_hw1.board.Position
import java.io.Serializable

class AIPlayer<T: InterfaceGameSquare, U: InterfacePosition>(private var playerString: String):
    InterfacePlayer<T, U>, Serializable {
    override suspend fun getMove(board: InterfaceBoard<T, U>): InterfaceMove<U> {

    }

    override fun onUserClickedLocation(location: U) {
    }
}