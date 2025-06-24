package statistics

import chaos.MyComponent
import chaos.MyWindow
import common.math.geometry.Rect
import common.math.geometry.Vector2D
import common.math.pow10
import java.awt.Color
import java.awt.Graphics
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

class CometDiagram<T>(points0 :Collection<T>, val adapter :Adapter<T>) {
    interface Adapter<T> {
        fun getOldW(t :T) :Double
        fun getOldY(t :T) :Double
        fun getNewW(t :T) :Double
        fun getNewY(t :T) :Double
        fun getLabel(t :T) :String?
        fun getColor(t :T) :Color
    }

    data class MPoint(val oldW :Double, val oldY :Double, val newW :Double, val newY :Double, val label :String? =null)
    fun interface Id :Adapter<MPoint> {
        override fun getOldW(t :MPoint) = t.oldW
        override fun getOldY(t :MPoint) = t.oldY
        override fun getNewW(t :MPoint) = t.newW
        override fun getNewY(t :MPoint) = t.newY
        override fun getLabel(t :MPoint) = t.label
    }

    val points :List<T> = points0.toList()
    private val n = points.size

    val oldMean :Double = points.sumOf { adapter.getOldY(it) * adapter.getOldW(it) } / points.sumOf { adapter.getOldW(it) }
    val oldTotalWeight :Double = points.sumOf { adapter.getOldW(it) }
    val newMean :Double = points.sumOf { adapter.getNewY(it) * adapter.getNewW(it) } / points.sumOf { adapter.getNewW(it) }
    val newTotalWeight :Double = points.sumOf { adapter.getNewW(it) }

    val minW :Double = points.minOf { min(adapter.getOldW(it), adapter.getNewW(it)) }
    val maxW :Double = points.maxOf { max(adapter.getOldW(it), adapter.getNewW(it)) }
    val minY :Double = points.minOf { min(adapter.getOldY(it), adapter.getNewY(it)) }
    val maxY :Double = points.maxOf { max(adapter.getOldY(it), adapter.getNewY(it)) }

    val range :Rect
        get() = Rect.of(minW, minY, maxW, maxY)

    fun draw(g :Graphics, scale :Rect) {
        g.font = g.font.deriveFont(11.0f)
        g.drawAxes(scale)
        for (p in points) {
            g.color = adapter.getColor(p)
            g.drawChange(scale.px(adapter.getOldW(p)), scale.py(adapter.getOldY(p)),
                scale.px(adapter.getNewW(p)), scale.py(adapter.getNewY(p)), adapter.getLabel(p))
        }
        g.color = Color.BLACK
        g.drawChange(scale.px(oldTotalWeight/n), scale.py(oldMean),
            scale.px(newTotalWeight/n), scale.py(newMean), "Mean")
    }

    private fun Graphics.drawChange(p0x :Int, p0y :Int, p1x :Int, p1y :Int, label :String? =null) {
        val dx = p1x - p0x;  val dy = p1y - p0y
        val d2 = dx*dx + dy*dy
        if (d2 < 9) {
            if (label!=null && label.isNotBlank()) {
                drawString(label, p0x -5*label.length, p0y -12)
            }
            fillOval(p0x -1, p0y -1, 3, 3)
        } else {
            val v = Vector2D(dx.toDouble(), dy.toDouble()).times(3/sqrt(d2.toDouble())).perp()
            val vx = v.x.roundToInt();  val vy = v.y.roundToInt()
            fillPolygon(intArrayOf(p0x, p1x +vx, p1x -vx, p0x), intArrayOf(p0y, p1y +vy, p1y -vy, p0y), 4)
            if (label!=null && label.isNotBlank()) {
                drawString(label, (p0x+p1x)/2 +3*vx*label.length, (p0y+p1y)/2 +4+3*vy)
            }
        }
    }

    private fun Graphics.drawAxes(scale :Rect) {
        color = Color.GRAY
        val dy = pow10(log10(maxY - minY).roundToInt() - 1)
        val p0y = scale.py(((oldMean+newMean)/2/dy).roundToInt()*dy)
        val dx = pow10(log10(maxW - minW).roundToInt() - 1)
        for (x in (minW/dx).roundToInt()..(maxW/dx).roundToInt()) {
            val px = scale.px(x*dx)
            drawLine(px, p0y-3, px, p0y+3)
            val label = "%.2g".format(x*dx)
            drawString(label, px-4*label.length, p0y+16)
        }
        val p0x = scale.px(((oldTotalWeight+newTotalWeight)/(2*n)/dx).roundToInt()*dx)
        for (y in (minY/dy).roundToInt()..(maxY/dy).roundToInt()) {
            val py = scale.py(y*dy)
            drawLine(p0x-3, py, p0x+3, py)
            val label = "%.3g".format(y*dy)
            drawString(label, p0x-8-7*label.length, py+4)
        }
        drawLine(0, p0y, 10240, p0y)
        drawLine(p0x, 0, p0x, 10240)
    }
}

class DiagramComponent<T>(val data :CometDiagram<T>) : MyComponent() {
    override val title = "Comet Diagram and Simpson Effect"

    private val range = data.range
    private lateinit var scale :Rect

    companion object {
        fun computeScale(width :Int, height :Int, range :Rect) :Rect {
            val dx = 0.9*width/range.dx;  val dy = 0.9*height/range.dy
            return Rect(range.x0-0.05*range.dx, range.y1+0.05*range.dy, dx, -dy)
        }
    }

    override fun paint(g :Graphics) {
        scale = computeScale(width, height, range)
        data.draw(g, scale)
    }
}

fun of(points :Collection<CometDiagram.MPoint>, adapter :CometDiagram.Id) = CometDiagram(points, adapter)

fun main() {
    val cometDiagram = of(listOf(CometDiagram.MPoint(2000.0, 100.0, 1800.0, 102.0, "A"),
            CometDiagram.MPoint(1000.0,50.0,1200.0,51.0, "B"))) {
        if (it.newY>=it.oldY) Color.blue  else Color.orange
    }
    val window = MyWindow(DiagramComponent(cometDiagram))
    window.isVisible = true
}
