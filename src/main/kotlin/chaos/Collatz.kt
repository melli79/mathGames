@file:OptIn(ExperimentalUnsignedTypes::class)

package chaos

import common.math.geometry.Rect
import common.math.geometry.x1
import java.awt.Color
import java.awt.Graphics
import kotlin.collections.map
import kotlin.math.*

class Collatz(val limit :UInt =1000u) :MyComponent() {
    override val title = "Collatz Lengths"

    val lengths = mutableListOf(0u, 0u)
    val maxes :List<UInt>
    val range :Rect
    private lateinit var scale :Rect

    init {
        for (s in 2u..limit) {
            val seq = loop(s).reversed()
            val it = seq.iterator()
            if (it.hasNext()) {
                var l = lengths[it.next().toInt()]
                while (it.hasNext()) {
                    l++
                    val n = it.next().toInt()
                    if (n<limit.toInt()) {
                        if (n > lengths.size) lengths.addAll(UIntArray(n - lengths.size) { 0u })
                        if (n in 0..<lengths.size) lengths[n] = l
                        else lengths.add(l)
                    }
                }
            }
        }
        maxes = lengths.cumulate(0u, ::max)
        range = Rect.of(0.0,0.0, lengths.size.toDouble(), lengths.max().toDouble())
        println("$range")
    }

    fun loop(s :UInt) :List<UInt> {
        val result = mutableListOf<UInt>()
        var n = s
        while (n>1u && (n>=lengths.size.toUInt() || lengths[n.toInt()]==0u)) {
            result.add(n)
            if (n%2u==0u)
                n/=2u
            else
                n = 3u*n+1u
        }
        result.add(n)
        return result
    }

    override fun paint(g :Graphics) {
        this.scale = computeScale(width, height, range)
        g.drawAxes()
        var lastM = scale.py(maxes.first().toDouble())
        var lastPx = scale.px(0.0)
        var lastC = scale.py(0.0)
        lengths.mapIndexed { n, l -> Pair(n, l) }.drop(1).forEach { (n, l) ->
            g.color = Color.green.darker()
            val px = scale.px(n.toDouble())
            g.fillRect(px -1, scale.py(l.toDouble())-1, 3, 3)
            g.color = Color.blue.darker()
            val py = scale.py(maxes[n].toDouble())
            g.drawLine(lastPx, lastM, px, py)
            g.color = Color.darkGray
            val ln = ln(n.toDouble())
            val pc = scale.py(ln*exp(ln/25)*21.5)
            g.drawLine(lastPx, lastC, px, pc)
            lastPx = px;  lastM = py;  lastC = pc
        }
        g.color = Color.black
        var px = scale.px(0.0)+20
        g.drawLine(px, 20, px+15, 20); px += 20
        g.drawString("l ≈ 21.5*ln(n)*n^(1/25)", px, 25)
    }

    fun Graphics.drawAxes() {
        color = Color.gray
        val px0 = scale.px(0.0)
        drawLine(px0, 0, px0, height-1)
        drawString("l", px0-25,30)
        val dy = 10.0.pow(log10(max(abs(range.y0), abs(range.y1))).toInt()-1)
        for (y1 in (range.y0/dy).roundToInt()..(range.y1/dy).roundToInt()) if (y1!=0) {
            val y = y1*dy
            val py = scale.py(y)
            drawLine(px0-5,py, px0+5,py)
            drawString(y.roundToInt().toString(), px0-35,py+4)
        }
        val py0 = scale.py(0.0)
        drawLine(0,py0, width-1, py0)
        drawString("n", width-50,py0+40)
        val dx = 10.0.pow(log10(max(abs(range.x0), abs(range.x1()))).toInt()-1)
        for (x1 in (range.x0/dx).roundToInt()..(range.x1()/dx).roundToInt()) {
            val x = x1*dx
            val px = scale.px(x)
            drawLine(px, py0-5, px, py0+5)
            drawString(x.roundToInt().toString(), px-16,py0+20)
        }
    }

    fun computeScale(width :Int, height :Int, range :Rect) :Rect {
        val dx = 0.9*width/range.dx
        val dy = 0.9*height/range.dy
        return Rect(range.x0+0.5*(range.dx-width/dx), range.y1-0.5*(range.dy-height/dy), dx, -dy)
    }
}

fun <T> List<T>.cumulate(init :T, cumulator:(T,T)->T) :List<T> {
    var last = init
    return this.map { t -> last = cumulator(last, t); last }
}

fun main() {
    val myWindow = MyWindow(Collatz(100_000u))
    myWindow.isVisible = true
}
