fun main() {
    fun String.getCrate(stackIndex: Int): Char? {
        val crateIndex = 4 * stackIndex + 1
        if (crateIndex > this.length) return null
        val crate = this[crateIndex]
        return if (crate.isWhitespace()) null else crate
    }

    fun createStacks(input: List<String>, inputDelimiterIndex: Int): List<ArrayDeque<Char>> {
        val stacks = input[inputDelimiterIndex - 1].split("   ").map { ArrayDeque<Char>() }
        for (i in inputDelimiterIndex - 2 downTo 0) {
            for (j in stacks.indices) {
                input[i].getCrate(j)?.let { crate -> stacks[j].addLast(crate) }
            }
        }
        return stacks
    }

    fun part1(input: List<String>): String {
        val inputDelimiterIndex = input.indexOfFirst(String::isEmpty)
        val stacks = createStacks(input, inputDelimiterIndex)
        for (i in inputDelimiterIndex + 1 until input.size) {
            val (_, amountString, _, fromString, _, toString) = input[i].split(' ')
            repeat(amountString.toInt()) {
                stacks[toString.toInt() - 1].addLast(stacks[fromString.toInt() - 1].removeLast())
            }
        }
        return stacks.map { it.last() }.joinToString(separator = "")
    }

    fun part2(input: List<String>): String {
        val inputDelimiterIndex = input.indexOfFirst(String::isEmpty)
        val stacks = createStacks(input, inputDelimiterIndex)
        for (i in inputDelimiterIndex + 1 until input.size) {
            val (_, amountString, _, fromString, _, toString) = input[i].split(' ')
            val movedCrated = ArrayDeque<Char>()
            repeat(amountString.toInt()) {
                movedCrated.addFirst(stacks[fromString.toInt() - 1].removeLast())
            }
            stacks[toString.toInt() - 1].addAll(movedCrated)
        }
        return stacks.map { it.last() }.joinToString(separator = "")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

private operator fun <E> List<E>.component6(): E = this[5]
