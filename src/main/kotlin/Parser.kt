import kotlin.system.exitProcess

class Parser (private val text : String){
    private val tokens = Lexer(text).scanText()
    private var pointer = 0

    private fun peek() : Token = tokens[pointer]
    private fun consume() : Token = peek().also { pointer++ }
    private inline fun <reified ExpectedTokenType : Token> consumeExpected(errorMessage : String) {
        if (consume() !is ExpectedTokenType) {
            handleError(errorMessage)
        }
    }

    fun parse() : Exp {
        val exp = expression()
        consumeExpected<Token.EOF>("Expected end of file")
        return exp
    }

    private fun expression() : Exp =
        when(peek()) {
            is Token.Element -> Exp.Element.also { pointer++ }
            is Token.OpenParen -> binExp()
            else -> constExp()
        }

    private fun binExp() : Exp.BinExp {
        consumeExpected<Token.OpenParen>("Expected \"(\"")
        val left = expression()
        val operation = when (consume()) {
            is Token.Plus -> Operation.Plus
            is Token.Minus -> Operation.Minus
            is Token.Times -> Operation.Times
            else -> handleError("Expected an operator (\"+\", \"-\" or \"*\")")
        }
        val right = expression()
        consumeExpected<Token.ClosedParen>("Expected \")\"")

        return Exp.BinExp(left, operation, right)
    }

    private fun constExp() : Exp.ConstExp {
        val start = consume()
        if (start is Token.Minus) {
            val number = consume()
            if (number is Token.Number) {
                return Exp.ConstExp(-number.value)
            } else {
                handleError("Expected a number")
            }

        }

        if (start is Token.Number) {
            return Exp.ConstExp(start.value)
        } else {
            handleError("Expected a number")
        }
    }

    private fun handleError(errorMessage : String) : Nothing {
        val charPos = tokens[pointer - 1].start

        System.err.println("Parser Error: $errorMessage")
        System.err.println(text)
        System.err.println(" ".repeat(charPos) + "^ (at char $charPos)")
        exitProcess(1)
    }

}