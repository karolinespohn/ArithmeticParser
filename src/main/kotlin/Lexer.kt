sealed class Token {
    abstract val start : Int
    data class Number(val value : Int, override val start: Int) : Token()
    data class Plus(override val start : Int) : Token()
    data class Minus(override val start: Int) : Token()
    data class Times(override val start: Int) : Token()
    data class OpenParen(override val start: Int) : Token()
    data class ClosedParen(override val start: Int) : Token()
    data class Element(override val start: Int) : Token()
    data class EOF(override val start: Int) : Token()
}

class Lexer(private val text : String) {
    private var pointer = 0

    fun scanText() : List<Token> {
        val tokens = mutableListOf<Token>()

        while (pointer < text.length) {
            val token = chooseToken()
            if (token != null) {
                tokens.add(token)
            }
        }
        tokens.add(Token.EOF(pointer))
        return tokens
    }

    private fun chooseToken() : Token? =
        when (text[pointer]) {
            ' ', '\n' -> null.also { pointer++ }
            '+' -> Token.Plus(pointer).also { pointer++ }
            '-' -> Token.Minus(pointer).also { pointer++ }
            '*' -> Token.Times(pointer).also { pointer++ }
            '(' -> Token.OpenParen(pointer).also { pointer++ }
            ')' -> Token.ClosedParen(pointer).also { pointer++ }
            'e' -> if ("element".toRegex().matchesAt(text, pointer)) {
                    val token = Token.Element(pointer)
                    pointer +=7
                    token
                } else {
                    errorHandling("The only textual token is \"element\"")
                }

            else   -> {
                val match = "[0-9]+".toRegex().matchAt(text, pointer)
                    ?: if ("[a-zA-Z]".toRegex().matchesAt(text, pointer)) {
                        errorHandling("The only textual token is \"element\"")
                    } else {
                        errorHandling("\"${text[pointer]}\" is not a valid char in this language")
                    }

                val token = Token.Number(match.value.toInt(), pointer)
                pointer += match.value.length
                token
            }
        }

    private fun errorHandling(errorMessage : String) : Nothing {
        throw ParsingException("Lexer", errorMessage, text, pointer)
    }

}