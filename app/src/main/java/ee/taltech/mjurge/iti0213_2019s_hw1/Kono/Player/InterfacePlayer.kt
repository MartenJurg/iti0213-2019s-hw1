package ee.taltech.mjurge.iti0213_2019s_hw1.Kono.Player

import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board.InterfaceGameSquare
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board.InterfaceBoard
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board.InterfaceMove
import ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board.InterfacePosition

interface InterfacePlayer<T: InterfaceGameSquare, U: InterfacePosition> {

    fun onUserClickedLocation(location: U)
    suspend fun getMove(board: InterfaceBoard<T, U>): InterfaceMove<U>

}