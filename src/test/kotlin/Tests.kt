import Exp.*
import Operation.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class Tests{
    @Test
    fun `test for errors`(){
        // Lexer Errors
        assertThrows<ParsingException> { parse("elephant") }
        assertThrows<ParsingException> { parse("älämänt") }
        assertThrows<ParsingException> { parse(",") }

        // Parser Errors
        assertThrows<ParsingException> {parse("")}
        assertThrows<ParsingException> {parse("(2 + 4)(1 + 2)")}
        assertThrows<ParsingException> {parse("(2 2 2)")}
        assertThrows<ParsingException> {parse("(2 2 2)")}
        assertThrows<ParsingException> {parse("(2 + 22")}
        assertThrows<ParsingException> {parse("(2 + --")}
        assertThrows<ParsingException> {parse("(2 + )")}

    }

    @Test
    fun `test element`() {
        assertEquals(Element, parse("element"))
    }

    @Test
    fun `test binary expressions`() {
        assertEquals(BinExp(ConstExp(1), Plus,
            BinExp(ConstExp(2), Times,
                BinExp(ConstExp(3), Minus, ConstExp(4)))), parse("(1 + (2 * (3 -4)))"))
        assertEquals(BinExp(ConstExp(-1), Plus, ConstExp(-2)), parse("(-1 + -2)"))
        assertEquals(BinExp(ConstExp(-1), Minus, ConstExp(-2)), parse("(-1 - -2)"))
    }

    @Test
    fun `test constant expression`() {
        assertEquals(ConstExp(1234567890), parse("1234567890"))
        assertEquals(ConstExp(1), parse("0000001"))
        assertEquals(ConstExp(0), parse("0000000"))
        assertEquals(ConstExp(0), parse("-0000000"))
        assertEquals(ConstExp(-1234567890), parse("-1234567890"))
    }


}