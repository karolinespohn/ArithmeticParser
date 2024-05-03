import java.io.File

fun parse(text : String) = Parser(text).parse()

fun main(args: Array<String>) {
    val text = File(args[0]).readText()

    try {
        println(parse(text))
    } catch (e : ParsingException) {
        System.err.println(e.toString())
    }

}