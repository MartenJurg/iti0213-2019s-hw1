package ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board

import java.io.Serializable


interface InterfaceBoard<T: InterfaceGameSquare, U : InterfacePosition>: Serializable {

    fun getBoard(): Array<Array<T>>
    fun deepCopy(): InterfaceBoard<T, U>
    fun makeMove(move: InterfaceMove<U>): Boolean
    fun canMakeMove(move: InterfaceMove<U>): Boolean
    fun setPlayer1Turn(player1Turn: Boolean)
    fun getPlayer1Turn(): Boolean
    fun getWinner(): String?
    fun isGameOver(): Boolean
    fun resetBoard()
    fun select(pos: U)
    fun deSelect(pos: U)
}