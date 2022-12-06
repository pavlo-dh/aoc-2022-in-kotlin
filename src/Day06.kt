fun main() {
    fun findMarkerEnd(signal: String, markerLength: Int): Int {
        var markerEnd = 0
        outer@ for (i in markerLength - 1 until signal.length) {
            val markerChars = mutableSetOf<Char>()
            for (j in i downTo i - (markerLength - 1)) {
                markerChars.add(signal[j])
            }
            if (markerChars.size == markerLength) {
                markerEnd = i + 1
                break@outer
            }
        }
        return markerEnd
    }

    fun part1(input: List<String>): Int = findMarkerEnd(input[0], 4)

    fun part2(input: List<String>): Int = findMarkerEnd(input[0], 14)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
