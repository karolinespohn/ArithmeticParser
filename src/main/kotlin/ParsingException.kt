class ParsingException(val type : String,
                       override val message : String,
                       val programText : String,
                       val position : Int) : RuntimeException() {
    override fun toString(): String =
        """$type error: $message 
        $programText
        ${" ".repeat(position) + "^ (at char $position)"}
        """


}