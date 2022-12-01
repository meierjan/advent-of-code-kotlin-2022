private fun createCaloriesSumArray(input: List<String>) : List<Int> {
    val calorieGroups = mutableListOf(mutableListOf<Int>())
    for(item in input) {
        if (item.isEmpty()) {
            calorieGroups.add(mutableListOf())
        } else {
            calorieGroups.last().add(item.toInt())
        }
    }
    return calorieGroups.map { calGroup ->
        calGroup.sumOf { it }
    }
}

fun part1(input: List<String>): Int =
     createCaloriesSumArray(input)
         .max()

fun part2(input: List<String>): Int =
    createCaloriesSumArray(input)
        .takeLast(3)
        .sum()

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_1_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01_1")
    println(part1(input))
    println(part2(input))
}
