package reduction

import common.math.findPrimes
import java.awt.BorderLayout
import java.awt.Graphics
import javax.swing.JComponent
import javax.swing.JFrame

class ReductionTree() :JComponent() {
    companion object {
        val limit = 1u.shl(20)
        val primes = findPrimes(limit/2u)

        fun findPrimesBetween(pm :UInt, limit :UInt) = primes.filter { p -> p in pm..limit }
    }

    override fun paint(g :Graphics) {
        var row = 1
        drawEntry(g, row, width/2, Triple(2u,1u,0))
        row++
        var entries = mutableListOf<Triple<UInt, UInt, Int>>()
        drawEntry(g, row, width/2, Triple(4u, 2u, 0))
        entries.add(Triple(4u, 2u, width/2))
        val maxCols = width/32
        while (true) {
            row++
            val newEntries = mutableListOf<Triple<UInt, UInt, Int>>()
            var cols = 0
            for ((n, pm, _) in entries) {
                for (p in findPrimesBetween(pm, n)) {
                    val n1 = n*p
                    if (n1< limit) {
                        newEntries.add(Triple(n1, p, cols++))
                    }
                }
            }
            entries = newEntries
            if (cols<1 || 5<row)
                break
            if (cols>maxCols)
                row += drawInGroups(g, entries, row)
            else {
                val f = width /cols;  val offset = width/(2*cols)
                entries = entries.map { entry ->
                    val col = offset+ f* entry.third
                    drawEntry(g, row, col, entry)
                    Triple(entry.first, entry.second, col)
                }.toMutableList()
            }
        }
    }

    private fun drawInGroups(g :Graphics, entries :MutableList<Triple<UInt, UInt, Int>>, row0 :Int) :Int {
        var lastP = 2u
        var col = 0;  var row = 0
        for (i in entries.indices) {
            val (n, p, _) = entries[i]
            if (p<lastP) {
                col++
                row = 0
            }
            lastP = p
            entries[i] = Triple(n, p, row++)
        }
        val f = width/(col+1);  val offset = width/(2*col+1)
        col = -1
        for (e in entries) {
            if (e.third<1) col++
            drawEntry(g, row0 +e.third, f*col +offset, e)
        }
        return entries.map { e -> e.third }.max() +1
    }

    private fun drawEntry(g :Graphics, row :Int, column :Int, e :Triple<UInt, UInt, Int>) {
        g.drawString("${e.first} (${e.second})", column, 16*row)
    }
}

class MyWindow(rootPane :JComponent) :JFrame("Reduction Game") {
    init {
        layout = BorderLayout()
        contentPane = rootPane
        setSize(2048, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
    }
}

fun main() {
    val w = MyWindow(ReductionTree())
    w.isVisible = true
}
