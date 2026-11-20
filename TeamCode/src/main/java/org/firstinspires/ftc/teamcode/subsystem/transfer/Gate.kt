package org.firstinspires.ftc.teamcode.subsystem.transfer

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry

class Gate(
    val servo: Servo
) {
    enum class Mode(val servoPosition: Double) {
        OPEN(0.32),
        CLOSED(0.20);
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