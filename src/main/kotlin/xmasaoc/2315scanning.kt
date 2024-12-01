package xmasaoc

import partitions.abs
import java.io.InputStream

private const val RESOURCE_NAME = "15beacons.txt"
private val debug = '-' in RESOURCE_NAME
private val maskRow :Int = 2_000_000
private val cutoff = 4_000_000

fun main(args: Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val sensorsAndBeacons = parseSensorsAndBeacons(input)
    if (debug)
        println("Sensors and beacons are at: " +sensorsAndBeacons.joinToString { p -> "${p.first} reading ${p.second}" })
    val minRow = computeMinRow(sensorsAndBeacons);  val maxRow = computeMaxRow(sensorsAndBeacons)
    val minCol = computeMinCol(sensorsAndBeacons);  val maxCol = computeMaxCol(sensorsAndBeacons)
    println("Expected size: $cutoff x ${maxCol-minCol+1} = ${cutoff.toLong()*(maxCol-minCol+1).toLong()/1024/1024} MB")
    val grid = // if (debug)
//        CroppedMatrix<Code>((0..cutoff).map { (minCol..maxCol).map { EMPTY } },
//            minRow= 0, maxRow= cutoff, colOffset= -minCol, zero= EMPTY)
//      else
        MaskedMatrix((minCol..maxCol).map { EMPTY },
            maskRow= maskRow, colOffset= -minCol, zero= EMPTY)
    for (sb in sensorsAndBeacons) {
        val sensor = sb.first;  val beacon = sb.second
        if (beacon.first!=null && beacon.second!=null) {
            grid.fillDiamond(sensor, d1(sensor, Pair(beacon.first!!, beacon.second!!)), COVERED)
            grid[beacon.first!!, beacon.second!!] = BEACON
        } else if (beacon.first!=null) {
            grid.fillDiamond(sensor, abs(sensor.first-beacon.first!!), COVERED)
        }else if (beacon.second != null) {
            grid.fillDiamond(sensor, abs(sensor.second - beacon.second!!), COVERED)
        }
        grid[sensor.first, sensor.second] = SENSOR
    }
    val resultRow1 = grid.getRow(maskRow)
    if (debug) {
        println("What is covered:\n$grid")
        println("row $maskRow: " + resultRow1.joinToString(" "))
    }
    val numBeacons = sensorsAndBeacons.filter { p -> p.second.first==maskRow }.map { p -> p.second }.toSet().size
    val counts = resultRow1.groupBy { it }.map { entry -> Pair(entry.key, entry.value.size) }.toMap().toMutableMap()
    if ((counts[BEACON]?:0) < numBeacons) {
        val misses = numBeacons -counts[BEACON]!!
        println("Missed $misses beacons.")
        counts[BEACON] = numBeacons
        counts[COVERED] = counts[COVERED]!! -misses
    }
    println("$counts of ${resultRow1.size} cells.")
    // this does not work on a MaskedMatrix, neither does the CroppedMatrix fit into memory
    val candidates = grid.getRows()
        .mapIndexed { r :Int, row ->
            val joinedRow = row.joinToString(""){ it.toInt().toChar().toString() }
            Triple(r-grid.rowOffset, joinedRow.indexOf('#')-grid.colOffset, joinedRow.trim('.'))
        }.filter { it.third.count { it=='.' }==1 }
        .map { Pair(it.first, it.second+it.third.indexOf('.')) }
        .map { p -> Triple(p.first, p.second, p.first+ cutoff.toLong()*p.second) }
    println("Candidates: $candidates")
}

class CroppedMatrix<F>(values0 :List<List<F>>, val minRow :Int, val maxRow :Int, colOffset :Int, zero :F)
    :OffsetMatrix<F>(values0, -minRow, colOffset, zero) {
    override operator fun get(i :Int, j :Int) = if (i in minRow until maxRow) super.get(i, j)  else zero

    override fun getRow(i :Int) = if (i in minRow until maxRow) super.getRow(i)  else (1u..numCols).map { zero }

    override operator fun set(i :Int, j :Int, value :F) = if (i in minRow until maxRow) super.set(i, j, value)  else Unit

    override fun replace(r :Int, c :Int, part :List<F>) = if (r in minRow until maxRow) super.replace(r, c, part)  else Unit

    override fun toString() = getRows().mapIndexed { r, row ->
        row.joinToString(" ", prefix= "%+3d: [".format(minRow+r), postfix= "]")
    }.joinToString("\n")
}

private fun <F> MaskedMatrix<F>.fillDiamond(sensor :Pair<Int, Int>, d :UInt, color :F) {
    val h = abs(maskRow -sensor.first)
    println(h)
    if (h<=d) {
        val length = 2*(d-h).toInt() +1
        val part = (1..length).map { color }
        replace(maskRow, sensor.second -(d-h).toInt(), part)
    }
}

class MaskedMatrix<F>(values0 :List<F>, val maskRow :Int, colOffset :Int, zero :F)
        :OffsetMatrix<F>(listOf(values0), -maskRow, colOffset, zero) {
    override operator fun get(i :Int, j :Int) = if (i==maskRow) super.get(i, j)  else zero

    override fun getRow(i :Int) = if (i==maskRow) super.getRow(i)  else (1u..numCols).map { zero }

    override operator fun set(i :Int, j :Int, value :F) = if (i==maskRow) super.set(i, j, value)  else Unit

    override fun replace(r :Int, c :Int, part :List<F>) = if (r==maskRow) super.replace(r, c, part)  else Unit

    override fun toString() = getRows().map { row ->
        row.joinToString(" ", prefix= "%+3d: [".format(maskRow), postfix= "]")
    }.joinToString("\n")
}

private fun <F> Matrix<F>.fillDiamond(sensor :Pair<Int, Int>, d :UInt, color :F) {
    println(d)
    val part = mutableListOf(color)
    for (hl in 0..d.toInt()) {
        replace(sensor.first-d.toInt()+hl, sensor.second-hl, part)
        if (hl!=d.toInt())
            replace(sensor.first+d.toInt()-hl, sensor.second-hl, part)
        part.add(color);  part.add(color)
    }
}

open class OffsetMatrix<F>(values0 :List<List<F>>, val rowOffset :Int, val colOffset :Int, zero :F) :Matrix<F>(values0, zero) {

    override fun toString() = getRows().mapIndexed { r, row ->
        row.joinToString(" ", prefix= "%+3d: [".format(r-rowOffset), postfix= "]")
    }.joinToString("\n")

    override operator fun get(i :Int, j :Int) = super.get((i+rowOffset).toUInt(), (j+colOffset).toUInt())

    open fun getRow(i :Int) = super.getRow((i+rowOffset).toUInt())

    override operator fun set(i :Int, j :Int, value :F) = set((i+rowOffset).toUInt(), (j+colOffset).toUInt(), value)

    override fun replace(r :Int, c :Int, part :List<F>) = super.replace(r+rowOffset, c+colOffset, part)
}

private fun computeMaxRow(seonsorsAndBeacons :List<Pair<Pair<Int, Int>, Pair<Int?, Int?>>>) =
    seonsorsAndBeacons.maxOf { p ->
        val sensor = p.first;  val b0 = p.second
        if (b0.first!=null && b0.second!=null) {
            val d = d1(sensor, Pair(b0.first!!, b0.second!!))
            sensor.first +d.toInt()
        } else if (b0.first != null) {
            val d = abs(sensor.first -b0.first!!)
            sensor.first +d.toInt()
        } else
            sensor.first
    }

private fun computeMinRow(seonsorsAndBeacons :List<Pair<Pair<Int, Int>, Pair<Int?, Int?>>>) =
    seonsorsAndBeacons.minOf { p ->
        val sensor = p.first;  val b0 = p.second
        if (b0.first != null && b0.second != null) {
            val d = d1(sensor, Pair(b0.first!!, b0.second!!))
            sensor.first -d.toInt()
        } else if (b0.first != null) {
            val d = abs(sensor.first -b0.first!!)
            sensor.first -d.toInt()
        } else
            sensor.first
    }

private fun computeMaxCol(seonsorsAndBeacons :List<Pair<Pair<Int, Int>, Pair<Int?, Int?>>>) =
    seonsorsAndBeacons.maxOf { p ->
        val sensor = p.first;  val b0 = p.second
        if (b0.first!=null && b0.second!=null) {
            val d = d1(sensor, Pair(b0.first!!, b0.second!!))
            sensor.second +d.toInt()
        } else if (b0.second != null) {
            val d = abs(sensor.second -b0.second!!)
            sensor.second +d.toInt()
        } else
            sensor.second
    }

private fun computeMinCol(seonsorsAndBeacons :List<Pair<Pair<Int, Int>, Pair<Int?, Int?>>>) =
    seonsorsAndBeacons.minOf { p ->
        val sensor = p.first;  val b0 = p.second
        if (b0.first!=null && b0.second!=null) {
            val d = d1(sensor, Pair(b0.first!!, b0.second!!))
            sensor.second -d.toInt()
        } else if (b0.second != null) {
            val d = abs(sensor.second -b0.second!!)
            sensor.second -d.toInt()
        } else
            sensor.second
    }

fun d1(x :Pair<Int, Int>, y :Pair<Int, Int>) = abs(x.first -y.first) +abs(x.second -y.second)

private fun parseSensorsAndBeacons(inputStream :InputStream) :List<Pair<Pair<Int, Int>, Pair<Int?, Int?>>> {
    val result = mutableListOf<Pair<Pair<Int, Int>, Pair<Int?, Int?>>>()
    val input = inputStream.toReader()
    while (input.ready()) {
        val line = input.readLine().trim().split("\\s+".toRegex())
        // Sensor at x=20, y=1: closest beacon is at x=15, y=3
        if (line.size<10 || line[0]!="Sensor"||line[1]!="at"||line[4]!="closest"||line[5]!="beacon"||line[6]!="is"||line[7]!="at") {
            println("Error parsing line: $line")
            continue
        }
        val numSX = line[2].substring(2, line[2].length-1)
        val numSY = line[3].substring(2, line[3].length-1)
        val sensor = Pair(numSY.toIntOrNull(), numSX.toIntOrNull()) // row, column
        if (sensor.first==null||sensor.second==null) {
            println("Error parsing sensor position from: ${line.subList(1,3)}.")
            continue
        }
        val numBX = line[8].substring(2, line[8].length-1)
        val numBY = line[9].substring(2)
        val beacon = Pair(numBY.toIntOrNull(), numBX.toIntOrNull()) // row, column
        if (beacon.first==null||beacon.second==null) {
            println("Warning: Could not parse beacon position: ${line.subList(8, 10)}")
        }
        result.add(Pair(Pair(sensor.first!!, sensor.second!!), beacon))
    }
    return result
}

typealias Code = Byte
private const val EMPTY = '.'.code.toByte()
private const val SENSOR = 'S'.code.toByte()
private const val BEACON = 'B'.code.toByte()
private const val COVERED = '#'.code.toByte()
