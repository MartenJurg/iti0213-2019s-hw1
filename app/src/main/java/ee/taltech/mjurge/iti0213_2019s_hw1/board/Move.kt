package ee.taltech.mjurge.iti0213_2019s_hw1.board

class Move<T: InterfacePosition>(private val from: T, private val to: T):
    InterfaceMove<T> {

    override fun from(): T {
        return from
    }

    override fun to(): T {
        return to
    }

    override fun toString(): String {
        return "Move $from -> $to)"
    }
}