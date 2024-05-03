import java.lang.IllegalArgumentException

class Parser (text : String){
    val tokens = Lexer(text).scanText()
    var pointer = 0

    fun peek() : Token = tokens[pointer]
    fun consume() : Token = peek().also { pointer++ }

    fun expression() : Exp =
        when(peek()) {
            Token.Element -> Exp.Element.also { pointer++ }
            Token.OpenParen -> binExp()
            else -> constExp()
        }

    fun binExp() : Exp.BinExp {
        consume() // for open paren
        val left = expression()
        val operation = when (consume()) {
            Token.Plus -> Operation.Plus
            Token.Minus -> Operation.Minus
            Token.Times -> Operation.Times
            else -> throw IllegalArgumentException()
        }
        val right = expression()
        consume() // for closed paren

        return Exp.BinExp(operation, left, right)
    }

    fun constExp() : Exp.ConstExp {
        val start = consume()
        if (start == Token.Minus) {
            val number = consume()
            if (number is Token.Number) return Exp.ConstExp(-number.value)

            throw IllegalArgumentException()
        }

        if (start is Token.Number) return Exp.ConstExp(start.value)

        throw IllegalArgumentException()
    }

}