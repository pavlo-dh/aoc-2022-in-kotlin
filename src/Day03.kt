fun main() {
    fun List<Char>.sumPriorities() = this.sumOf { type ->
        if (type.isLowerCase()) type - 'a' + 1
        else type - 'A' + 27
    }

    fun part1(input: List<String>): Int {
        val rucksacksCompartments = input.map { line ->
            val middleIndex = line.length / 2
            line.substring(0, middleIndex).toSet() to line.substring(middleIndex, line.length).toSet()
        }
        val itemTypes = rucksacksCompartments.fold(mutableListOf<Char>()) { itemTypes, (first, second) ->
            itemTypes.also { itemTypes.addAll(first.intersect(second)) }
        }
        return itemTypes.sumPriorities()
    }

    fun part2(input: List<String>): Int {
        val badgeItemTypes = input.chunked(3).flatMap { (first, second, third) ->
            first.toSet().intersect(second.toSet()).intersect(third.toSet())
        }
        return badgeItemTypes.sumPriorities()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
