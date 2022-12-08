fun main() {
    fun part1(input: List<String>): Int {
        val visibleGrid = input.map { BooleanArray(it.length) }

        for (i in 1 until input.size - 1) {
            var highest = input[i][0]
            for (j in 1 until input.size - 1) {
                if (input[i][j] > highest && input[i][j] > input[i][j - 1]) {
                    visibleGrid[i][j] = true
                    highest = input[i][j]
                }
            }

            highest = input[i][input.lastIndex]
            for (j in input.size - 2 downTo 1) {
                if (input[i][j] > highest && input[i][j] > input[i][j + 1]) {
                    visibleGrid[i][j] = true
                    highest = input[i][j]
                }
            }
        }

        for (j in 1 until input.size - 1) {
            var highest = input[0][j]
            for (i in 1 until input.size - 1) {
                if (input[i][j] > highest && input[i][j] > input[i - 1][j]) {
                    visibleGrid[i][j] = true
                    highest = input[i][j]
                }
            }

            highest = input[input.lastIndex][j]
            for (i in input.size - 2 downTo 1) {
                if (input[i][j] > highest && input[i][j] > input[i + 1][j]) {
                    visibleGrid[i][j] = true
                    highest = input[i][j]
                }
            }
        }

        var visibleCount = 0
        for (i in input.indices) {
            for (j in input.indices) {
                if (i == 0 || i == input.size - 1 || j == 0 || j == input.size - 1) visibleGrid[i][j] = true
                if (visibleGrid[i][j]) visibleCount++
            }
        }

        return visibleCount
    }

    fun part2(input: List<String>): Int {
        fun findScore(i: Int, j: Int): Int {
            if (i == 0 || i == input.size || j == 0 || j == input.size) return 0
            var down = 0
            for (k in i + 1..input.lastIndex) {
                down++
                if (input[k][j] >= input[i][j]) break
            }
            var up = 0
            for (k in i - 1 downTo 0) {
                up++
                if (input[k][j] >= input[i][j]) break
            }
            var right = 0
            for (k in j + 1..input.lastIndex) {
                right++
                if (input[i][k] >= input[i][j]) break
            }
            var left = 0
            for (k in j - 1 downTo 0) {
                left++
                if (input[i][k] >= input[i][j]) break
            }
            return down * up * right * left
        }

        var highestScore = 0
        for (i in input.indices) {
            for (j in input.indices) {
                val score = findScore(i, j)
                if (score > highestScore) highestScore = score
            }
        }
        return highestScore
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
