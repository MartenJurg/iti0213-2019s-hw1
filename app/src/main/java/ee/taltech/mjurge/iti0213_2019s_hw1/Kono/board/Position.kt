package ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board

class Position(private val row: Int, private val col: Int) :
    InterfacePosition {

    override fun getCol(): Int {
        return col
    }

    override fun getRow(): Int {
        return row
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (row != other.row) return false
        if (col != other.col) return false

        return true
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + col
        return result
    }

    override fun toString(): String {
        return "($row, $col)"
    }
}