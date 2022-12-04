fun main() {
    fun parseData(input: List<String>) = input.map { line ->
        val (firstFrom, firstTo, secondFrom, secondTo) = line.split('-', ',').map(String::toInt)
        firstFrom..firstTo to secondFrom..secondTo
    }

    fun part1(input: List<String>): Long {
        val data = parseData(input)
        return data.sumOf { pair ->
            val firstSet = pair.first.toSet()
            val secondSet = pair.second.toSet()
            if (firstSet.containsAll(secondSet) || secondSet.containsAll(firstSet)) 1L else 0L
        }
    }

    fun part2(input: List<String>): Long {
        val data = parseData(input)
        return data.sumOf { pair ->
            val firstSet = pair.first.toSet()
            val secondSet = pair.second.toSet()
            if (firstSet.intersect(secondSet).isNotEmpty()) 1L else 0L
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part2(testInput) == 4L)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
