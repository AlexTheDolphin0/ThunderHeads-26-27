package org.firstinspires.ftc.teamcode.subsystem.shooter.turret

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.abs

class PTurret(
    val servoLeft: Servo,
    val servoMid: Servo,
    val servoRight: Servo,
) : Turret() {
    companion object {
        val ZERO_POSITION: Double = 0.5
        val POSITION_TO_ANGLE_RATIO: Double = (1.0 / 500.0) * (80.0 / 40.0) * (44.0 / 71.0)
    }
    override var angle: Double
        get() = super.angle
        set(angle) {
            super.angle = AngleUnit.normalizeDegrees(angle)
            val position = super.angle * POSITION_TO_ANGLE_RATIO + ZERO_POSITION
            servoLeft.position = position
            servoMid.position = position
            servoRight.position = position
        }
    override val isWithinTolerance: Boolean
        get() = abs(servoLeft.position - angle) < ANGLE_TOLERANCE
                && abs(servoMid.position - angle) < ANGLE_TOLERANCE
                && abs(servoRight.position - angle) < ANGLE_TOLERANCE
}