package xmasaoc

import java.util.stream.Stream

enum class Direction(val symb :Char, val mask :Int) {
  Up('^', 1), Right('>', 2), Down('v', 4), Left('<', 8);

  fun turn() = when (this) {
      Up -> Right
      Right -> Down
      Down -> Left
      Left -> Up
  }

  companion object {
    fun of(symb :Char) = entries.firstOrNull { it.symb==symb }
  }
}

data class Pos(val r :Int, val c :Int, val d :Direction) {

  fun next() :Pos? = when (d) {
    Direction.Up -> if (r>0) Pos(r-1, c, d)  else null
    Direction.Right -> Pos(r, c+1, d)
    Direction.Down -> Pos(r+1, c, d)
    Direction.Left -> if (c>0) Pos(r, c-1, d)  else null
  }

  fun turn() = copy(d= d.turn())
}

typealias Grid = List<String>
typealias MGrid = MutableList<String>

val Grid.numRows
  get() = size
val Grid.numCols
  get() = first().length

fun print(g :Grid) = println(g.joinToString { "\n"+it })

fun parseGrid(input :Stream<String>) :Pair<MGrid, Pos> {
  var pos = Pos(0, 0, Direction.Right)
  var row = -1
  val grid = input.map { line ->
    ++row
    val m = "[>^v<]".toRegex().find(line)
    if (m!=null) {
      val c = m.range.start
      pos = Pos(row, c, Direction.of(line[c])!!)
      val result = line.toMutableList()
      result[c] = '.'
      result.joinToString("")
    } else
      line
  }.toList().toMutableList()
  return Pair(grid, pos)
}

fun Grid.loop(pos0 :Pos, detect :Boolean =true) :Pair<Long, Array<ByteArray>> {
  var count = 0L
  val limit = numRows.toLong() * numCols * Direction.entries.size
  val map = Array(numRows) { ByteArray(numCols) {0} }
  var pos = pos0
  outer@while (count < limit) {
    if (map[pos.r][pos.c]==0.toByte()) {
      ++count
    } else if (detect && (map[pos.r][pos.c].toInt() and pos.d.mask != 0))
      return Pair(-count-1L, map)
    map[pos.r][pos.c] = (map[pos.r][pos.c].toInt() or pos.d.mask).toByte()
    // System.out.print("$pos, ");  System.out.flush()
    for (n in 1..4) {
      val next = pos.next() ?: break@outer
      if (next.r>=numRows || next.c>=numCols) break@outer
      if (get(next.r)[next.c]=='.') {
        map[pos.r][pos.c] = (map[pos.r][pos.c].toInt() or next.d.mask).toByte()
        pos = next
        continue@outer
      }
      pos = pos.turn()
    }
    break
  }
  return Pair(count, map)
}

fun MGrid.constructLoops(pos :Pos, used :Array<ByteArray>) :ULong {
  var count = 0uL
  used.forEachIndexed { r :Int, row :ByteArray ->
    row.forEachIndexed { c :Int, s :Byte -> if (c!=0 && (r!=pos.r||c!=pos.c)) {
      // System.out.print("trying ($r, $c): ");  System.out.flush()
      val oldRow = get(r)
      val fRow = oldRow.toMutableList()
      fRow[c] = 'O'
      this[r] = fRow.joinToString("")
      val loop = loop(pos, true)
      if (loop.first < 0L) {
        ++count
        // print("obstruction ($r, $c): ");  println(loop.second.mapIndexed { r :Int, row -> row.mapIndexed { c :Int, s :Byte -> when {
        //   (s.toInt() and (Direction.Up.mask or Direction.Down.mask) != 0) ->
        //     if (s.toInt() and (Direction.Right.mask or Direction.Left.mask) != 0) '+'
        //     else '|'
        //   (s.toInt() and (Direction.Right.mask or Direction.Left.mask) != 0) -> '-'
        //   else -> get(r)[c]
        // } }.joinToString("") }.joinToString("\n", "\n"))
      }
      this[r] = oldRow
      // println()
    } }
  }
  return count
}

fun main() {
   // val input = listOf(
   //   "....#.....",
   //   ".........#",
   //   "..........",
   //   "..#.......",
   //   ".......#..",
   //   "..........",
   //   ".#..^.....",
   //   "........#.",
   //   "#.........",
   //   "......#..."
   // ).stream()
  val input = getInput(null, "2406map.txt").toReader().lines()
  val (grid, pos) = parseGrid(input)
  // print(grid)
  val result = grid.loop(pos)
  println("\nStepped on ${result.first} fields.")
  val res2 = grid.constructLoops(pos, result.second)
  println("There are $res2 ways to get the guard into a loop.")
}
