package org.firstinspires.ftc.teamcode.subsystem.transfer

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.hardware.Servo

@Configurable
class Deflowerer(
    val servo: Servo
) {
    @Configurable
    enum class Mode(@JvmField var position: Double) {
        UP(0.85),
        DOWN(1.0)
    }

    companion object {
        @JvmField var WIGGLE_DISTANCE: Double = 0.001
        @JvmField var WIGGLE_PERIOD_MS: Double = 200.0
        @JvmField var DAMPING_BASE: Double = 20.0
    }

    init {
        up()
    }

    var mode: Mode = Mode.UP
        set(mode) {
            field = mode
            targetPosition = mode.position
        }

    var targetPosition: Double = mode.position
    var nonWigglePosition: Double = targetPosition

    fun up() {
        mode = Mode.UP
    }

    fun down() {
        mode = Mode.DOWN
    }
    fun tick(milliseconds: Double) {
        nonWigglePosition = nextDampedPosition(nonWigglePosition, targetPosition)
        servo.position = nonWigglePosition
        if (mode == Mode.DOWN) {
            servo.wiggle(nonWigglePosition, milliseconds)
        }

    }
    private fun Servo.wiggle(nonWigglePosition: Double, milliseconds: Double) {
        val wiggleUp = milliseconds % WIGGLE_PERIOD_MS < WIGGLE_PERIOD_MS / 2
        this.position = nonWigglePosition + when {
            wiggleUp -> WIGGLE_DISTANCE
            else -> -WIGGLE_DISTANCE
        }
    }
    private fun nextDampedPosition(position: Double, targetPosition: Double): Double {
        return position + (targetPosition - position) / DAMPING_BASE //Changes based on run speed because I'm lazy
    }
}