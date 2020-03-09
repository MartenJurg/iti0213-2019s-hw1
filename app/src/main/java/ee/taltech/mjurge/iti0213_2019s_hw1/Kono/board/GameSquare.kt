package ee.taltech.mjurge.iti0213_2019s_hw1.Kono.board

class GameSquare: InterfaceGameSquare {

    private var isSelected = false
    private var location: InterfacePosition? = null
    private var squareString = ""

    override fun getPosition(): InterfacePosition? {
        return location
    }

    override fun initPosition(position: InterfacePosition) {
        if (location != null) {
            location = position
        }
    }

    override fun isSelected(): Boolean {
        return isSelected
    }

    override fun select() {
        isSelected = true
    }

    override fun deSelect() {
        isSelected = false
    }

    override fun getString(): String {
        return squareString
    }

    override fun setString(string: String) {
        squareString = string
    }


}