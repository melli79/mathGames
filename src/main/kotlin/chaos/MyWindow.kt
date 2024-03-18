package chaos

import java.awt.BorderLayout
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JComponent
import javax.swing.JFrame
import kotlin.system.exitProcess

abstract class MyComponent :JComponent() {
    abstract val title :String

    open fun scaleUp() {}
    open fun scaleDown() {}
}

class MyWindow(val content :MyComponent) : JFrame(), KeyListener {
    init {
        this.layout = BorderLayout()
        this.contentPane = content
        defaultCloseOperation = EXIT_ON_CLOSE
        addKeyListener(this)
        setSize(800, 600)
    }

    override fun keyTyped(event :KeyEvent) {/* not needed */}

    override fun keyPressed(event :KeyEvent) {/* not needed */}

    override fun keyReleased(event :KeyEvent) = when (event.keyCode) {
        KeyEvent.VK_PLUS, KeyEvent.VK_EQUALS -> content.scaleUp()
        KeyEvent.VK_MINUS -> content.scaleDown()
        KeyEvent.VK_ESCAPE, KeyEvent.VK_Q, KeyEvent.VK_X -> exitProcess(0)
        else -> {/* ignore */}
    }
}

fun main() {
    val window = MyWindow(LogComponent())
    window.isVisible = true
}