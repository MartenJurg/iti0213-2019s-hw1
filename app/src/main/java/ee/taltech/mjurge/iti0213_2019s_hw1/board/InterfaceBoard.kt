package ee.taltech.mjurge.iti0213_2019s_hw1.board

import ee.taltech.mjurge.iti0213_2019s_hw1.InterfaceGameSquare
import java.io.Serializable


interface InterfaceBoard<T: InterfaceGameSquare, U : InterfacePosition>: Serializable {

    fun getBoard(): Array<Array<T>>
    fun makeMove(move: InterfaceMove<U>): Boolean
    fun canMakeMove(move: InterfaceMove<U>): Boolean
    fun setPlayer1Turn(player1Turn: Boolean)
    fun getPlayer1Turn(): Boolean
    fun getWinner(): String?
    fun resetBoard()
    fun select(pos: U)
    fun deSelect(pos: U)
}