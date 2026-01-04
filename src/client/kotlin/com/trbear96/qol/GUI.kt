package com.trbear96.qol

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.UMatrixStack
import net.minecraft.client.gui.Click
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.input.KeyInput
import net.minecraft.text.Text

class GUI : Screen(Text.literal("GUI")) {
    val window = Window(ElementaVersion.V10)

    override fun init() {
        val panel = UIContainer().constrain {
            x = 100.pixels()
            width = 100.pixels()
            height = 100.pixels()
        } childOf window

        UIText("Hello World!").constrain {
            x = CenterConstraint()
            y = CenterConstraint()
        } childOf panel
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.render(context, mouseX, mouseY, deltaTicks)
        window.draw(UMatrixStack(context!!.matrices))
    }

    override fun mouseReleased(click: Click?): Boolean {
        window.mouseRelease()
        return super.mouseReleased(click)
    }

    override fun mouseClicked(click: Click?, doubled: Boolean): Boolean {
        window.mouseClick(click!!.x, click.y, click.button())
        return super.mouseClicked(click, doubled)
    }

    override fun keyPressed(input: KeyInput?): Boolean {
        window.keyType(input!!.asNumber().toChar(), input.key);
        return super.keyPressed(input)
    }
}