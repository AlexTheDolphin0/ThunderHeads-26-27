package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry

class Deflowerer(
    val servoLeft: Servo,
    val servoRight: Servo
) : Subsystem() {
    enum class Mode(val leftPosition: Double, val rightPosition: Double) {
        ON(
            leftPosition = 0.0,
            rightPosition = 1.0,
        ),
        OFF(
            leftPosition = 1.0,
            rightPosition = 0.0,
        );
        operator fun not() = when (this) {
            ON -> OFF
            OFF -> ON
        }
    }

    init {
        off()
    }

    var mode: Mode = Mode.OFF
        set(mode) {
            field = mode
            servoLeft.position = mode.leftPosition
            servoRight.position = mode.rightPosition
        }

    fun on() {
        mode = Mode.ON
    }

    fun off() {
        mode = Mode.OFF
    }

    override fun printState(telemetry: Telemetry) {
        TODO("Not yet implemented")
    }
}