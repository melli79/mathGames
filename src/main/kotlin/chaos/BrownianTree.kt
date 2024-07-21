package chaos

import common.math.geometry.Rect
import common.math.statistics.cumsum
import common.math.geometry.x1
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyEvent
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class BrownianTree(private var orn :Orientation, private var h :Height) :MyComponent() {
    override val title = "Brownian Tree"

    enum class Orientation {
        Linear, Circular;

        operator fun inc() = when (this) {
            Linear -> Circular
            Circular -> Linear
        }
    }

    enum class Height {
        Generation, Width;

        operator fun inc() = when (this) {
            Generation -> Width
            Width -> Generation
        }
    }

    interface Scale {
        fun px(pos :Double, height :Double) :Int
        fun py(pos :Double, height :Double) :Int
    }

    val range = Rect.of(0.0, 0.0, 1.0, 1.0)
    private lateinit var scale :Scale

    data class Node(val pos :Double, val gen :UShort, val height :Double, val parent :Node?) {}

    private var depth = 7
    private var tree = generateTree()

    override fun keyPressed(event: KeyEvent) {
        when (event.keyCode) {
            KeyEvent.VK_SPACE -> tree = generateTree()
            KeyEvent.VK_H -> h++
            KeyEvent.VK_O -> orn++
        }
        repaint()
    }

    override fun scaleUp() {
        depth++;  if (depth>12) depth = 12
        tree = generateTree()
        repaint()
    }

    override fun scaleDown() {
        depth--;  if (depth<1) depth = 1;
        tree = generateTree()
        repaint()
    }

    private fun generateTree(): List<Node> {
        val root = Node(0.5, 0u, 0.0, null)
        val result = mutableListOf(root)
        var row = listOf(root)
        repeat (depth) { gen -> if (row.isNotEmpty()) {
            val first = row.first()
            val last = row.last()
            row = (listOf(Node(2*range.x0-first.pos, gen.toUShort(), first.height, null)) + row +
                    listOf(Node(2*range.x1()-last.pos, gen.toUShort(), last.height, null)))
                    .windowed(3).flatMap { (lNode, parent, rNode) ->
                val left = (lNode.pos+parent.pos)/2;  val right = (rNode.pos+parent.pos)/2
                val poss = (1..random.nextInt(4, 7)).map { random.nextDouble() }.cumsum()
                val scale = (right-left) / poss.last()
                (poss.drop(1).dropLast(1) zip poss.windowed(3).map { (l,_,r) -> (r-l)/2 }).mapNotNull { (p, w) ->
                    val child = Node(left +scale* p, (gen+1).toUShort(), min(range.y1, parent.height +0.2*w), parent)
                    result.add(child)
                    if (scale*w >= 0.001) child
                    else null
                }
            }
        }}
        return result
    }

    override fun paint(g :Graphics) {
        scale = computeScale(range, width, height)
        g.color = Color.BLACK
        tree.forEach { node ->
            if (node.parent!=null) {
                val p = node.parent;  val ph = if (h==Height.Width) p.height  else 0.1*p.gen.toInt()
                val rx = scale.px(p.pos, ph);  val ry = scale.py(p.pos, ph)
                val nh = if (h==Height.Width) node.height  else 0.1*node.gen.toInt()
                val px = scale.px(node.pos, nh);  val py = scale.py(node.pos, nh)
                g.drawLine(rx,ry, px,py)
                g.fillOval(px-1, py-1, 3,3)
            } else {
                val px = scale.px(node.pos, node.height);  val py = scale.py(node.pos, node.height)
                g.fillOval(px-2, py-2, 5,5)
            }
        }
    }

    private fun computeScale(range :Rect, width :Int, height :Int) :BrownianTree.Scale = when (orn) {
        Orientation.Circular -> {
            val x0 = -0.5*width;  val y0 = -0.5*height
            CircScale(Rect(-1.0,-1.0, 0.5*width, 0.5*height))
        }
        Orientation.Linear -> {
            val dx = width / range.dx;  val dy = height / range.dy
            LinScale(Rect(range.x0, range.y0, dx, dy))
        }
    }

    data class LinScale(val rect :Rect) :Scale {
        override fun px(pos: Double, height: Double) = rect.px(pos)
        override fun py(pos: Double, height: Double) = rect.py(height)
    }

    data class CircScale(val rect: Rect) :Scale {
        override fun px(pos: Double, height: Double) = rect.px(height*cos(2*PI*pos))
        override fun py(pos: Double, height: Double) = rect.py(height*sin(2*PI*pos))
    }
}
