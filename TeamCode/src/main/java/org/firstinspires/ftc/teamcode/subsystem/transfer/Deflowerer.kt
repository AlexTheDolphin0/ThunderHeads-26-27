package org.firstinspires.ftc.teamcode.subsystem.transfer

import com.qualcomm.robotcore.hardware.Servo

class Deflowerer(
    val servo: Servo
) {
    enum class Mode(val position: Double) {
        ON(
            position = 0.0,
        ),
        OFF(
            position = 1.0,
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
            servo.position = mode.position
        }

    fun on() {
        mode = Mode.ON
    }

    fun off() {
        mode = Mode.OFF
    }
}