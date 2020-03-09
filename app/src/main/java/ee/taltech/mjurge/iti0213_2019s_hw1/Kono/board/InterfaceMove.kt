package ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board

import java.io.Serializable

interface InterfaceMove<T: InterfacePosition>: Serializable {
    fun from(): T
    fun to(): T
}