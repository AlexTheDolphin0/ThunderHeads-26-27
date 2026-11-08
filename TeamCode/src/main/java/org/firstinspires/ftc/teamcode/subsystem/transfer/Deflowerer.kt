package org.firstinspires.ftc.teamcode.subsystem.transfer

import com.qualcomm.robotcore.hardware.Servo

class Deflowerer(
    val intakeServo: Servo,
    val plateServo: Servo
) {
    enum class Mode(val intakePosition: Double, val platePosition: Double) {
        UP(
            intakePosition = 0.0,
            platePosition = 0.0,
        ),
        DOWN(
            intakePosition = 1.0,
            platePosition = 1.0,
        ),
        ROLLER(
            intakePosition = 1.0,
            platePosition = 0.0,
        );
    }

    init {
        up()
    }

    var mode: Mode = Mode.UP
        set(mode) {
            field = mode
            intakeServo.position = mode.intakePosition;
            plateServo.position = mode.platePosition
        }

    fun up() {
        mode = Mode.UP
    }

    fun down() {
        mode = Mode.DOWN
    }

    fun roller() {
        mode = Mode.ROLLER
    }
}