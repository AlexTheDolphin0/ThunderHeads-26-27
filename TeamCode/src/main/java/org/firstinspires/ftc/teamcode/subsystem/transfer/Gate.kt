package org.firstinspires.ftc.teamcode.subsystem.transfer

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry

@Configurable
class Gate(
    val servo: Servo
) {
    @Configurable
    object Positions {
        @JvmField var OPEN = 0.32
        @JvmField var CLOSED = 0.20
    }
    enum class Mode(val servoPosition: Double) {
        OPEN(Positions.OPEN),
        CLOSED(Positions.CLOSED);
        operator fun not() = when (this) {
            OPEN -> CLOSED
            CLOSED -> OPEN
        }
    }
    var mode: Mode = Mode.CLOSED
        set(mode) {
            field = mode
            servo.position = mode.servoPosition
        }
    init {
        //close()
    }
    fun open() {
        mode = Mode.OPEN
    }

    fun close() {
        mode = Mode.CLOSED
    }
}