import kotlin.math.abs

fun main() {
    data class Point(val x: Int, val y: Int) {
        fun manhattanDistance(other: Point) = abs(x - other.x) + abs(y - other.y)
    }

    fun parsePoint(s: String): Point {
        val x = s.substring(s.indexOf('=') + 1, s.indexOf(',')).toInt()
        val y = s.substring(s.lastIndexOf('=') + 1).toInt()
        return Point(x, y)
    }

    fun parseInput(input: List<String>) = input.map { line ->
        val (sensor, beacon) = line.split(": ").map(::parsePoint)
        sensor to beacon
    }

    fun part1(input: List<String>, y: Int): Int {
        val sensors = mutableSetOf<Point>()
        val beacons = mutableSetOf<Point>()
        val reachedPositions = mutableSetOf<Point>()
        parseInput(input).forEach { (sensor, beacon) ->
            sensors.add(sensor)
            beacons.add(beacon)
            val distance = sensor.manhattanDistance(beacon)
            val minY = sensor.y - distance
            val maxY = sensor.y + distance
            if (y in minY..maxY) {
                for (x in sensor.x - distance..sensor.x + distance) {
                    val position = Point(x, y)
                    if (sensor.manhattanDistance(position) <= distance) {
                        reachedPositions.add(position)
                    }
                }
            }
        }
        reachedPositions.removeAll(sensors + beacons)
        return reachedPositions.size
    }

    fun part2(input: List<String>, maxCoordinate: Int): Long {
        val sensorDistancePairs = parseInput(input).map { (sensor, beacon) ->
            val distance = sensor.manhattanDistance(beacon)
            sensor to distance
        }

        fun findDistressBeacon(sensor: Point, distance: Int): Point? {
            val increasedDistance = distance + 1
            val startX = sensor.x
            val startY = sensor.y + increasedDistance
            var x = startX
            var y = startY
            var xIncrement = 1
            var yIncrement = -1
            do {
                if (x in 0..maxCoordinate && y in 0..maxCoordinate) {
                    val position = Point(x, y)
                    var otherSensorReaches = false
                    for ((otherSensor, otherDistance) in sensorDistancePairs) {
                        if (otherSensor.manhattanDistance(position) <= otherDistance) {
                            otherSensorReaches = true
                            break
                        }
                    }
                    if (!otherSensorReaches) {
                        return position
                    }
                }
                x += xIncrement
                y += yIncrement
                if (abs(x - sensor.x) == increasedDistance) {
                    xIncrement *= -1
                }
                if (abs(y - sensor.y) == increasedDistance) {
                    yIncrement *= -1
                }
            } while (x != startX && y != startY)
            return null
        }

        for ((sensor, distance) in sensorDistancePairs) {
            findDistressBeacon(sensor, distance)?.let { distressBeacon ->
                return distressBeacon.x * 4000000L + distressBeacon.y
            }
        }
        error("Distress beacon not found!")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("Day15")
    println(part1(input, 2000000))
    println(part2(input, 4000000))
}
