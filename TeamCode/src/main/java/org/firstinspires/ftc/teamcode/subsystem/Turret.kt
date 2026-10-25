package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.abs

class Turret(
    val servoLeft: Servo,
    val servoMid: Servo,
    val servoRight: Servo,
) : Subsystem() {
    var angle: Double = 0.0
        set(value) {
            field = AngleUnit.normalizeDegrees(value)
            val position = value * POSITION_TO_ANGLE_RATIO + ZERO_POSITION
            servoLeft.position = position
            servoMid.position = position
            servoRight.position = position
        }

    companion object {
        const val ZERO_POSITION: Double = 0.5
        const val POSITION_TO_ANGLE_RATIO: Double = 1 / 360.0
        const val ANGLE_TOLERANCE: Double = 1.0
    }

    init {
        off()
    }

    val isWithinTolerance: Boolean
        get() = abs(servoLeft.position - angle) < ANGLE_TOLERANCE
                && abs(servoMid.position - angle) < ANGLE_TOLERANCE
                && abs(servoRight.position - angle) < ANGLE_TOLERANCE
    fun off() {
        angle = 0.0
    }
    override fun printState(telemetry: Telemetry) {
        TODO("Not yet implemented")
    }
}