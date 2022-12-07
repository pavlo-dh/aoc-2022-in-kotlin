fun main() {
    class Directory(val name: String, val parent: Directory?) {
        var size = 0
    }

    fun processFiles(currentCommandIndex: Int, input: List<String>, currentDirectory: Directory): Int {
        val nextCommandIndex =
            (currentCommandIndex + 1 until input.size).firstOrNull { index -> input[index].startsWith('$') }
                ?: input.size
        for (i in currentCommandIndex + 1 until nextCommandIndex) {
            val line = input[i]
            if (!line.startsWith('d')) {
                val fileSize = line.split(' ').first()
                currentDirectory.size += fileSize.toInt()
            }
        }
        return nextCommandIndex
    }

    fun part1(input: List<String>): Int {
        var commandIndex = 1

        var currentDirectory = Directory(name = "/", parent = null)
        var sizeSum = 0

        do {
            val command = input[commandIndex]
            when (command[2]) {
                'c' -> {
                    currentDirectory = when (command[5]) {
                        '.' -> {
                            if (currentDirectory.size <= 100000) sizeSum += currentDirectory.size
                            currentDirectory.parent!!.also { parentDirectory ->
                                parentDirectory.size += currentDirectory.size
                            }
                        }

                        else -> Directory(name = command.substring(5), parent = currentDirectory)
                    }
                    commandIndex++
                }

                'l' -> {
                    commandIndex = processFiles(commandIndex, input, currentDirectory)
                }
            }
        } while (commandIndex != input.size)

        while (currentDirectory.name != "/") {
            if (currentDirectory.size <= 100000) sizeSum += currentDirectory.size
            currentDirectory = currentDirectory.parent!!.also { parentDirectory ->
                parentDirectory.size += currentDirectory.size
            }
        }

        return sizeSum
    }

    fun part2(input: List<String>): Int {
        var commandIndex = 1

        var currentDirectory = Directory(name = "/", parent = null)
        val directories = mutableListOf(currentDirectory)

        do {
            val command = input[commandIndex]
            when (command[2]) {
                'c' -> {
                    currentDirectory = when (command[5]) {
                        '.' -> {
                            currentDirectory.parent!!.also { parentDirectory ->
                                parentDirectory.size += currentDirectory.size
                            }
                        }

                        else -> Directory(name = command.substring(5), parent = currentDirectory).also { directory ->
                            directories.add(directory)
                        }
                    }
                    commandIndex++
                }

                'l' -> {
                    commandIndex = processFiles(commandIndex, input, currentDirectory)
                }
            }
        } while (commandIndex != input.size)

        while (currentDirectory.name != "/") {
            currentDirectory = currentDirectory.parent!!.also { parentDirectory ->
                parentDirectory.size += currentDirectory.size
            }
        }

        val spaceTotal = 70000000
        val spaceNeeded = 30000000
        val currentSize = directories.first().size
        directories.sortBy { it.size }
        return directories.first { directory -> spaceTotal - (currentSize - directory.size) >= spaceNeeded }.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
