package org.firstinspires.ftc.teamcode.subsystem

import org.firstinspires.ftc.teamcode.prism.Color
import org.firstinspires.ftc.teamcode.prism.GoBildaPrismDriver
import org.firstinspires.ftc.teamcode.prism.PrismAnimations

class LEDs(
    val leftDriver: GoBildaPrismDriver,
    val rightDriver: GoBildaPrismDriver
) {
    var prismAnimation: PrismAnimations.AnimationBase = PrismAnimations.Solid(Color.TRANSPARENT)
        set(prismAnimation) {
            field = prismAnimation
            for (driver in listOf(leftDriver, rightDriver)) {
                driver.insertAndUpdateAnimation(
                    GoBildaPrismDriver.LayerHeight.LAYER_0,
                    prismAnimation
                )
            }
        }
    init {
        prismAnimation = PrismAnimations.Solid(Color.TRANSPARENT)
    }
    fun matchAlliance() {
        prismAnimation = PrismAnimations.SineWave(
            when (Alliance.alliance) {
                Alliance.RED -> Color.RED
                Alliance.BLUE -> Color.BLUE
            },
            Color.TRANSPARENT
        )
    }
}