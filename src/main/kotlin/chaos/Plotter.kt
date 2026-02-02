package chaos

import common.math.geometry.Rect
import common.math.geometry.x1
import java.awt.Color
import java.awt.Graphics
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.text.format

class Plotter(private val points: List<Pair<Double, Double>>, private val linReg: Pair<Double, Double>,
          override val title :String = "y = %.3g ln x %+.3g".format(linReg.first, linReg.second)) : MyComponent() {
    private val range = computeRange(points)

    private fun computeRange(points: List<Pair<Double, Double>>) :Rect {
        val first = points.first()
        var xm = 0.0; var xM = xm
        var ym = first.second;  var yM = ym
        for (p in points) {
            if (p.first<xm)
                xm = p.first
            else if (xM<p.first)
                xM = p.first
            if (p.second<ym)
                ym = p.second
            else if (yM<p.second)
                yM = p.second
        }
        println("($xm, $xM)x($ym, $yM)")
        return Rect.of(min(xm, -0.5), min(ym, -5.0), xM, yM)
    }

    private lateinit var scale :Rect

    // Paints scaled points, axes and a regression line
    override fun paint(g :Graphics) {
        scale = computeScale(width, height)
        g.drawAxes()
        // var lastX = scale.px(points.first().first)
        // var lastY1 = scale.py(points.first().second)
        g.color = Color.GREEN
        for (p in points) {
            val px = scale.px(p.first);  val py1 = scale.py(p.second)
            g.fillOval(px-2,py1-2, 5, 5)
            // lastX = px;  lastY1 = py1
        }
        g.color = Color.BLUE
        val y0 = linReg.first*range.x0 +linReg.second;  val y1 = linReg.first*range.x1() +linReg.second
        g.drawLine(scale.px(range.x0), scale.py(y0), scale.px(range.x1()), scale.py(y1))
        g.drawString(title, 150, 50)
    }

    private fun Graphics.drawAxes() {
        color = Color.GRAY
        val x0 = scale.px(0.0)
        drawLine(x0,0, x0,height)
        val y0 = scale.py(0.0)
        // drawString("0", x0-10,y0+14)
        val dx = 1.0 // 0.0.pow(log10(max(abs(range.x0), abs(range.x1()))).toInt()-1)
        for (x1 in (range.x0/dx).roundToInt()..(range.x1()/dx).roundToInt()) {
            val x = x1*dx
            val px = scale.px(x)
            drawLine(px, y0, px, y0 + 5)
            drawString("%.2g".format(10.0.pow(x)), px - 10, y0 + 16)
        }

        drawLine(0,y0, width,y0)
        val dy = 10.0 // .pow(((log10(max(abs(range.y0), abs(range.y1)))).toInt()-1)).roundToInt().toDouble()
        for (y1 in (range.y0/dy).roundToInt()..(range.y1/dy).roundToInt()) {
            val y = y1*dy
            val py = scale.py(y)
            drawLine(x0, py, x0 + 5, py)
            drawString("%.0f".format(y), x0 - 24, py + 4)
        }
    }

    private fun computeScale(width: Int, height: Int):Rect {
        val dx = width/range.dx*0.95;  val dy = height/range.dy*0.95
        return Rect(range.x0, range.y1, dx, -dy)
    }
}