import kotlin.math.min

sealed class Command {
    data class CD(val path: String) : Command() {

        companion object {
            const val DISCRIMINATOR = "cd"

            fun parse(input: List<String>): CD {
                val (_, _, path) = input.first().split(" ")
                return CD(path)
            }
        }
    }

    data class LS(val files: List<Reference>) : Command() {
        companion object {
            const val DISCRIMINATOR = "ls"

            fun parse(input: List<String>): LS {
                return LS(input.drop(1).map {
                    Reference.parse(it)
                })

            }
        }
    }

    companion object {

        fun parse(input: List<String>): List<Command> {
            val inputGroupedByCmd =
                input.joinToString("#")
                    .split("$")
                    .map {
                        "$$it".split("#")
                            .filter {
                                it.isNotEmpty()
                            }
                    }
                    .drop(1)

            return inputGroupedByCmd.map {
                val cmdParts = it.first().split(" ")
                when (cmdParts[1]) {
                    LS.DISCRIMINATOR -> LS.parse(it)
                    CD.DISCRIMINATOR -> CD.parse(it)
                    else -> throw IllegalArgumentException("${cmdParts[1]} is an unknown discriminator")
                }
            }
        }
    }
}

sealed class Reference {
    abstract val name: String
    abstract val size: Long
    abstract var parent: Folder?

    data class File(
        override val size: Long,
        override val name: String,
        override var parent: Folder? = null,
    ) : Reference() {
        companion object {

            fun parse(string: String): File {
                val (size, name) = string.split(" ")
                return File(size.toLong(), name)
            }
        }

        override fun toString(): String = name
    }

    data class Folder(
        override val name: String,
        var references: List<Reference>? = null,
        override var parent: Folder? = null,
    ) : Reference() {
        override val size: Long
            get() = references?.sumOf { it.size } ?: 0


        override fun toString(): String = name


        companion object {

            fun parse(string: String): Folder {
                val (_, name) = string.split(" ")
                return Folder(name)
            }
        }
    }

    companion object {
        fun parse(string: String): Reference =
            when {
                string.startsWith("dir") -> Folder.parse(string)
                else -> File.parse(string)
            }
    }
}

object FileTreeBuilder {

    fun buildTree(input: List<String>): Reference.Folder {
        val cmds = Command.parse(input)

        val root = cmds.first() as Command.CD
        val rootReference = Reference.Folder(name = root.path)

        var currentPointer: Reference.Folder = rootReference

        for (cmd in cmds.drop(1)) {
            when (cmd) {
                is Command.CD -> {
                    currentPointer = when (cmd.path) {
                        // move down
                        ".." -> currentPointer.parent!!
                        // move up
                        else -> {
                            currentPointer.references!!
                                .filterIsInstance<Reference.Folder>()
                                .first { it.name == cmd.path }
                        }
                    }
                }
                is Command.LS -> {
                    currentPointer.references = cmd.files
                    cmd.files.map { it.parent = currentPointer }

                }
            }
        }

        return rootReference
    }
}

private fun filterBySize(reference: Reference, sizeLimit: Long): Long =
    when (reference) {
        is Reference.File -> 0
        is Reference.Folder -> if (reference.size < sizeLimit) {
            reference.size
        } else {
            0
        } + (reference.references?.sumOf { filterBySize(it, sizeLimit) } ?: 0)
    }


fun day7_part1(input: List<String>): Long {
    val tree = FileTreeBuilder.buildTree(input)
    return filterBySize(tree, 100000)
}

private fun getMinSizeAbove(reference: Reference, sizeBarrier: Long, currentMin: Long = Long.MAX_VALUE): Long {
    val currentSize = reference.size
    return when (reference) {
        is Reference.File -> Long.MAX_VALUE
        is Reference.Folder -> if (currentSize in sizeBarrier until currentMin) {
            min(currentSize, reference.references?.minOf { getMinSizeAbove(it, sizeBarrier, currentMin) } ?: Long.MAX_VALUE)
        } else {
            reference.references?.minOf { getMinSizeAbove(it, sizeBarrier, currentMin) } ?: Long.MAX_VALUE
        }
    }
}

fun day7_part2(input: List<String>): Long {
    val tree = FileTreeBuilder.buildTree(input)
    val diskSpace = 70000000
    val spaceNeeded = 30000000
    val usedSpace = tree.size

    val spaceLeft = diskSpace - usedSpace
    val spaceToFree = spaceNeeded - spaceLeft



    return getMinSizeAbove(tree, spaceToFree)
}

fun main() {

    val testInput = readInput("Day07_1_test")
    val realInput = readInput("Day07_1")

    check(day7_part1(testInput) == 95437L)
    println(day7_part1(realInput))

    check(day7_part2(testInput) == 24933642L)
    println(day7_part2(realInput))

}
