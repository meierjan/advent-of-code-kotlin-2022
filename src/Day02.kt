import java.lang.IllegalArgumentException

private enum class Result(
    val points: Int
) {
    WIN(6), DRAW(3), LOSE(0);

    companion object {
        fun parse(char: Char) = when (char) {
            'X' -> LOSE
            'Y' -> DRAW
            'Z' -> WIN
            else -> throw IllegalArgumentException("Unknown RESULT $char")
        }

    }
}

private enum class Weapon(
    val points: Int
) {
    ROCK(1), PAPER(2), SCISSORS(3);

    companion object {
        fun parse(char: Char): Weapon =
            when (char) {
                'A', 'X' -> ROCK
                'B', 'Y' -> PAPER
                'C', 'Z' -> SCISSORS
                else -> throw IllegalArgumentException("Unknown WEAPON $char")
            }
    }

    fun fight(other: Weapon) =
        when {
            this.weakness == other -> Result.LOSE
            this.strength == other -> Result.WIN
            else -> Result.DRAW
        }

    val weakness: Weapon
        get() = when (this) {
            ROCK -> PAPER
            PAPER -> SCISSORS
            SCISSORS -> ROCK
        }

    val strength: Weapon
        get() = when (this) {
            ROCK -> SCISSORS
            PAPER -> ROCK
            SCISSORS -> PAPER
        }


}


fun day2_part1(input: List<String>): Int =
    input.map {
        val (opponentWeapon, yourWeapon) = it.split(" ")
            .map { Weapon.parse(it.first()) }

        val matchResult = yourWeapon.fight(opponentWeapon)
        matchResult.points + yourWeapon.points
    }.sum()

fun day2_part2(input: List<String>): Int =
    input.map {
        val (weaponChar, resultChar) = it.split(" ")
        val opponentWeapon = Weapon.parse(weaponChar.first())
        val matchResult = Result.parse(resultChar.first())

        val yourWeapon = when (matchResult) {
            Result.DRAW -> opponentWeapon
            Result.WIN -> opponentWeapon.weakness
            Result.LOSE -> opponentWeapon.strength
        }

        matchResult.points + yourWeapon.points
    }.sum()


fun main() {
    val testInput = readInput("Day02_1_test")
    check(day2_part1(testInput) == 15)

    val input = readInput("Day02_1")
    println(day2_part1(input))

    check(day2_part2(testInput) == 12)
    println(day2_part2(input))


}