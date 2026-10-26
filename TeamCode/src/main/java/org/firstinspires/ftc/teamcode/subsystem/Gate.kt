package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry

class Gate(
    val servo: Servo
) : Subsystem() {
    enum class Mode(val servoPosition: Double) {
        OPEN(0.0),
        CLOSED(1.0);
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
        close()
    }
    fun open() {
        mode = Mode.OPEN
    }

    fun close() {
        mode = Mode.CLOSED
    }

    override fun printState(telemetry: Telemetry) {
        TODO("Not yet implemented")
    }
}