fun main() {
    fun part1(input: List<String>): Int {
        var cycle = 1
        var x = 1
        var signalStrength = 0
        val signalStrengthCycles = listOf(20, 60, 100, 140, 180, 220)

        fun checkSignalStrength() {
            if (cycle in signalStrengthCycles) signalStrength += cycle * x
        }

        fun noop() {
            checkSignalStrength()
            cycle++
        }

        fun add(line: String) {
            repeat(2) {
                noop()
            }
            val v = line.split(' ').last().toInt()
            x += v
        }

        input.forEach { line ->
            if (line.startsWith('n')) noop()
            else add(line)
        }

        return signalStrength
    }

    fun part2(input: List<String>): String {
        var cycle = 0
        var x = 1
        var spritePosition = 0..2
        val screen = Array(6) { CharArray(40) { '.' } }

        fun draw() {
            val row = cycle / 40
            val column = cycle - row * 40
            if (column in spritePosition) {
                screen[row][column] = '#'
            }
        }

        fun noop() {
            draw()
            cycle++
        }

        fun add(line: String) {
            repeat(2) {
                noop()
            }
            val v = line.split(' ').last().toInt()
            x += v
            spritePosition = x - 1..x + 1
        }

        fun printScreen() = buildString {
            for (i in screen.indices) {
                for (j in screen.first().indices) {
                    append(screen[i][j])
                }
                if (i != screen.lastIndex) appendLine()
            }
        }

        input.forEach { line ->
            if (line.startsWith('n')) noop()
            else add(line)
        }

        return printScreen()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    val part2Output = """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
    """.trimIndent()
    check(part2(testInput) == part2Output)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
