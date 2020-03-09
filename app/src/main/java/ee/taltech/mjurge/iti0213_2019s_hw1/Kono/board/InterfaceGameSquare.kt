package ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board

import java.io.Serializable

interface InterfaceGameSquare: Serializable {

    fun getPosition(): InterfacePosition?
    fun initPosition(position: InterfacePosition)
    fun isSelected(): Boolean
    fun select()
    fun deSelect()
    fun getString(): String
    fun setString(string: String)

}