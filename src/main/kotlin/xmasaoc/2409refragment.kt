package xmasaoc

import java.io.BufferedReader
import kotlin.math.min

fun min(a :Byte, b :Byte) = when {
  a <= b -> a
  else -> b
}

typealias Entry = Pair<Short, Byte>
const val empty = (-1).toShort()

fun decode(input :String) :List<Entry> {
  val result = mutableListOf<Entry>()
  var id = 0.toShort()
  val iit = input.iterator()
  while (iit.hasNext()) {
    val c1 = iit.next()
    val size = (c1-'0').toByte()
    result.add(Entry(id++, size))
    if (!iit.hasNext()) break
    val c2 = iit.next()
    val s2 = (c2-'0').toByte()
    if (s2>0) result.add(Entry(empty, s2))
  }
  return result
}

fun List<Entry>.findFree(from :Int = -1) :Int {
  for (result in (from+1)..<size)
    if (get(result).first==empty) return result
  return size
}

fun MutableList<Entry>.move(nextFree :Int, freeSize :Byte, id :Short, contentSize :Byte) :Boolean {
  if (nextFree < size-1) {
    var result = false
    val move = min(freeSize, contentSize)
    if (move<freeSize) {
      add(nextFree, Entry(id, move))
      result = true
      this[nextFree+1] = Entry(empty, (freeSize-move).toByte())
    } else
      this[nextFree] = Entry(id, move)
    if (move < contentSize) {
      add(Entry(id, (contentSize-move).toByte()))
      result = true
    }
    return result
  } else
    this[nextFree] = Entry(id, contentSize)
  return false
}

fun condense(src :List<Entry>) :List<Entry> {
  val result = src.toMutableList()
  var nextFree = result.findFree(-1)
  while (nextFree<result.size) {
    val freeSize = result[nextFree].second
    val (id, contentSize) = result.removeLast()
    if (id==empty) continue
    result.move(nextFree, freeSize, id, contentSize)
    nextFree = result.findFree(nextFree)
  }
  return result
} 

fun List<Entry>.findFreeBlock(size :Byte, limit :Int = Int.MAX_VALUE) :Int {
  for (result in 0..<min(this.size, limit)) {
    val candidate = get(result)
    if (candidate.first==empty && size<=candidate.second)
      return result
  }
  return limit
}

fun MutableList<Entry>.merge(index :Int, debug :Boolean =false) :Boolean {
  if (index !in 0..<size) return false
  var (type, bsize) = get(index)
  var result = false
  if (debug) print("attempting merge ${index+1} and $index...")
  if (index+1<size && get(index+1).first==type) {
    val addlSize = removeAt(index+1).second
    bsize = (bsize+addlSize).toByte()
    set(index, Entry(type, bsize))
    if (debug) println("success: $bsize")
    result = true
  } else if (debug) println("skip.")
  if (debug) print("attempting merge ${index-1} with $index...")
  if (index>0 && get(index-1).first==type) {
    val addlSize = get(index-1).second
    removeAt(index)
    bsize = (bsize+addlSize).toByte()
    set(index-1, Entry(type, bsize))
    if (debug) println("success: $bsize")
    return true
  } else if (debug) println("was ${get(index-1).first} and ${type}, skipped.")
  return result
}

fun eagerCondense(src :List<Entry>) :List<Entry> {
  val result = src.toMutableList()
  var nextBlock = result.size-1
  while (nextBlock >= 0) {
    while (nextBlock>=0 && result[nextBlock].first==empty) --nextBlock
    if (nextBlock<0) break
    val (id, contentSize) = result[nextBlock]
    val firstFree = result.findFreeBlock(contentSize, nextBlock)
    if (firstFree<nextBlock) {
      result[nextBlock] = Entry(empty, contentSize)
      val freeSize = result[firstFree].second
      if (result.move(firstFree, freeSize, id, contentSize)) ++nextBlock
      result.merge(nextBlock)
    }
    --nextBlock
  }
  return result
}

fun checksum(bam :List<Entry>) :Long {
  var result = 0L
  var pos = 0
  for (entry in bam) {
    if (entry.first!=empty) {
      for (n in 1..entry.second) {
        result += pos*entry.first
        ++pos
      }
    } else
      pos += entry.second.toInt()
  }
  return result
}

fun main() {
  // val input = "2333133121414131402"
  val input = getInput(null, "2409bam.txt").toReader().use(BufferedReader::readText)
  val bam = decode(input)
  val newBam = condense(bam)
  val result = checksum(newBam)
  println("The new BAM is $newBam and has a checksum of $result.")
  println("The old BAM was $bam.")
  val bam2 = eagerCondense(bam)
  val res2 = checksum(bam2)
  println("The eagerly condensed BAM is $bam2 and has a checksum of $res2.")
}

