package ee.taltech.mjurge.iti0213_2019s_hw1.board

import java.io.Serializable

interface InterfacePosition: Serializable {

    fun getCol(): Int

    fun getRow(): Int
}