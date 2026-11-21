package org.firstinspires.ftc.teamcode.subsystem.transfer

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.hardware.Servo

class Intake(
    val servo: Servo
) {
    companion object {
        val MILLIS_BEFORE_UP = 250.0
    }
    object Positions {
        val UP = 0.0
        val DOWN = 0.45
        val FURTHER_DOWN = 1.0
    }
    enum class Mode {
        UP,
        DOWN
    }
    var mode: Mode = Mode.UP
        set(mode) {
            field = mode
            millisSinceModeChange = 0.0
            servo.position = when (mode) {
                Mode.UP -> Positions.FURTHER_DOWN
                Mode.DOWN -> Positions.DOWN
            }
        }
    var millisSinceModeChange: Double = 0.0
    fun tick(deltaTimeMillis: Double) {
        millisSinceModeChange += deltaTimeMillis
        if (mode == Mode.UP && millisSinceModeChange > MILLIS_BEFORE_UP)
            servo.position = Positions.UP
    }
    fun up() {
        mode = Mode.UP
    }

    fun down() {
        mode = Mode.DOWN
    }
}