package racing

import java.awt.BorderLayout
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JComponent
import javax.swing.JFrame

abstract class KeyBoardComponent : JComponent() {
    abstract fun help()
    abstract fun up()
    abstract fun down()
    abstract fun left()
    abstract fun right()
    abstract fun enter()
    abstract fun handleKey(event :KeyEvent)
}

fun main() {
    val window = MyWindow(VectorRace(3u))
    window.isVisible = true
}

class MyWindow(private val mainComponent :KeyBoardComponent) : JFrame("Vector Race"), KeyListener {
    init {
        this.layout = BorderLayout()
        this.contentPane = mainComponent
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        addKeyListener(this)
    }

    override fun keyTyped(event :KeyEvent) {/* not needed */}

    override fun keyPressed(event :KeyEvent) {/* not needed */}

    override fun keyReleased(event :KeyEvent) = when (event.keyCode) {
        KeyEvent.VK_ESCAPE -> System.exit(0)
        KeyEvent.VK_F1 -> mainComponent.help()
        KeyEvent.VK_UP -> mainComponent.up()
        KeyEvent.VK_DOWN -> mainComponent.down()
        KeyEvent.VK_LEFT -> mainComponent.left()
        KeyEvent.VK_RIGHT -> mainComponent.right()
        KeyEvent.VK_ENTER -> mainComponent.enter()
        else -> mainComponent.handleKey(event)
    }
}
