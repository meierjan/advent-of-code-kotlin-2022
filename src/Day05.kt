import Interval.Companion.parseLine
import java.util.*
import kotlin.io.path.createTempDirectory

data class CrateStack(
    val stacks: List<Stack<Char>>
) {
    companion object {
        fun parse(text: List<String>): CrateStack {
            val stackNumber = text.first().length / 4 + 1
            val stacks = MutableList(stackNumber) { Stack<Char>() }

            val divider = text.indexOfFirst { it.isEmpty() }
            val crateDefinition = text.subList(0, divider - 1)


            crateDefinition.reversed().forEach {
                it.chunked(4).forEachIndexed { index, crate ->
                    if (crate.isNotBlank()) {
                        val letter = crate.trim().trimStart('[').trimEnd(']').first()
                        stacks[index].push(letter)
                    }
                }
            }

            return CrateStack(stacks)
        }
    }
}

data class MovementDefinition(
    val amount: Int,
    val from: Int,
    val to: Int
) {
    companion object {
        fun parse(text: List<String>): List<MovementDefinition> {
            val divider = text.indexOfFirst { it.isEmpty() }
            val movementDefinition = text.subList(divider + 1, text.size)

            return movementDefinition.map {
                val splitDefinition = it.split(" ")
                MovementDefinition(
                    amount = splitDefinition[1].toInt(),
                    from = splitDefinition[3].toInt() - 1,
                    to = splitDefinition[5].toInt() - 1
                )
            }
        }
    }
}


fun day5_part1(input: List<String>): String {
    val crateStack = CrateStack.parse(input)
    val movements = MovementDefinition.parse(input)

    movements.forEach { operation ->

        val amount = operation.amount

        repeat(amount) {
            crateStack.stacks[operation.to].push(crateStack.stacks[operation.from].pop())
        }
    }

    return crateStack.stacks.map { it.pop() }.joinToString("")
}

fun day5_part2(input: List<String>): String {
    val crateStack = CrateStack.parse(input)
    val movements = MovementDefinition.parse(input)

    movements.forEach { operation ->

        val amount = operation.amount

        val crates = mutableListOf<Char>()
        repeat(amount) {
            crates.add(crateStack.stacks[operation.from].pop())
        }

        for (crateToMove in crates.reversed()) {
            crateStack.stacks[operation.to].push(crateToMove)
        }
    }

    return crateStack.stacks.map { it.pop() }.joinToString("")
}

fun main() {
//    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_1_test")



    println(day5_part1(testInput))
    check(day5_part1(testInput) == "CMZ")

    val input = readInput("Day05_1")
    println(day5_part1(input))

    println(day5_part2(testInput))
    check(day5_part2(testInput) == "MCD")
    println(day5_part2(input))
}
