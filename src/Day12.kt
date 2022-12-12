fun main() {
    data class Point(val y: Int, val x: Int)

    val deltas = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

    fun mapElevation(c: Char) = when (c) {
        'S' -> 'a'
        'E' -> 'z'
        else -> c
    }

    fun MutableMap<Point, MutableList<Point>>.addEdge(from: Point, to: Point) {
        val fromEdges = computeIfAbsent(from) { mutableListOf() }
        fromEdges.add(to)
    }

    fun MutableMap<Point, MutableList<Point>>.bfs(start: Point, end: Point): Int? {
        val visited = mutableSetOf(start)
        val queue = ArrayDeque<Point>()
        queue.addLast(start)
        val depths = mutableMapOf(start to 0)
        while (queue.isNotEmpty()) {
            val v = queue.removeFirst()
            if (v == end) {
                return depths[end]
            }
            get(v)?.forEach { to ->
                if (!visited.contains(to)) {
                    visited.add(to)
                    queue.addLast(to)
                    depths[to] = depths[v]!! + 1
                }
            }
        }
        return null
    }

    fun findFewestStepsNumber(input: List<String>, isStartingPoint: (elevation: Char) -> Boolean): Int {
        val startingPoints = mutableListOf<Point>()
        lateinit var end: Point
        val graph = mutableMapOf<Point, MutableList<Point>>()
        input.forEachIndexed { i, row ->
            row.forEachIndexed { j, elevation ->
                val point = Point(i, j)
                if (isStartingPoint(elevation)) {
                    startingPoints.add(point)
                } else if (elevation == 'E') {
                    end = point
                }
                for ((deltaY, deltaX) in deltas) {
                    val k = i + deltaY
                    val l = j + deltaX
                    if (k in 0..input.lastIndex && l in 0..row.lastIndex) {
                        val pointElevation = mapElevation(elevation)
                        val neighbourElevation = mapElevation(input[k][l])
                        if (pointElevation.code - neighbourElevation.code >= -1) {
                            graph.addEdge(point, Point(k, l))
                        }
                    }
                }
            }
        }
        return startingPoints.mapNotNull { graph.bfs(it, end) }.min()
    }

    fun part1(input: List<String>): Int = findFewestStepsNumber(input) { elevation ->
        elevation == 'S'
    }

    fun part2(input: List<String>): Int = findFewestStepsNumber(input) { elevation ->
        elevation == 'S' || elevation == 'a'
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
