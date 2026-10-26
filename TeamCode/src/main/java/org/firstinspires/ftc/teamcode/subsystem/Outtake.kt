package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.abs

class Outtake(
    val motorLeft: DcMotorEx,
    val motorRight: DcMotorEx
) : Subsystem() {
    var velocity: Double = 0.0
        set(velocity) {
            field = velocity
            motorLeft.velocity = velocity
            motorRight.velocity = velocity
        }
    init {
        motorLeft.mode = DcMotor.RunMode.RUN_USING_ENCODER
        motorRight.mode = DcMotor.RunMode.RUN_USING_ENCODER
        off()
    }
    companion object {
        const val VELOCITY_TOLERANCE: Double = 2.5
    }
    val isWithinTolerance: Boolean
        get() = abs(motorLeft.velocity - velocity) < VELOCITY_TOLERANCE
                && abs(motorRight.velocity - velocity) < VELOCITY_TOLERANCE
    fun off() {
        velocity = 0.0
    }
    override fun printState(telemetry: Telemetry) {
        TODO("Not yet implemented")
    }
}