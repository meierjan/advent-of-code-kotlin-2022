import java.lang.IllegalArgumentException

data class RucksackContent(
    val first: String, val second: String
) {
    companion object {
        fun parse(text: String): RucksackContent {
            val middle = text.length / 2
            return RucksackContent(
                first = text.substring(0, middle),
                second = text.substring(middle, text.length)
            )
        }
    }
}

val Char.priority
    get() : Int =
        if (this in 'a'..'z') {
            this - 'a' + 1
        } else if (this in 'A'..'z') {
            this - 'A' + 27
        } else {
            throw IllegalArgumentException("Must be within a-z or A-Z")
        }

fun String.countChars(): Map<Char, Int> {
    val result = LinkedHashMap<Char, Int>()
    for (entry in this) {
        val newVal = result.getOrDefault(entry, 0).inc()
        result[entry] = newVal
    }
    return result
}


fun day3_part1(input: List<String>): Int =
    input.map { RucksackContent.parse(it) }
        .map { rucksack ->
            val left = rucksack.first.countChars().mapValues { 1 }
            val right = rucksack.second.countChars().mapValues { 1 }

            left.filterKeys { right.containsKey(it) }
        }
        .map {
            it.map { it.key.priority }.sum()
        }.sum()

fun day3_part2(input: List<String>): Int =
    input.chunked(3).map {
        val one = it[0].countChars()
        val two = it[1].countChars()
        val three = it[2].countChars()

        one.filterKeys { key -> two.containsKey(key) && three.containsKey(key) }
    }.sumOf { it.keys.sumOf { it.priority } }


fun main() {
//    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_1_test")
    println(day3_part1(testInput))
    check(day3_part1(testInput) == 157)

    val input = readInput("Day03_1")
    println(day3_part1(input))

    check(day3_part2(testInput) == 70)
    println(day3_part2(input))
}
