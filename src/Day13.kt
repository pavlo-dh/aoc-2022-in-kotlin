sealed class E : Comparable<E>

class L(vararg elements: E) : E() {
    val elements = mutableListOf(*elements)

    override fun compareTo(other: E): Int = when (other) {
        is L -> {
            elements.forEachIndexed { index, element ->
                val otherElement = other.elements.getOrNull(index) ?: return 1
                val comparisonResult = element.compareTo(otherElement)
                if (comparisonResult != 0) return comparisonResult
            }
            if (other.elements.size > elements.size) -1 else 0
        }

        is I -> this.compareTo(L(other))
    }

    override fun toString() = elements.joinToString(separator = ",", prefix = "[", postfix = "]")
}

class I(val value: Int) : E() {
    override fun compareTo(other: E) = when (other) {
        is I -> value.compareTo(other.value)
        is L -> L(this).compareTo(other)
    }

    override fun toString() = value.toString()
}

fun main() {
    fun findSubListEnd(elementsString: String, subListStart: Int): Int {
        var bracketsCounter = 1
        for (i in subListStart + 1..elementsString.lastIndex) {
            if (elementsString[i] == ']') {
                bracketsCounter--
                if (bracketsCounter == 0) {
                    return i
                }
            } else if (elementsString[i] == '[') {
                bracketsCounter++
            }
        }
        error("Sublist end not found.")
    }

    fun parseElementsString(elementsString: String): L {
        var index = 0
        val l = L()
        while (index < elementsString.length) {
            if (elementsString[index] == '[') {
                val subListEnd = findSubListEnd(elementsString, index)
                val subListString = elementsString.substring(index + 1, subListEnd)
                val subList = parseElementsString(subListString)
                l.elements.add(subList)
                index = subListEnd + 2
            } else {
                var delimiterIndex = elementsString.indexOf(',', startIndex = index + 1)
                if (delimiterIndex == -1) delimiterIndex = elementsString.length
                val integerString = elementsString.substring(index, delimiterIndex)
                val integer = I(integerString.toInt())
                l.elements.add(integer)
                index = delimiterIndex + 1
            }
        }
        return l
    }

    fun parseInput(input: List<String>): List<L> = input.chunked(3).flatMap { (firstString, secondString) ->
        val firstPacket = parseElementsString(firstString.substring(1, firstString.lastIndex))
        val secondPacket = parseElementsString(secondString.substring(1, secondString.lastIndex))
        listOf(firstPacket, secondPacket)
    }

    fun part1(input: List<String>): Int {
        val packets = parseInput(input)
        val comparisons = packets.chunked(2).map { (firstPacket, secondPacket) ->
            firstPacket.compareTo(secondPacket)
        }
        return comparisons.withIndex().sumOf { (index, value) ->
            if (value == -1) index + 1 else 0
        }
    }

    fun part2(input: List<String>): Int {
        val firstDividerPacketString = "[[2]]"
        val secondDividerPacketString = "[[6]]"
        val packets = parseInput(input + listOf("\n", firstDividerPacketString, secondDividerPacketString))
        val packetsAsStringsSorted = packets.sorted().map(L::toString)
        val firstDividerPacketIndex = packetsAsStringsSorted.indexOf(firstDividerPacketString) + 1
        val secondDividerPacketIndex = packetsAsStringsSorted.indexOf(secondDividerPacketString) + 1
        return firstDividerPacketIndex * secondDividerPacketIndex
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
