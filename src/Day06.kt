import java.util.*

private class BufferScanner(
    val bufferSize: Int = 4
) {
   private val queue: ArrayDeque<Char> = ArrayDeque(bufferSize)

   fun read(char: Char) {
        queue.addFirst(char)
       if(queue.size > bufferSize) {
           queue.removeLast()
       }
   }

    fun scanUniqueness() : Boolean =
        queue.toSet().size == bufferSize

}

private fun BufferScanner.scanTextForUniqueSequence(input: String) : Int {
    for((index, char) in input.toList().withIndex()) {
        this.read(char)
        if(this.scanUniqueness()) {
            return index + 1
        }
    }
    return -1;
}

fun day6_part1(input: String): Int = BufferScanner(4).scanTextForUniqueSequence(input)


fun day6_part2(input: String): Int = BufferScanner(14).scanTextForUniqueSequence(input)

fun main() {

    val input = readInput("Day06_1").first()
    // task 1
    check(day6_part1("bvwbjplbgvbhsrlpgdmjqwftvncz") == 5)
    check(day6_part1("nppdvjthqldpwncqszvftbrmjlhg") == 6)
    check(day6_part1("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 10)
    check(day6_part1("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 11)
    println(day6_part1(input))

    // task 2
    check(day6_part2("mjqjpqmgbljsphdztnvjfqwrcgsmlb") == 19)
    check(day6_part2("bvwbjplbgvbhsrlpgdmjqwftvncz") == 23)
    check(day6_part2("nppdvjthqldpwncqszvftbrmjlhg") == 23)
    check(day6_part2("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 29)
    check(day6_part2("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 26)
    println(day6_part2(input))

}
