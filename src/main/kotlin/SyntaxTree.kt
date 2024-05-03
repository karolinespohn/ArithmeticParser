sealed class Exp{
    object Element: Exp() {
        override fun toString(): String = "element"
    }
    data class BinExp(val left : Exp, val operation: Operation, val right : Exp): Exp()
    data class ConstExp(val value : Int) : Exp()
}

enum class Operation {
    Plus, Minus, Times
}
