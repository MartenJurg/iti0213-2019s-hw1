package ee.taltech.mjurge.iti0213_2019s_hw1.Player

import ee.taltech.mjurge.iti0213_2019s_hw1.GameSquare
import ee.taltech.mjurge.iti0213_2019s_hw1.InterfaceGameSquare
import ee.taltech.mjurge.iti0213_2019s_hw1.board.*
import java.io.Serializable

class AIPlayer<T: InterfaceGameSquare, U: InterfacePosition>(private var playerString: String):
    InterfacePlayer<T, U>, Serializable {
    override suspend fun getMove(board: InterfaceBoard<T, U>): InterfaceMove<U> {
        return Move(Position(1, 2), Position(2,3)) as InterfaceMove<U>
    }

    override fun onUserClickedLocation(location: U) {
    }
}