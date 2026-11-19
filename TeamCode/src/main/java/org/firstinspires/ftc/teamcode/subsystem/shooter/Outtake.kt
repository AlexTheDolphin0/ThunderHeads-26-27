package org.firstinspires.ftc.teamcode.subsystem.shooter

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.abs

@Configurable
class Outtake(
    val motorLeft: DcMotorEx,
    val motorRight: DcMotorEx
) {
    companion object {
        @JvmField var VELOCITY_TOLERANCE: Double = 2.5
    }
    @Configurable
    object PidfCoefficients {
        @JvmField var LEFT: PIDFCoefficients = PIDFCoefficients(200.0, 10.0, 10.0, 10.0)
        @JvmField var RIGHT: PIDFCoefficients = PIDFCoefficients(200.0, 10.0, 10.0, 10.0)
    }
    var velocity: Double = 0.0
        set(velocity) {
            field = velocity
            motorLeft.velocity = velocity
            motorRight.velocity = velocity
        }
    init {
        motorLeft.mode = DcMotor.RunMode.RUN_USING_ENCODER
        motorRight.mode = DcMotor.RunMode.RUN_USING_ENCODER
        motorLeft.direction = DcMotorSimple.Direction.REVERSE
        motorRight.direction = DcMotorSimple.Direction.FORWARD
        motorLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PidfCoefficients.LEFT)
        motorRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PidfCoefficients.RIGHT)
        off()
    }
    var pidfCoefficients = motorLeft.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER)
        set(pidfCoefficients) {
            field = pidfCoefficients
            motorLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients)
            motorRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients)
        }
    val isWithinTolerance: Boolean
        get() = abs(motorLeft.velocity - velocity) < VELOCITY_TOLERANCE
                && abs(motorRight.velocity - velocity) < VELOCITY_TOLERANCE
    fun off() {
        velocity = 0.0
    }
}