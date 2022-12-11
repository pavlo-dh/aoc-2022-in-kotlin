fun main() {
    class Monkey(
        val items: ArrayDeque<Long>,
        val operation: Long.(Long) -> Long,
        val operand: Long?,
        val testConstant: Long,
        val testPassesMonkeyNumber: Int,
        val testFailsMonkeyNumber: Int,
        var itemsInspected: Long = 0L
    )

    fun parseInput(input: List<String>): Map<Int, Monkey> = input.chunked(7).associate { monkeyInput ->
        val number = monkeyInput[0][7].digitToInt()
        val items = ArrayDeque(monkeyInput[1].substringAfter("items: ").split(", ").map(String::toLong))
        val (operator, operandString) = monkeyInput[2].substringAfter("old ").split(' ')
        val operation: Long.(Long) -> Long = if (operator == "*") Long::times else Long::plus
        val operand = if (operandString != "old") operandString.toLong() else null
        val testConstant = monkeyInput[3].substringAfter("by ").toLong()
        val testPassesMonkeyNumber = monkeyInput[4].substringAfter("monkey ").toInt()
        val testFailsMonkeyNumber = monkeyInput[5].substringAfter("monkey ").toInt()
        number to Monkey(items, operation, operand, testConstant, testPassesMonkeyNumber, testFailsMonkeyNumber)
    }

    fun Monkey.performOperation(old: Long): Long {
        val operand = operand ?: old
        return old.operation(operand)
    }

    fun calculateMonkeyBusinessLevel(
        monkeys: Map<Int, Monkey>, repetitions: Int, reduceWorryLevel: (worryLevel: Long) -> Long
    ): Long {
        repeat(repetitions) {
            monkeys.forEach { (_, monkey) ->
                repeat(monkey.items.size) {
                    val item = monkey.items.removeFirst()
                    monkey.itemsInspected++
                    var worryLevel = monkey.performOperation(item)
                    worryLevel = reduceWorryLevel(worryLevel)
                    val nextMonkeyNumber = if (worryLevel % monkey.testConstant == 0L) {
                        monkey.testPassesMonkeyNumber
                    } else {
                        monkey.testFailsMonkeyNumber
                    }
                    monkeys.getValue(nextMonkeyNumber).items.addLast(worryLevel)
                }
            }
        }
        return monkeys.values.sortedByDescending(Monkey::itemsInspected).take(2).let {
            it.first().itemsInspected * it.last().itemsInspected
        }
    }

    fun part1(input: List<String>): Long {
        val monkeys = parseInput(input)
        return calculateMonkeyBusinessLevel(monkeys, 20) { worryLevel ->
            worryLevel / 3
        }
    }

    fun gcd(a: Long, b: Long): Long = if (a == 0L) b else gcd(b % a, a)

    fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

    fun List<Long>.lcm(): Long = fold(first()) { result, element ->
        lcm(result, element)
    }

    fun part2(input: List<String>): Long {
        val monkeys = parseInput(input)
        val testConstantsLCM = monkeys.values.map(Monkey::testConstant).lcm()
        return calculateMonkeyBusinessLevel(monkeys, 10000) { worryLevel ->
            worryLevel % testConstantsLCM
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
