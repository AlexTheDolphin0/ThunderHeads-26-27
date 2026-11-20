package org.firstinspires.ftc.teamcode.subsystem.transfer

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.hardware.Servo

@Configurable
class Intake(
    val servo: Servo
) {
    @Configurable
    companion object {
        @JvmField var MILLIS_BEFORE_UP = 250.0
    }
    @Configurable
    object Positions {
        @JvmField var UP = 0.0
        @JvmField var DOWN = 1.0
        @JvmField var FURTHER_DOWN = 1.0
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