package dynamic

/**
 * Computing the editing distance between 2 strings:
 *
 * insert char +1
 * delete char +1
 * replace char +1
 */

sealed interface Edit {
  data class Insert(val pos :Int, val ch :Char) :Edit {
    override fun exec(input :String) = input.substring(0..<pos)+ch+input.substring(pos..<input.length)
  }
  data class Delete(val pos :Int) :Edit {
    override fun exec(input :String) = input.substring(0..<pos)+input.substring(pos+1..<input.length)
  }
  data class Replace(val pos :Int, val newC :Char) :Edit {
    override fun exec(input :String) = input.substring(0..<pos)+newC+input.substring(pos+1..<input.length)
  }
  object Nop :Edit {
    override fun exec(input :String) = input
  }

  fun exec(input :String) :String
}

fun levenshtein(input1 :String, input2 :String) :List<Edit> {
  val grid = Array(input1.length+1) { Array<Pair<Int, Edit>>(input2.length+1) {Pair(0, Edit.Nop)} }
  input2.mapIndexed { i, ch ->
    grid[0][i+1] = Pair(i+1, Edit.Insert(i, ch))
  } 
  input1.indices.map { i ->
      grid[i+1][0] = Pair(i+1, Edit.Delete(0))
  }
  for (r in 1..<grid.size)
    for (c in 1..<grid[0].size) {
      val up = grid[r-1][c]
      val left = grid[r][c-1]
      val ed = grid[r-1][c-1]
      val edDist = if (input1[r-1] != input2[c-1]) 1  else 0
      grid[r][c] = when {
        ed.first+edDist <= up.first+1 && ed.first+edDist <= left.first+1 ->
          Pair(ed.first+edDist, if (edDist==1) Edit.Replace(c-1, input2[c-1])  else Edit.Nop)
        up.first <= left.first -> Pair(up.first+1, Edit.Delete(r-1))
        else -> Pair(left.first+1, Edit.Insert(c-1, input2[c-1]))
      }
  }
  val result = mutableListOf<Edit>()
  var cell = grid.last().last();  var r=grid.size-1;  var c = grid[0].size-1
  while (r>0 || c>0) {
    result.add(0, cell.second)
    when (cell.second) {
      is Edit.Replace -> {r--; c--}
      is Edit.Nop -> {r--; c--; result.removeAt(0)}
      is Edit.Insert -> c--
      is Edit.Delete -> r--
    }
    cell = grid[r][c]
  }
  return result
}

