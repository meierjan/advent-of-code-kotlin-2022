import kotlinx.serialization.*
import kotlinx.serialization.json.*

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

sealed class NestedIntList {
    data class List(
        val list: kotlin.collections.List<NestedIntList>
    ) : NestedIntList()

    data class Int(
        val int: kotlin.Int
    ) : NestedIntList()
}

fun NestedIntList.Int.wrap(): NestedIntList.List =
    NestedIntList.List(listOf(this))

fun List<String>.toNestedIntList(): List<Pair<NestedIntList.List, NestedIntList.List>> {
    return this.chunked(3).map {
        val first = Json.decodeFromString<JsonArray>(it[0]).toNestedIntList() as NestedIntList.List
        val second = Json.decodeFromString<JsonArray>(it[1]).toNestedIntList() as NestedIntList.List
        Pair(first, second)
    }
}

fun compare(left: NestedIntList, right: NestedIntList): Int =
    if (left is NestedIntList.Int && right is NestedIntList.Int) {
        left.int.compareTo(right.int)
    } else if (left is NestedIntList.List && right is NestedIntList.List) {
        left.list.zip(right.list).map { (left, right) -> compare(left, right) }.find { it != 0 }
            ?: left.list.size.compareTo(right.list.size)
    } else if (left is NestedIntList.Int && right is NestedIntList.List) {
        compare(left.wrap(), right)
    } else if (left is NestedIntList.List && right is NestedIntList.Int) {
        compare(left, right.wrap())
    } else {
        throw IllegalArgumentException();
    }


fun JsonElement.toNestedIntList(): NestedIntList =
    when (this) {
        is JsonArray -> NestedIntList.List(this.map { it.toNestedIntList() })
        is JsonPrimitive -> NestedIntList.Int(this.toString().toInt())
        else -> throw IllegalArgumentException("not supported type")
    }


fun day13_part1(input: List<String>): Int =
    input.toNestedIntList()
        .map { (left, right) -> compare(left, right) }
        .mapIndexed { index, isRightOrder -> Pair(index, isRightOrder) }
        .filter { it.second !=  1}
        .sumOf { it.first + 1 }


fun day13_part2(input: List<String>): Int {
    val divider = listOf("[[2]]\n","[[6]]\n","").toNestedIntList().first()

    val orderedList = input.toNestedIntList()
        .plus(divider)
        .flatMap { listOf(it.first, it.second) }
        .sortedWith {a,b -> compare(a,b) }

    return orderedList.indexOf(divider.first).inc() *  orderedList.indexOf(divider.second).inc()
}


fun main() {
    val testInput = readInput("Day13_1_test")
    val realInput = readInput("Day13_1")



    println(day13_part1(testInput))
    println(day13_part1(realInput))

    println(day13_part2(testInput))
    println(day13_part2(realInput))

}
