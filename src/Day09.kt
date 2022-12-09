import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt

fun main() {
    data class Point(val x: Int, val y: Int)

    fun parseMovement(inputLine: String): Triple<Int, Int, Int> {
        val (direction, steps) = inputLine.split(' ')
        val (headXMovement, headYMovement) = when (direction.first()) {
            'R' -> 1 to 0
            'L' -> -1 to 0
            'U' -> 0 to 1
            'D' -> 0 to -1
            else -> error("Wrong direction!")
        }
        return Triple(steps.toInt(), headXMovement, headYMovement)
    }

    fun movementNeeded(previous: Point, current: Point) =
        sqrt((current.x - previous.x).toDouble().pow(2) + (current.y - previous.y).toDouble().pow(2)).toInt() > 1

    fun move(previous: Point, current: Point, previousXMovement: Int, previousYMovement: Int) = when {
        previous.x == current.x -> Point(current.x, current.y + previousYMovement)
        previous.y == current.y -> Point(current.x + previousXMovement, current.y)
        else -> Point(current.x + (previous.x - current.x).sign, current.y + (previous.y - current.y).sign)
    }

    fun part1(input: List<String>): Int {
        var head = Point(0, 0)
        var tail = head
        val visited = mutableSetOf(tail)
        input.forEach { inputLine ->
            val (steps, headXMovement, headYMovement) = parseMovement(inputLine)
            repeat(steps) {
                head = Point(head.x + headXMovement, head.y + headYMovement)
                if (movementNeeded(head, tail)) {
                    tail = move(head, tail, headXMovement, headYMovement)
                    visited.add(tail)
                }
            }
        }
        return visited.size
    }

    fun part2(input: List<String>): Int {
        var head = Point(0, 0)
        var rope = Array(10) { head }
        val visited = mutableSetOf(rope.last())
        input.forEach { inputLine ->
            val (steps, headXMovement, headYMovement) = parseMovement(inputLine)
            repeat(steps) {
                head = rope.first()
                val newHead = Point(head.x + headXMovement, head.y + headYMovement)
                val newRope = rope.copyOf()
                newRope[0] = newHead
                for (i in 1..9) {
                    val previous = newRope[i - 1]
                    val previousNotMoved = rope[i - 1]
                    val previousXMovement = previous.x - previousNotMoved.x
                    val previousYMovement = previous.y - previousNotMoved.y
                    val current = newRope[i]
                    if (movementNeeded(previous, current)) {
                        newRope[i] = move(previous, current, previousXMovement, previousYMovement)
                        if (i == 9) {
                            visited.add(newRope[i])
                        }
                    }
                }
                rope = newRope
            }
        }
        return visited.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)
    val testInput2 = readInput("Day09_test2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
