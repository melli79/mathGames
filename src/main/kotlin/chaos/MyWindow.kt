package chaos

import java.awt.BorderLayout
import java.awt.event.ComponentListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.util.*
import javax.swing.JComponent
import javax.swing.JFrame
import kotlin.system.exitProcess

val random = Random(System.currentTimeMillis())

abstract class MyComponent :JComponent() {
    abstract val title :String

    open fun scaleUp() {}
    open fun scaleDown() {}
    open fun mouseClicked(event :MouseEvent) {}
    open fun keyPressed(event :KeyEvent) {}
}

class MyWindow(val content :MyComponent) : JFrame(), KeyListener, MouseListener {
    init {
        this.layout = BorderLayout()
        this.contentPane = content
        defaultCloseOperation = EXIT_ON_CLOSE
        addKeyListener(this)
        addMouseListener(this)
        if (content is ComponentListener)
            addComponentListener(content)
        setSize(800, 600)
        title = content.title
    }

    override fun keyTyped(event :KeyEvent) {/* not needed */}

    override fun keyPressed(event :KeyEvent) {/* not needed */}

    override fun keyReleased(event :KeyEvent) = when (event.keyCode) {
        KeyEvent.VK_PLUS, KeyEvent.VK_EQUALS -> content.scaleUp()
        KeyEvent.VK_MINUS -> content.scaleDown()
        KeyEvent.VK_ESCAPE, KeyEvent.VK_Q, KeyEvent.VK_X -> exitProcess(0)
        else -> content.keyPressed(event)
    }

    override fun mouseClicked(event :MouseEvent) = content.mouseClicked(event)

    override fun mousePressed(event :MouseEvent) {
        // ignore
    }

    override fun mouseReleased(event :MouseEvent) {
        // ignore
    }

    override fun mouseEntered(event :MouseEvent) {
        // ignore
    }

    override fun mouseExited(event :MouseEvent) {
        // ignore
    }
}

fun main() {
    val window = MyWindow(Lsystem())  // NewtonFractal())  // LogFractal(LogFractal.ColorPattern.OSC2)) // MyWindow(RealLog()) // Mandelbrot()) // BrownianTree(BrownianTree.Orientation.Circular, BrownianTree.Height.Width))
    window.isVisible = true
}
