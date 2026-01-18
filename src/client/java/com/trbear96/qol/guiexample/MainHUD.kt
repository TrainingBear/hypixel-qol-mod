package com.trbear96.qol.guiexample

import com.fasterxml.jackson.databind.node.ObjectNode
import com.trbear96.*
import com.trbear96.qol.core.JsonConfig.Companion.getOrMakeObject
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
    val tombol = mutableListOf<UIComponent>()

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
            if (route == picked) {
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
        // JAGA ENGGREK
        tombol.add(
            UIBlock(
                if (jagaenggrek) Color(140, 140, 70).toConstraint()
                else Color(186, 186, 186).toConstraint()
            )
                .modifTombol("Jaga Enggrek", container, onklik = {
                    config.getOrMakeObject(METODEPANENSAWIT).put(
                        JAGAENGGREK,
                        !config.get(METODEPANENSAWIT).get(JAGAENGGREK).asBoolean()
                    )
                    hasConfigChanged = true
                    tombol[tombol.size - 1].animate { // apply
                        setColorAnimation(
                            Animations.IN_OUT_BOUNCE,
                            0.15f,
                            Color(140, 140, 70).toConstraint()
                        )
                    }
                    println("Set jaga enggrek to: ${config.get(METODEPANENSAWIT).get(JAGAENGGREK).asBoolean()}")
                }, onkeluar = {
                    this.animate { // apply
                        setColorAnimation(
                            Animations.IN_OUT_BOUNCE,
                            0.5f,
                            if (config.get(METODEPANENSAWIT).get(JAGAENGGREK).asBoolean())
                                Color(140, 140, 70).toConstraint()
                            else Color(186, 186, 186).toConstraint()
                        )
                    }
                })
        )
        // CAPEK MSG
        tombol.add(
            UIBlock(
                if (capek) Color(140, 140, 70).toConstraint()
                else Color(186, 186, 186).toConstraint()
            )
                .modifTombol("Capek MSG", container, onklik = {
                    config.getOrMakeObject(METODEPANENSAWIT).put(
                        CAPEK,
                        !config.get(METODEPANENSAWIT).get(CAPEK).asBoolean()
                    )
                    hasConfigChanged = true
                    tombol[tombol.size - 1].animate { // apply
                        setColorAnimation(
                            Animations.IN_OUT_BOUNCE,
                            0.15f,
                            Color(140, 140, 70).toConstraint()
                        )
                    }
                    println("Set capek msg to: ${config.get(METODEPANENSAWIT).get(CAPEK).asBoolean()}")
                }, onkeluar = {
                    this.animate { // apply
                        setColorAnimation(
                            Animations.IN_OUT_BOUNCE,
                            0.25f,
                            if (config.get(METODEPANENSAWIT).get(CAPEK).asBoolean())
                                Color(140, 140, 70).toConstraint()
                            else Color(186, 186, 186).toConstraint()
                        )
                    }
                })
        )
        // CAPEK MSG
        tombol.add(
            UIBlock(
                if (conceal) Color(140, 140, 70).toConstraint()
                else Color(186, 186, 186).toConstraint()
            )
                .modifTombol("Conceal", container, onklik = {
                    config.getOrMakeObject(METODEPANENSAWIT).put(
                        CONCEAL,
                        !config.get(METODEPANENSAWIT).get(CONCEAL).asBoolean()
                    )
                    hasConfigChanged = true
                    tombol[tombol.size - 1].animate { // apply
                        setColorAnimation(
                            Animations.IN_OUT_BOUNCE,
                            0.15f,
                            Color(140, 140, 70).toConstraint()
                        )
                    }
                    println("Set conceal to: ${config.get(METODEPANENSAWIT).get(CAPEK).asBoolean()}")
                }, onkeluar = {
                    this.animate { // apply
                        setColorAnimation(
                            Animations.IN_OUT_BOUNCE,
                            0.25f,
                            if (config.get(METODEPANENSAWIT).get(CAPEK).asBoolean())
                                Color(140, 140, 70).toConstraint()
                            else Color(186, 186, 186).toConstraint()
                        )
                    }
                })
        )
        // APPLY
        tombol.add(
            UIBlock(Color(120, 120, 120).toConstraint())
                .modifTombol("Apply", container, onklik = {
//                    if (!hasConfigChanged) return@modifTombol
                    aturUlangConfig()
                    confighandler.save(MYSAWIT)
                    hasConfigChanged = false
                    println("[MySawit] Config saved!")
                    this.animate {
                        setColorAnimation(
                            Animations.IN_OUT_BOUNCE,
                            0.25f,
                            Color(120, 120, 120).toConstraint()
                        )
                    }
                }, onkeluar = {
                    this.animate { // apply
                        setColorAnimation(
                            Animations.IN_OUT_BOUNCE,
                            0.25f,
                            if (hasConfigChanged) Color(140, 140, 70).toConstraint()
                            else Color(120, 120, 120).toConstraint()
                        )
                    }
                })
        )
        /*        UIText("Pilih sawit yang kamu suka!", shadow = false).constrain {
        *            x = 2.pixels()
        *            y = CenterConstraint()
        *            textScale = 1.pixels()
        *            color = Color.GREEN.darker().toConstraint()
        *        } childOf container
        *
        *       Modifier.gradient(top = Color(0x091323), Color.BLACK).applyToComponent(window)
         */
    }

    var indekstombol = 0
    fun UIBlock.modifTombol(
        label: String,
        parent: UIComponent,
        onklik: UIBlock.() -> Unit,
        onkeluar: UIBlock.() -> Unit
    ): UIBlock {
        this.constrain {
            x = (10 + (indekstombol * 10)).percent()
            y = 90.percent()
            width = 40.pixels()
            height = 16.pixels()
        }.onMouseClick { onklik() }.onMouseEnter {
            this.animate {
                setColorAnimation(
                    Animations.IN_EXP, 0.25f,
                    Color(120, 120, 100).toConstraint(), 0f
                )
            }
        }.onMouseLeave { onkeluar() } childOf parent
        UIText(label, shadow = false).constrain {
            x = 1.pixels()
            y = CenterConstraint()
            textScale = 0.8.pixels()
            color = Color(0x091323).toConstraint()
        } childOf this
        indekstombol += 1
        return this
    }

    fun ScrollComponent.addRoute(name: String): UIComponent {
        val c = UIBlock(Color(207, 207, 196)).constrain {
            y = SiblingConstraint(2f)
            height = 10.pixels
            width = 100.percent
        }.onMouseClick {
            rutes.forEach { r ->
                r.animate {
                    setColorAnimation(
                        Animations.IN_OUT_BOUNCE,
                        0.5f,
                        Color(207, 207, 196).toConstraint()
                    )
                }
            }
            config.getOrMakeObject(METODEPANENSAWIT).put(TIPE, name)
            tombol[tombol.size - 1].animate { // apply
                setColorAnimation(
                    Animations.IN_OUT_BOUNCE,
                    0.15f,
                    Color(140, 140, 70).toConstraint()
                )
            }
            hasConfigChanged = true
            val str = config.get(METODEPANENSAWIT).get(TIPE).asText()
            println("new pick! $str")
            this.animate {
                setColorAnimation(
                    Animations.OUT_SIN,
                    0.5f,
                    Color(140, 140, 70).toConstraint(),
                    0f
                )
            }
        }.onMouseEnter {
            if (config.get(METODEPANENSAWIT).get(TIPE).asText() == name) return@onMouseEnter
            this.animate {
                setColorAnimation(
                    Animations.OUT_EXP,
                    0.5f,
                    Color(120, 120, 100).toConstraint(),
                    0f
                )
            }
        }.onMouseLeave {
            if (config.get(METODEPANENSAWIT).get(TIPE).asText() == name) return@onMouseLeave
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