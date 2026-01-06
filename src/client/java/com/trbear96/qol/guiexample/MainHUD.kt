package com.trbear96.qol.guiexample

import com.trbear96.picked
import com.trbear96.routes
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.*
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.ScissorEffect
import java.awt.Color
import java.net.URI

/**
 * MainHUD is a fully fleshed example of a lot of Elementa's features
 * and how to effectively use them. This example is a "sticky note pad"
 * where users can create, delete, move, and write on little sticky notes.
 *
 * The example won't look particularly pretty, but that is up to the programmer
 * to design their GUIs how they wish.
 */
object MainHUD : WindowScreen(ElementaVersion.V10) {
    val rutes = mutableListOf<UIComponent>()
    init {
        val container = UIContainer().constrain {
            x = CenterConstraint()
            y = 2.pixels
            height = 75.percent()
            width = 65.percent()
        } childOf window
//        effect OutlineEffect(Color.cyan, 2f)
        UIImage.ofResource("/assets/mysawitqol/logo.png").constrain {
            x = CenterConstraint() + 10.pixel
            y = 2.pixels()
            height = 125.pixel
            width = 125.pixel
        } childOf container

        val scroll = ScrollComponent().constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + 5.pixels
            width = 98.percent
            height = 30.percent - 3.pixels
        } childOf container

        for (route in routes.keys) {
            val rute = scroll.addRoute(route)
            if(route == picked) {
                rute.animate {
                    setColorAnimation(
                        Animations.OUT_SIN,
                        0.5f,
                        Color(140, 140, 70).toConstraint(),
                        0f
                    )
                }
                println("picked $picked")
            }
        }

//        UIText("Pilih sawit yang kamu suka!", shadow = false).constrain {
//            x = 2.pixels()
//            y = CenterConstraint()
//            textScale = 1.pixels()
//            color = Color.GREEN.darker().toConstraint()
//        } childOf container
//        Modifier.gradient(top = Color(0x091323), Color.BLACK).applyToComponent(window)
    }

    fun ScrollComponent.addRoute(name: String): UIComponent {
        val c = UIBlock(Color(207, 207, 196)).constrain {
            y = SiblingConstraint(2f)
            height = 10.pixels
            width = 100.percent
        }.onMouseClick {
            println("new pick! $name")
            rutes.forEach { r ->
                r.animate {
                    setColorAnimation(
                        Animations.IN_OUT_BOUNCE,
                        0.5f,
                        Color(207, 207, 196).toConstraint()
                    )
                }
            }

            picked = name
            this.animate {
                setColorAnimation(
                    Animations.OUT_SIN,
                    0.5f,
                    Color(140, 140, 70).toConstraint(),
                    0f
                )
            }
        }.onMouseEnter {
            if(picked == name) return@onMouseEnter
            this.animate {
                setColorAnimation(
                    Animations.OUT_EXP,
                    0.5f,
                    Color(120, 120, 100).toConstraint(),
                    0f
                )
            }
        }.onMouseLeave {
            if(picked == name) return@onMouseLeave
            animate {
                setColorAnimation(
                    Animations.OUT_EXP,
                    0.5f,
                    Color(207, 207, 196).toConstraint()
                )
            }
        } childOf this
        UIText(name, shadow = false).constrain {
            x = 1.pixels()
            y = CenterConstraint()
            textScale = 0.8.pixels()
            color = Color(0x091323).toConstraint()
        } childOf c
        rutes.add(c)
        return c
    }

    class StickyNote : UIBlock(Color.BLACK) {
        private var isDragging: Boolean = false
        private var dragOffset: Pair<Float, Float> = 0f to 0f
        private val textArea: UITextInput

        init {
            constrain {
                x = CenterConstraint()
                y = CenterConstraint()

                width = 150.pixels()
                height = 100.pixels()
            }

            onMouseClick {
                parent.removeChild(this)
                parent.addChild(this)
            }

            val topBar = UIBlock(Color.YELLOW).constrain {
                x = 1.pixel()
                y = 1.pixel()

                width = 100.percent() - 2.pixels()

                height = 24.pixels()
            }.onMouseClick { event ->
                isDragging = true
                dragOffset = event.absoluteX to event.absoluteY
            }.onMouseRelease {
                isDragging = false
            }.onMouseDrag { mouseX, mouseY, _ ->
                if (!isDragging) return@onMouseDrag

                val absoluteX = mouseX + getLeft()
                val absoluteY = mouseY + getTop()

                val deltaX = absoluteX - dragOffset.first
                val deltaY = absoluteY - dragOffset.second

                dragOffset = absoluteX to absoluteY

                val newX = this@StickyNote.getLeft() + deltaX
                val newY = this@StickyNote.getTop() + deltaY

                this@StickyNote.setX(newX.pixels())
                this@StickyNote.setY(newY.pixels())
            } childOf this

            UIText("X", shadow = false).constrain {
                x = 4.pixels(alignOpposite = true)
                y = CenterConstraint()

                color = Color.BLACK.toConstraint()

                textScale = 2.pixels()
            }.onMouseEnter {
                animate {
                    setColorAnimation(Animations.OUT_EXP, 0.5f, Color.RED.toConstraint())
                }
            }.onMouseLeave {
                animate {
                    setColorAnimation(Animations.OUT_EXP, 0.5f, Color.BLACK.toConstraint())
                }
            }.onMouseClick { event ->
                this@StickyNote.parent.removeChild(this@StickyNote)

                event.stopPropagation()
            } childOf topBar

            val textHolder = UIBlock(Color(80, 80, 80)).constrain {
                x = 1.pixel()
                y = SiblingConstraint()

                width = RelativeConstraint(1f) - 2.pixels()

                height = FillConstraint()
            } childOf this

            textHolder effect ScissorEffect()

            textArea = (UITextInput(placeholder = "Enter your note...").constrain {
                x = 2.pixels()
                y = 2.pixels()
                height = FillConstraint() - 2.pixels()
            }.onMouseClick {
                grabWindowFocus()
            } childOf textHolder) as UITextInput
        }
    }
}