package ee.taltech.mjurge.iti0213_2019s_hw1.Player

import ee.taltech.mjurge.iti0213_2019s_hw1.InterfaceGameSquare
import ee.taltech.mjurge.iti0213_2019s_hw1.board.InterfaceBoard
import ee.taltech.mjurge.iti0213_2019s_hw1.board.InterfaceMove
import ee.taltech.mjurge.iti0213_2019s_hw1.board.InterfacePosition
import ee.taltech.mjurge.iti0213_2019s_hw1.board.Position

interface InterfacePlayer<T: InterfaceGameSquare, U: InterfacePosition> {

    fun onUserClickedLocation(location: U)
    suspend fun getMove(board: InterfaceBoard<T, U>): InterfaceMove<U>

}