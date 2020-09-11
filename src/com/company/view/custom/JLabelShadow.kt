package com.company.view.custom

import java.awt.*
import javax.swing.Icon
import javax.swing.JLabel
import javax.swing.text.StyleConstants.getForeground
import javax.swing.text.StyleConstants.setForeground


class JLabelShadow : JLabel {
    var shadowColor: Color? = null

    private fun setInitialBackgroundColor() {
        shadowColor = Color.BLACK
    }

    constructor() {
        setInitialBackgroundColor()
    }

    constructor(image: Icon?) : super(image) {
        setInitialBackgroundColor()
    }

    constructor(image: Icon?, horizontalAlignment: Int, fonte: Font?) : super(image, horizontalAlignment) {
        setInitialBackgroundColor()
    }

    constructor(text: String?) : super(text) {
        setInitialBackgroundColor()
    }

    constructor(text: String?, horizontalAlignment: Int, fonte: Font?) : super(text, horizontalAlignment) {
        setInitialBackgroundColor()
    }

    constructor(text: String?, icon: Icon?, horizontalAlignment: Int, fonte: Font?) : super(text, icon, horizontalAlignment) {
        setInitialBackgroundColor()
    }

    protected override fun paintComponent(g: Graphics) {
        val graphics = g as Graphics2D


// Remember current graphics parameters
        val oldTextAntialiasingHint = graphics.getRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING)
        val oldForeground: Color = getForeground()

// Set rendering quality
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        run {
            // Paint the shadow
            val shadowGraphics = graphics.create() as Graphics2D
            shadowGraphics.translate(shadowOFFSET_X, shadowOFFSET_Y)
            shadowGraphics.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    0.7f)
            setForeground(this.shadowColor)
            super.paintComponent(shadowGraphics)
            shadowGraphics.dispose()
        }

// Paint the text
        setForeground(oldForeground)
        super.paintComponent(graphics)


// Restore rendering quality
        if (null != oldTextAntialiasingHint) {
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    oldTextAntialiasingHint)
        } else {
// do nothing
        }
    }

    var shadowOFFSET_X = 1
    var shadowOFFSET_Y = 2

}