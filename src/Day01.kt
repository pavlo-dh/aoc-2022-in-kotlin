fun main() {
    fun calculateElvesCalories(input: List<String>): List<Int> {
        val elvesCalories = arrayListOf<Int>()
        var currentElfCalories = 0
        input.forEach {
            if (it.isEmpty()) {
                elvesCalories.add(currentElfCalories)
                currentElfCalories = 0
            } else {
                currentElfCalories += it.toInt()
            }
        }
        elvesCalories.add(currentElfCalories)
        return elvesCalories
    }

    fun part1(input: List<String>): Int {
        val elvesCalories = calculateElvesCalories(input)
        return elvesCalories.max()
    }

    fun part2(input: List<String>): Int {
        val elvesCalories = calculateElvesCalories(input)
        return elvesCalories.sortedDescending().take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
