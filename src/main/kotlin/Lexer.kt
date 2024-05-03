import java.lang.IllegalArgumentException

sealed class Token{
    data class Number(val value : Int) : Token()
    object Plus : Token()
    object Minus : Token()
    object Times : Token()
    object OpenParen : Token()
    object ClosedParen : Token()
    object Element : Token()
    object EOF : Token()
}

class Lexer(val text : String) {
    var pointer = 0

    fun scanText() : List<Token> {
        val tokens = mutableListOf<Token>()

        while (pointer < text.length) {
            val token = chooseToken()
            if (token != null) {
                tokens.add(token)
            }
        }
        tokens.add(Token.EOF)
        return tokens
    }

    private fun chooseToken() : Token? =
        when (text[pointer]) {
            ' ', '\n' -> null.also { pointer++ }
            '+' -> Token.Plus.also { pointer++ }
            '-' -> Token.Minus.also { pointer++ }
            '*' -> Token.Times.also { pointer++ }
            '(' -> Token.OpenParen.also { pointer++ }
            ')' -> Token.ClosedParen.also { pointer++ }
            'e' -> Token.Element.also { pointer += 7 }
            else   -> {
                val match = "[0-9]*".toRegex().matchAt(text, pointer)
                    ?: throw IllegalArgumentException()

                pointer += match.value.length
                Token.Number(match.value.toInt())
            }
        }

}

