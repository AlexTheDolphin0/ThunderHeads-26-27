package org.firstinspires.ftc.teamcode.subsystem.transfer

import com.qualcomm.robotcore.hardware.Servo

class Deflowerer(
    val intakeServo: Servo,
    val plateServo: Servo
) {
    enum class Mode(val intakePosition: Double, val platePosition: Double) {
        UP(
            intakePosition = 0.0,
            platePosition = 0.83,
        ),
        ROLLER(
            intakePosition = 1.0,
            platePosition = UP.platePosition,
        ),
        DOWN(
            intakePosition = ROLLER.intakePosition,
            platePosition = 1.0,
        ),
        WIGGLE(
            intakePosition = DOWN.intakePosition,
            platePosition = DOWN.platePosition
        );
    }

    companion object {
        const val WIGGLE_DISTANCE: Double = 0.025
        const val WIGGLE_PERIOD_MS: Double = 200.0
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

    fun wiggle() {
        mode = Mode.WIGGLE
    }

    fun tick(milliseconds: Double) {
        if (mode == Mode.WIGGLE) {
            val wiggleUp = milliseconds % WIGGLE_PERIOD_MS < WIGGLE_PERIOD_MS / 2
            plateServo.position = Mode.WIGGLE.platePosition + when {
                wiggleUp -> WIGGLE_DISTANCE
                else -> -WIGGLE_DISTANCE
            }
        }
    }
}