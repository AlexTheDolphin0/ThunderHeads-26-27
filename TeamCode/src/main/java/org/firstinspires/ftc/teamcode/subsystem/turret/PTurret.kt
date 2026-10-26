package org.firstinspires.ftc.teamcode.subsystem.turret

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.abs

class PTurret(
    val servoLeft: Servo,
    val servoMid: Servo,
    val servoRight: Servo,
) : Turret() {
    companion object {
        const val ZERO_POSITION: Double = 0.5
        const val POSITION_TO_ANGLE_RATIO: Double = 1 / 360.0
    }
    override var angle: Double
        get() = super.angle
        set(angle) {
            super.angle = angle
            val position = super.angle * POSITION_TO_ANGLE_RATIO + ZERO_POSITION
            servoLeft.position = position
            servoMid.position = position
            servoRight.position = position
        }
    override val isWithinTolerance: Boolean
        get() = abs(servoLeft.position - angle) < ANGLE_TOLERANCE
                && abs(servoMid.position - angle) < ANGLE_TOLERANCE
                && abs(servoRight.position - angle) < ANGLE_TOLERANCE

    override fun printState(telemetry: Telemetry) {
        TODO("Not yet implemented")
    }
}