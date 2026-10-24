package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry

class Gate(
    val servo: Servo
) : Subsystem() {
    enum class Mode(val servoPosition: Double) {
        OPEN(0.0),
        CLOSED(1.0)
    }
    var mode: Mode = Mode.CLOSED
        set(value) {
            field = value
            servo.position = value.servoPosition
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