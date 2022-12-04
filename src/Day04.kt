import Interval.Companion.parseLine

data class Interval(
    val from: Int, val to: Int,
) {
    fun fullyContains(other: Interval) : Boolean {
        return this.from <= other.from && this.to >= other.to
    }

    fun overlap(other: Interval) : Boolean {

        return if(this.from <= other.from) {
            this.to >= other.from
        } else {
            other.to >= this.from
        }
    }

    companion object {
        fun parse(text: String): Interval {
            val sides = text.split("-")
            return Interval(
                sides[0].toInt(),
                sides[1].toInt()
            )
        }

        fun parseLine(line: String) : Pair<Interval, Interval> {
            val splitLine = line.split(",")
            return Pair(parse(splitLine[0]), parse(splitLine[1]))
        }
    }
}

fun day4_part1(input: List<String>): Int =
    input.map { parseLine(it) }
        .map { it.first.fullyContains(it.second) || it.second.fullyContains(it.first)}
        .count { it }


fun day4_part2(input: List<String>): Int =
    input.map { parseLine(it) }
        .map { it.first.overlap(it.second)}
        .count { it }

fun main() {
//    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_1_test")
    println(day4_part1(testInput))
    check(day4_part1(testInput) == 2)

    val input = readInput("Day04_1")
    println(day4_part1(input))

    println(day4_part2(testInput))
    check(day4_part2(testInput) == 4)
    println(day4_part2(input))
}
