fun main() {
    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    }

    fun parseInput(input: List<String>): MutableMap<Point, Char> {
        val cave = mutableMapOf<Point, Char>()
        input.forEach { line ->
            val points = line.split(" -> ").map { pointCoordinatesString ->
                val (x, y) = pointCoordinatesString.split(',').map(String::toInt)
                Point(x, y)
            }
            for (i in 1..points.lastIndex) {
                val first = points[i - 1]
                val second = points[i]
                val xRange = when {
                    first.x == second.x -> first.x..first.x
                    second.x > first.x -> first.x..second.x
                    else -> second.x..first.x
                }
                val yRange = when {
                    first.y == second.y -> first.y..first.y
                    second.y > first.y -> first.y..second.y
                    else -> second.y..first.y
                }
                for (x in xRange) {
                    for (y in yRange) {
                        cave[Point(x, y)] = '#'
                    }
                }
            }
        }
        return cave
    }

    val sandUnitMovements = listOf(Point(0, 1), Point(-1, 1), Point(1, 1))

    fun part1(input: List<String>): Int {
        val cave = parseInput(input)
        val start = Point(500, 0)
        cave[start] = '+'
        val minX = cave.keys.minOf(Point::x)
        val maxX = cave.keys.maxOf(Point::x)
        val maxY = cave.keys.maxOf(Point::y)
        var allSandUnitsRested = false
        while (!allSandUnitsRested) {
            var sandUnit = start
            var sandUnitRested = false
            sandUnitFalling@ while (!sandUnitRested) {
                var sandUnitMoved = false
                for (sandUnitMovement in sandUnitMovements) {
                    val movedSandUnit = sandUnit + sandUnitMovement
                    if (movedSandUnit.x < minX || movedSandUnit.x > maxX || movedSandUnit.y > maxY) {
                        allSandUnitsRested = true
                        break@sandUnitFalling
                    } else if (cave[movedSandUnit] == null) {
                        sandUnit = movedSandUnit
                        sandUnitMoved = true
                        break
                    }
                }
                if (!sandUnitMoved) {
                    cave[sandUnit] = 'o'
                    sandUnitRested = true
                }
            }
        }
//        for (j in 0..maxY) {
//            for (i in minX..maxX) {
//                val point = Point(i, j)
//                print(cave[point] ?: '.')
//            }
//            println()
//        }
        return cave.values.count { it == 'o' }
    }

    fun part2(input: List<String>): Int {
        val cave = parseInput(input)
        val start = Point(500, 0)
        val maxY = cave.keys.maxOf(Point::y) + 2
        var allSandUnitsRested = false
        while (!allSandUnitsRested) {
            var sandUnit = start
            var sandUnitRested = false
            while (!sandUnitRested) {
                var sandUnitMoved = false
                for (sandUnitMovement in sandUnitMovements) {
                    val movedSandUnit = sandUnit + sandUnitMovement
                    if (movedSandUnit.y < maxY && cave[movedSandUnit] == null) {
                        sandUnit = movedSandUnit
                        sandUnitMoved = true
                        break
                    }
                }
                if (!sandUnitMoved) {
                    cave[sandUnit] = 'o'
                    sandUnitRested = true
                    if (sandUnit == start) {
                        allSandUnitsRested = true
                    }
                }
            }
        }
//        val minX = cave.keys.minOf(Point::x)
//        val maxX = cave.keys.maxOf(Point::x)
//        for (j in 0 until maxY) {
//            for (i in minX..maxX) {
//                val point = Point(i, j)
//                print(cave[point] ?: '.')
//            }
//            println()
//        }
        return cave.values.count { it == 'o' }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
