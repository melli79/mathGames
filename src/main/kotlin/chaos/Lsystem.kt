package chaos

import common.math.geometry.Point2D
import common.math.geometry.Vector2D
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.util.Timer
import kotlin.concurrent.timer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

class Lsystem(val rules :Map<Char, String> = mapOf(Pair('A', "A-B--B+A++AA+B-"), Pair('B', "+A-BB--B-A++A+B")),
              val start :String ="A-|B--|B+|A++|A|A+|B-") : MyComponent() {
    override val title = "L-system: Gosper curve"

    companion object {
        val colors = arrayOf(Color.blue, Color.green, Color.cyan, Color.red, Color(128,0,128), Color(192,128,0), Color.pink)
    }

    val depth = 10
    val lambda = 1/sqrt(7.0)
    val unit0 = 0.7*lambda
    private var unit = unit0
    val p = Point2D(0.25, 0.25)
    val dphi = PI/3
    private lateinit var path :String
    private var phi0 = dphi/3
    private var currentDepth = 0

    private var t :Timer? =null

    init {
        reset()
    }

    fun String.subst() = asSequence().joinToString("") { if (it in "+-|") it.toString()  else {
        rules[it]!!
    } }

    fun Graphics.turtle(path :String, unit :Double, start :Point2D, phi0 :Double = 0.0) {
        var p = start;  var phi = phi0
        var index = 0;  color = colors[index]
        var lastX = p.x.roundToInt();  var lastY = p.y.roundToInt()
        for (s in path) when (s) {
            '+' -> phi += dphi
            '-' -> phi -= dphi
            '|' -> {
                index = (index + 1) % colors.size
                color = colors[index]
            }
            else -> {
                p = p.translate(Vector2D(unit*cos(phi), -unit*sin(phi)))
                val x = p.x.roundToInt();  val y = p.y.roundToInt()
                drawLine(lastX,lastY, x,y)
                lastX = x;  lastY = y
            }
        }
        if (t==null)
            t = timer(initialDelay= 1000, period= 1000) { action() }
    }

    override fun keyPressed(event :KeyEvent) = when (event.keyCode) {
        KeyEvent.VK_BACK_SPACE -> reset()
        else -> println("Key ${event.keyCode} '${event.keyChar}' pressed.")
    }

    fun reset() {
        path = start; unit = unit0; phi0 = 0.0
        currentDepth = 0
        repaint()
    }

    override fun paint(g :Graphics) {
        val w = min(width, height)
        val unit = this.unit*w
        val p = Point2D(width*this.p.x+(width-w)/2, height*this.p.y+(height-w)/2)
        g.turtle(path, unit, p, phi0)
    }

    fun action() {
        if (unit<1.5/width) reset()
        if (currentDepth>=depth || unit<5.0/width) return
        path = path.subst()
        unit *= lambda
        phi0 += dphi/3
        ++currentDepth
        repaint()
    }
}
