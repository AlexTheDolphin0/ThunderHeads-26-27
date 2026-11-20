package org.firstinspires.ftc.teamcode.subsystem.shooter.turret

import com.bylazar.configurables.annotations.Configurable
import com.pedropathing.controllers.PIDController
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.PIDCoefficients
import kotlin.math.abs

class CRTurret(
    val servoLeft: CRServo,
    val servoMid: CRServo,
    val servoRight: CRServo,
    val encoder: DcMotorEx,
    ) : Turret() {
    companion object {
        val PID_COEFFICIENTS: PIDCoefficients = PIDCoefficients()
        val ENCODER_TO_ANGLE_RATIO: Double = 1 / 360.0
    }
    val pidController: PIDController = PIDController(PID_COEFFICIENTS.p, PID_COEFFICIENTS.i, PID_COEFFICIENTS.d)
    var power = 0.0
        set(power) {
            field = power
            servoLeft.power = power
            servoMid.power = power
            servoRight.power = power
        }
    override val isWithinTolerance: Boolean
        get() = abs(angleError) < ANGLE_TOLERANCE
    val angleError: Double
        get() = encoder.currentPosition / ENCODER_TO_ANGLE_RATIO - angle
    fun tick() {
        val pidOutput = pidController.calculate(angle, angleError)
        power = pidOutput
    }
}
