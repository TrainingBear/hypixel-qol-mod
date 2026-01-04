package com.trbear96.qol

import com.trbear96.qol.guiexample.ExampleGui
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.effect
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.plus
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.UMatrixStack
import net.minecraft.client.gui.Click
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.input.KeyInput
import net.minecraft.text.Text
import java.awt.Color

fun createMainWindow(): Window {
    println("Created a new gui!")
    val window = Window(ElementaVersion.V10)
    val bar = UIBlock().constrain {
        x = 2.pixels()
        y = SiblingConstraint() + 5.pixels()
        width = 150.pixels()
        height = 50.pixels()
    } childOf window

    val container = UIContainer().constrain {
        x = 0.pixels(true)
        width = ChildBasedSizeConstraint(padding = 2f)
        height = ChildBasedMaxSizeConstraint()
    } childOf bar effect OutlineEffect(Color.BLUE, 2f)

    repeat(3) {
        UIBlock(Color.RED).constrain {
            x = SiblingConstraint(padding = 2f)
            width = 25.pixels()
            height = 25.pixels()
        } childOf container
    }
    return window
}

class GUI(val window: Window) : Screen(Text.literal("GUI")) {

    override fun init() {
        super.init()
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
//        super.render(context, mouseX, mouseY, deltaTicks)
        println("Rendering.. ")
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