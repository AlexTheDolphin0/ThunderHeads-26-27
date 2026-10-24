package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.follower.Follower
import com.pedropathing.math.Vector2D
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.abs

class Turret(
    val follower: Follower,
    val servoLeft: Servo,
    val servoMid: Servo,
    val servoRight: Servo,
    val outtakeMotorLeft: DcMotorEx,
    val outtakeMotorRight: DcMotorEx
) : Subsystem() {
    enum class Mode {
        FULL_AUTO,
        AUTO_TARGET,
        MANUAL,
        OFF
    }
    var mode: Mode = Mode.FULL_AUTO
        set(value) {
            field = value
            when (field) {
                Mode.FULL_AUTO -> {}
                Mode.AUTO_TARGET -> {}
                Mode.MANUAL -> {}
                Mode.OFF -> {
                    servoPosition = 0.5
                    outtakeVelocity = 0.0
                }
            }
        }
    var servoPosition: Double = 0.5
        set(value) {
            field = Math.clamp(value, 0.0, 1.0)
            servoLeft.position = value
            servoMid.position = value
            servoRight.position = value
        }
    var outtakeVelocity: Double = 0.0
        set(value) {
            field = value
            outtakeMotorLeft.velocity = value
            outtakeMotorRight.velocity = value
        }

    companion object {
        fun launchSpeedFromDistanceSquared(distance: Double): Double {
            TODO("Not yet implemented")
        }

        fun outtakeVelocityFromVelocity(velocity: Vector2D): Double {
            val speedSquared = velocity.magnitudeSquared()
            TODO("Not yet implemented")
        }

        fun servoPositionFromAngle(velocity: Vector2D): Double {
            val angle = velocity.theta()
            TODO("Not yet implemented")
        }

        const val MAXIMUM_ATTACK_ANGLE: Double = 60.0
        const val SERVO_POSITION_TOLERANCE: Double = 0.05
        const val OUTTAKE_VELOCITY_TOLERANCE: Double = 2.5
    }

    init {
        outtakeMotorLeft.mode = DcMotor.RunMode.RUN_USING_ENCODER
        outtakeMotorRight.mode = DcMotor.RunMode.RUN_USING_ENCODER
        servoPosition = servoPosition //The setter for each of these automatically updates hardware. Hopefully this design pattern makes sense.
        outtakeVelocity = outtakeVelocity
    }

    val readyToShoot : Boolean
        get() = canReachHive && isWithinShootingTolerance
    private val launchVelocity: Vector2D
        get() {
            val relativePosition = Alliance.targetHive.position - follower.pose().toVector2D()
            val speed = launchSpeedFromDistanceSquared(relativePosition.magnitudeSquared())
            val absoluteVelocity = relativePosition * (speed / relativePosition.magnitude())
            return absoluteVelocity - follower.velocity().toVector2D()
        }
    private val canReachHive: Boolean
        get() {
            val relativePosition = Alliance.targetHive.position - follower.pose().toVector2D()
            val relativeAngle = AngleUnit.normalizeDegrees(Math.toDegrees(relativePosition.theta()) - Alliance.targetHive.outwardsAngle)
            return abs(relativeAngle) < Math.toRadians(MAXIMUM_ATTACK_ANGLE)
        }
    private val isWithinShootingTolerance : Boolean
        get() {
            val servosWithinTolerance = (
                    abs(servoLeft.position - servoPosition) < SERVO_POSITION_TOLERANCE
                            && abs(servoMid.position - servoPosition) < SERVO_POSITION_TOLERANCE
                            && abs(servoMid.position - servoPosition) < SERVO_POSITION_TOLERANCE )
            val motorsWithinTolerance = (
                    abs(outtakeMotorLeft.velocity - outtakeVelocity) < OUTTAKE_VELOCITY_TOLERANCE
                            && abs(outtakeMotorRight.velocity - outtakeVelocity) < OUTTAKE_VELOCITY_TOLERANCE )
            return servosWithinTolerance && motorsWithinTolerance
        }

    fun tick() {
        when (mode) {
            Mode.FULL_AUTO, Mode.AUTO_TARGET -> {
                if (canReachHive) {
                    servoPosition = servoPositionFromAngle(launchVelocity)
                    val outtakeVelocity = outtakeVelocityFromVelocity(launchVelocity)
                    outtakeMotorLeft.velocity = outtakeVelocity
                    outtakeMotorRight.velocity = outtakeVelocity
                }
            }
            else -> {}
        }
    }

    override fun printState(telemetry: Telemetry) {
        TODO("Not yet implemented")
    }
}