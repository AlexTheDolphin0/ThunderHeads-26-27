package org.firstinspires.ftc.teamcode.subsystem.transfer

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.hardware.Servo

class Intake(
    val servo: Servo
) {
    companion object {
        val WIGGLE_DISTANCE: Double = -0.05
        val WIGGLE_PERIOD_MS: Double = 200.0
        val MILLIS_BEFORE_UP = 250.0
    }
    object Positions {
        val UP = 0.0
        val DOWN = 0.55
        val DESCORER = 0.475
        val FURTHER_DOWN = 1.0
    }
    enum class Mode {
        UP,
        DOWN,
        DESCORER
    }
    var mode: Mode = Mode.UP
        set(mode) {
            field = mode
            millisSinceModeChange = 0.0
            servo.position = when (mode) {
                Mode.UP -> Positions.FURTHER_DOWN
                Mode.DOWN -> Positions.DOWN
                Mode.DESCORER -> Positions.DESCORER
            }
        }
    var millisSinceModeChange: Double = 0.0
    fun tick(deltaTimeMillis: Double, milliseconds: Double) {
        millisSinceModeChange += deltaTimeMillis
        if (mode == Mode.UP && millisSinceModeChange > MILLIS_BEFORE_UP)
            servo.position = Positions.UP
        if (mode == Mode.DESCORER) {
            servo.wiggle(Positions.DESCORER, milliseconds)
        }
    }

    private fun Servo.wiggle(nonWigglePosition: Double, milliseconds: Double) {
        val wiggleUp = milliseconds % WIGGLE_PERIOD_MS < WIGGLE_PERIOD_MS / 2
        this.position = nonWigglePosition + when {
            wiggleUp -> WIGGLE_DISTANCE
            else -> -WIGGLE_DISTANCE
        }
    }

    fun up() {
        mode = Mode.UP
    }

    fun down() {
        mode = Mode.DOWN
    }

    fun descorer() {
        mode = Mode.DESCORER
    }
}