package racing

import common.math.geometry.*
import common.math.geometry.Point2D.Companion.ORIGIN
import common.math.linspace
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_BYTE_BINARY
import kotlin.math.roundToInt

class VectorRace(val numPlayers :UInt) : KeyBoardComponent() {
    private val positions :Array<Point2D>
    private val velocities = Array(numPlayers.toInt()) { Vector2D.ZERO }

    companion object {
        val colors = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color(128,128,0))

        private val route0 = listOf(Point2D(0.0,0.0), Point2D(1.5,0.0), Point2D(1.5, 1.0),
            Point2D(-0.5,0.0), Point2D(-0.5, 1.0), Point2D(1.0, 1.0)
        )
    }
    private var step = 0
    private var turn = 0
    private var ax = 0.0;  private var ay = 0.0

    private val car :BufferedImage
    private val route :List<Point2D>
    val w = (numPlayers*2u).toDouble()/3
    private val range :Rect
    private lateinit var scale :Rect

    init {
        val v0 = (route0[1] - route0.first()).normed()
        val v = v0.perp()
        val lambda = w*15
        route = route0.map { it.scale(ORIGIN, lambda) }
        val p0 = route.first()
        positions = (-(numPlayers.toInt()/2)..(numPlayers.toInt()/2)).filter { it!=0 || numPlayers%2u==1u }
            .map { p0.translate(v*it.toDouble()) }.toTypedArray()
        (0..< numPlayers.toInt()).map { p -> velocities[p] = v0 }
        car = loadCar()
        range = computeRange()
    }

    override fun help() {
        TODO("Not yet implemented")
    }

    override fun enter() {
        check(turn<velocities.size)
        velocities[turn] += Vector2D(ax, ay)
        turn++; ax = 0.0;  ay = 0.0
        if (turn>=velocities.size) {
            turn = 0
            evolve()
        }
        repaint()
    }

    private fun evolve() {
        velocities.forEachIndexed { i, v :Vector2D ->
            positions[i].translate(v)
        }
    }

    override fun handleKey(event :KeyEvent) {
        println("Unknown key '${event.keyChar}' pressed.")
    }

    override fun paint(g :Graphics) {
        this.scale = range.computeScale(width, height)
        g.color = Color.BLACK
        g.drawRoute(route)
        val position = route.findPosition(positions[turn])
        g.drawString("Step $step, $turn's turn: @%.2f km".format(route.computeDistanceTraveled(position.lambda)),5, 20)
        positions.zip(velocities).forEachIndexed { i :Int, (p :Point2D, v) ->
            g.drawCar(colors[i % colors.size], p, v)
        }
    }

    private fun Graphics.drawCar(color :Color, p :Point2D, v :Vector2D) {
        this.color = color
        val px = scale.px(p.x)
        val py = scale.py(p.y)
        setXORMode(color)
        drawImage(car, px-24, py-6, this@VectorRace)
        setPaintMode()
        val dx = v.x * scale.dx;  val dy = v.y * scale.dy
        drawLine(px, py, px +dx.roundToInt(), py +dy.roundToInt())
        drawLine(
            px + (0.1*(7*dx -dy)).roundToInt(),
            py + (0.1*(9*dy +dx)).roundToInt(),
            px + dx.roundToInt(),
            py + dy.roundToInt()
        )
        drawLine(
            px + (0.1*(7*dx -dy)).roundToInt(),
            py + (0.1*(9*dy -dx)).roundToInt(),
            px + dx.roundToInt(),
            py + dy.roundToInt()
        )
    }

    override fun up() {
        ay += 0.05
        if (ay>= 0.05)
            ay = 0.05
    }

    override fun down() {
        ay -= 0.05
        if (ay<= -0.05)
            ay = -0.05
    }

    override fun left() {
        ax -= 0.05
        if (ax <= -0.05)
            ax = -0.05
    }

    override fun right() {
        ax += 0.05
        if (ax>= 0.05)
            ax = 0.05
    }

    private fun Graphics.drawRoute(route :List<Point2D>) {
        color = Color.BLACK
        drawSegment(route, 0.0, 1.0)
        drawStation(route.first(), route[1], w, "Start")
        drawStation(route.last(), route[route.size-2], -w, "Goal")
    }

    private fun Graphics.drawStation(p :Point2D, p1 :Point2D, w :Double, label :String) {
        val v = (p1-p).normed().perp()*w
        drawLine(scale.px(p.x-v.x), scale.py(p.y-v.y), scale.px(p.x+v.x), scale.py(p.y+v.y))
        drawString(label, scale.px(p.x) + 2, scale.py(p.y+v.y))
    }

    private fun Graphics.drawSegment(ps :List<Point2D>, t0 :Double, t1 :Double) {
        var lastP = ps.bezier(t0)
        val dt = 0.003
        var p = ps.bezier(t0 +dt)
        var (pl, pr) = Circle(lastP, w).intersect(Circle(p, w)).toList()
        for (t in linspace(t0+dt, t1, dt)) {
            p = ps.bezier(t)
            val pNews = drawStep(lastP, p, pl, pr)
            pl = pNews.first;  pr = pNews.second
            lastP = p
        }
        drawStep(lastP, ps.bezier(t1+dt), pl, pr)
    }

    private fun Graphics.drawStep(lastP :Point2D, p :Point2D,
        pl :Point2D, pr :Point2D) :Pair<Point2D, Point2D> {
        val (p1, p2) = Circle(lastP, w).intersect(Circle(p, w)).toList()
        return if ((pl - p1).norm2() < w/2) {
            drawLine(scale.px(pl.x), scale.py(pl.y), scale.px(p1.x), scale.py(p1.y))
            drawLine(scale.px(pr.x), scale.py(pr.y), scale.px(p2.x), scale.py(p2.y))
            Pair(p1, p2)
        } else {
            drawLine(scale.px(pl.x), scale.py(pl.y), scale.px(p2.x), scale.py(p2.y))
            drawLine(scale.px(pr.x), scale.py(pr.y), scale.px(p1.x), scale.py(p1.y))
            Pair(p2, p1)
        }
    }

    private fun Rect.computeScale(width :Int, height :Int) :Rect {
        val dx = width/dx;  val dy = height/dy
        return Rect(x0, y1, dx, -dy)
    }

    private fun computeRange() :Rect {
        val p0 = route.first()
        var xm = p0.x;  var xM = p0.x
        var ym = p0.y;  var yM = p0.y
        (listOf(route.first(), route.last()) + positions).forEach { p ->
            if (p.x < xm)
                xm = p.x
            else if (xM < p.x)
                xM = p.x
            if (p.y < ym)
                ym = p.y
            else if (yM < p.y)
                yM = p.y
        }
        return Rect.of(xm-1.0,ym-w, xM+1.0,yM+w*1.5)
    }

    private fun loadCar() :BufferedImage {
        val car = BufferedImage(24,12,TYPE_BYTE_BINARY)
        val g = car.createGraphics()
        g.color = Color.WHITE
        g.fillRect(0,0,24,12)
        g.color = Color.BLACK
        g.font = g.font.deriveFont(16.0f)
        g.drawString(">>️", 0,10)
        g.dispose()
        return car
    }
}
