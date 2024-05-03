sealed class Exp{
    object Element: Exp()
    data class BinExp(val operation : Operation, val left : Exp, val right : Exp): Exp()
    data class ConstExp(val value : Int) : Exp()
}

enum class Operation {
    Plus, Minus, Times
}
