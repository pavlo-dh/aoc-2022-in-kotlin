fun main() {
    fun parseInput(input: List<String>) = input.map { line ->
        line.split(' ', limit = 2).map(String::first)
    }

    fun part1(input: List<String>): Int {
        fun shapeScore(shape: Char) = when (shape) {
            'X' -> 1
            'Y' -> 2
            else -> 3
        }

        fun roundScore(opponent: Char, player: Char) = when {
            opponent == player - 23 -> 3

            (player == 'X' && opponent == 'C' ||
                    player == 'Y' && opponent == 'A' ||
                    player == 'Z' && opponent == 'B') -> 6

            else -> 0
        }

        val data = parseInput(input)
        return data.fold(0) { totalScore, (opponent, player) ->
            totalScore + shapeScore(player) + roundScore(opponent, player)
        }
    }

    fun part2(input: List<String>): Int {
        fun roundScore(outcome: Char) = when (outcome) {
            'X' -> 0
            'Y' -> 3
            else -> 6
        }

        fun shapeScore(opponent: Char, outcome: Char): Int {
            val shape = when (outcome) {
                'Y' -> opponent

                'X' -> when (opponent) {
                    'A' -> 'C'
                    'B' -> 'A'
                    else -> 'B'
                }

                else -> when (opponent) {
                    'A' -> 'B'
                    'B' -> 'C'
                    else -> 'A'
                }
            }

            return when (shape) {
                'A' -> 1
                'B' -> 2
                else -> 3
            }
        }

        val data = parseInput(input)
        val result = data.fold(0) { totalScore, (opponent, outcome) ->
            totalScore + roundScore(outcome) + shapeScore(opponent, outcome)
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
