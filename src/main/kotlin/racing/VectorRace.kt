package racing

import common.math.geometry.*
import common.math.geometry.Point2D.Companion.ORIGIN
import common.math.linspace
import common.math.sgn
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_BYTE_BINARY
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.roundToInt

class VectorRace(val numPlayers :UInt) : KeyBoardComponent() {
    private val positions :Array<Point2D>
    private val velocities = Array(numPlayers.toInt()) { Vector2D.ZERO }

    companion object {
        val colors = arrayOf(Color.RED, Color.GREEN.darker(), Color.BLUE, Color.CYAN, Color.MAGENTA, Color.ORANGE.darker(), Color(128,128,0))

        val names = arrayOf("Red", "Green", "Blue", "Cyan", "Magenta", "Orange", "Brown")
        private val route0 = listOf(Point2D(0.0,0.0), Point2D(1.5,0.0), Point2D(1.5, 1.0),
            Point2D(-0.5,0.0), Point2D(-0.5, 1.0), Point2D(1.0, 1.0)
        )
    }
    private var step = 0
    private var turn = 0
    private var status = ""
    private val winners = mutableListOf<Int>()
    private var ax = 0.0;  private var ay = 0.0

    enum class Direction {
        RIGHT, UP, LEFT, DOWN;
    }
    private val car :BufferedImage
    private val carLeftwards :BufferedImage
    private val carUpwards :BufferedImage
    private val carDownwards :BufferedImage
    private val route :List<Point2D>
    val w = (numPlayers*2u).toDouble()/3
    private val poss = DoubleArray(numPlayers.toInt()) { 0.0 }
    val names = Companion.names.sliceArray(0..< numPlayers.toInt())

    private val range :Rect
    private lateinit var scale :Rect
    private var font :Font? =null

    init {
        val v0 = (route0[1] - route0.first()).normed()
        val v = v0.perp()
        val lambda = w*15
        route = route0.map { it.scale(ORIGIN, lambda) }
        val p0 = route.first()
        positions = (-(numPlayers.toInt()/2)..(numPlayers.toInt()/2)).filter { it!=0 || numPlayers%2u==1u }
            .map { p0.translate(v*it.toDouble()) }.toTypedArray()
        (0..< numPlayers.toInt()).map { p -> velocities[p] = v0 }
        car = loadCar(Direction.RIGHT)
        carLeftwards = loadCar(Direction.LEFT)
        carUpwards = loadCarUpDown(Direction.UP)
        carDownwards = loadCarUpDown(Direction.DOWN)
        range = computeRange()
    }

    override fun help() {
        TODO("Not yet implemented")
    }

    override fun enter() {
        if (turn>=velocities.size)
            return
        velocities[turn] += Vector2D(ax, ay)
        turn++; ax = 0.0;  ay = 0.0
        while (turn<velocities.size && turn in winners)
            turn++
        if (turn>=velocities.size) {
            evolve()
            turn = 0
            while (turn in winners)
                turn++
        } else
            repaint()
    }

    private fun evolve() {
        status = ""
        (0..< numPlayers.toInt()).sortedByDescending { poss[it] }.forEach { i ->
            val v = velocities[i]
            val newPos = positions[i].translate(v)
            val crashWith = positions.mapIndexed { j :Int, p -> Pair(j, p) }
                .find { (j, p) -> i!=j && (p-newPos).norm2()<=0.25 }?.first
            val coords = route.findPosition(newPos)
            positions[i] = when {
                crashWith!=null -> handleCrash(i, v, crashWith, newPos)
                abs(coords.off) > w -> handleCrash(coords, i)
                coords.lambda >= 1.0 -> handleWinning(i, v, coords)
                else -> newPos
            }
            poss[i] = coords.lambda
        }
        repaint()
    }

    private fun handleWinning(i :Int, v :Vector2D,
        coords :Position
    ) :Point2D {
        if (i !in winners)
            winners.add(i)
        velocities[i] = Vector2D(0.0, if (abs(v.y)<1.0) 0.0  else v.y-sgn(v.y))
        return route.bezier(1.02).translate((route.last()-route[route.size-2]).normed().perp() *coords.off)
    }

    private fun handleCrash(i :Int, v :Vector2D,
        crashWith :Int, newPos :Point2D
    ) :Point2D {
        velocities[i] = (v + velocities[crashWith]) *0.5
        velocities[crashWith] += (velocities[crashWith]-v) *0.5
        status += ", ${names[i]} crashed into ${names[crashWith]}"
         return newPos
    }

    private fun handleCrash(pos :Position, i :Int) :Point2D {
        println(pos)
        velocities[i] = Vector2D.ZERO
        status += ", ${names[i]} crashed into the ${if (w<0) "right"  else "left"} wall"
        val p0 = route.bezier(pos.lambda)
        return p0.translate((route.bezier(pos.lambda+0.1)-p0).normed().perp()*sgn(pos.off).toDouble())
    }

    override fun handleKey(event :KeyEvent) {
        println("Unknown key '${event.keyChar}' pressed.")
    }

    override fun paint(g :Graphics) {
        this.scale = range.computeScale(width, height)
        if (font==null)
            font = g.font
        g.color = Color.BLACK
        g.drawRoute(route)
        positions.zip(velocities).forEachIndexed { i :Int, (p :Point2D, v) ->
            g.drawCar(colors[i % colors.size], p, v)
        }
        if (turn<velocities.size) {
            val p = positions[turn]
            g.drawString(
                "Step $step, ${names[turn]}'s turn: @%.2f km".format(route.computeDistanceTraveled(poss[turn]) /10),
                5, 20)
            val winners = winners.mapIndexed { pos, player -> "${describePosition(pos)}: $player" }
            when {
                winners.isNotEmpty() && status.isNotBlank() -> {
                    g.drawString(winners.joinToString()+", $status", 5,32)
                }
                winners.isNotEmpty() -> g.drawString(winners.joinToString(), 5,32)
                status.isNotBlank() -> g.drawString(status, 5, 32)
            }

            g.drawAccelerationGrid(p.translate(velocities[turn]))
            g.drawNewVelocity(colors[turn % colors.size], p, velocities[turn] +Vector2D(ax, ay))
        } else {
            g.font = font
            g.drawString(winners.mapIndexed { pos, player -> "${describePosition(pos)}: $player" }.joinToString(), 5,32)
            g.color = Color.RED.darker()
            g.font = font!!.deriveFont(Font.BOLD, 72.0f)
            g.drawString("Game Over!!", width/2-200, height/2+40)
        }
    }

    private fun describePosition(pos :Int) = when (pos) {
        0 -> "winner"
        1 -> "second"
        2 -> "third"
        else -> "${pos+1}"
    }

    private fun Graphics.drawAccelerationGrid(p :Point2D) {
        color = Color.GRAY
        for (x0 in -1..0) {
            for (y in -1..1) {
                val px = scale.px(p.x + x0);  val py = scale.py(p.y + y)
                fillOval(px-3, py-3, 5,5)
                drawLine(px, py, scale.px(p.x+x0+1), py)
            }
        }
        for (y in -1..1) {
            val px = scale.px(p.x+1);  val py = scale.py(p.y+y)
            fillOval(px-3, py-3, 5, 5)
        }
        for (x in -1..1) {
            for (y0 in -1..0) {
                val px = scale.px(p.x+x);  val py = scale.py(p.y+y0)
                drawLine(px, py, px, scale.py(p.y+y0+1))
            }
        }
    }

    private fun Graphics.drawCar(color :Color, p :Point2D, v :Vector2D) {
        this.color = color
        val px = scale.px(p.x)
        val py = scale.py(p.y)
        putCar(color, px, py, v)
        drawVector(px, py, v)
    }

    private fun Graphics.putCar(color :Color, px :Int, py :Int, v :Vector2D) {
        setXORMode(color)
        when {
            v.x>0 && v.x>abs(v.y) -> drawImage(car, px-24, py-6, this@VectorRace)
            v.y>0 && v.y>abs(v.x) -> drawImage(carUpwards, px-6, py, this@VectorRace)
            v.x<0 && v.x<-abs(v.y) -> drawImage(carLeftwards, px, py-6, this@VectorRace)
            else -> drawImage(carDownwards, px-6, py-24, this@VectorRace)
        }
        setPaintMode()
    }

    private fun Graphics.drawVector(px :Int, py :Int, v :Vector2D) {
        val dx = v.x * scale.dx;  val dy = v.y * scale.dy
        drawLine(px, py, px + dx.roundToInt(), py + dy.roundToInt())
        drawLine(
            px +(0.1*(7*dx -dy)).roundToInt(),
            py +(0.1*(9*dy +dx)).roundToInt(),
            px +dx.roundToInt(),
            py +dy.roundToInt()
        )
        drawLine(
            px +(0.1*(7*dx -dy)).roundToInt(),
            py +(0.1*(9*dy -dx)).roundToInt(),
            px +dx.roundToInt(),
            py +dy.roundToInt()
        )
    }

    private fun Graphics.drawNewVelocity(color :Color, p :Point2D, v :Vector2D) {
        this.color = color.brighter()
        if (ax != 0.0 || ay != 0.0)
            drawVector(scale.px(p.x), scale.py(p.y), v)
        drawVector(scale.px(p.x+v.x), scale.py(p.y+v.y), v)
    }

    override fun up() {
        ay += 1.0
        if (ay >= 1.0)
            ay = 1.0
        repaint()
    }

    override fun down() {
        ay -= 1.0
        if (ay <= -1.0)
            ay = -1.0
        repaint()
    }

    override fun left() {
        ax -= 1.0
        if (ax <= -1.0)
            ax = -1.0
        repaint()
    }

    override fun right() {
        ax += 1.0
        if (ax>= 1.0)
            ax = 1.0
        repaint()
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

    private fun loadCar(direction :Direction) :BufferedImage {
        val car = BufferedImage(24,12,TYPE_BYTE_BINARY)
        val g = car.createGraphics()
        g.color = Color.WHITE
        g.fillRect(0,0,24,12)
        g.color = Color.BLACK
        g.font = g.font.deriveFont(16.0f)
        if (direction==Direction.RIGHT)
            g.drawString(">>️", 0,10)
        else
            g.drawString("<<️", 0,10)
        g.dispose()
        return car
    }

    private fun loadCarUpDown(direction :Direction) :BufferedImage {
        val car = BufferedImage(12,24,TYPE_BYTE_BINARY)
        val g = car.createGraphics()
        g.color = Color.WHITE
        g.fillRect(0,0,12,24)
        g.color = Color.BLACK
        g.rotate(PI/2)
        if (direction==Direction.RIGHT)
            g.drawImage(car, 12,0,null)
        else
            g.drawImage(carLeftwards, 12,0, null)
        g.dispose()
        return car
    }
}
