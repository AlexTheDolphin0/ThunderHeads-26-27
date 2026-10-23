package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.follower.Follower
import com.pedropathing.math.Vector2D
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.Subsystems.Limelight
import kotlin.math.abs

class Turret(
    val follower: Follower,
    val limelight: Limelight,
    val servoLeft: Servo,
    val servoMid: Servo,
    val servoRight: Servo,
    val outtakeMotorLeft: DcMotorEx,
    val outtakeMotorRight: DcMotorEx
) : Subsystem() {
    enum class Hive(val position: Vector2D, val outwardsAngle: Double) {
        RED_AUDIENCE(
            position = Vector2D.cartesian(0.0, 0.0),
            outwardsAngle = 0.0
        ),
        RED_OPPOSITE(
            position = Vector2D.cartesian(0.0, 0.0),
            outwardsAngle = 0.0
        ),
        BLUE_AUDIENCE(
            position = Vector2D.cartesian(0.0, 0.0),
            outwardsAngle = 0.0
        ),
        BLUE_OPPOSITE(
            position = Vector2D.cartesian(0.0, 0.0),
            outwardsAngle = 0.0
        );
    }
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
                Mode.FULL_AUTO -> TODO()
                Mode.AUTO_TARGET -> TODO()
                Mode.MANUAL -> TODO()
                Mode.OFF -> TODO()
            }
        }
    var servoPosition: Double = 0.5
        set(value) {
            field = value
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
    val targetHive: Hive
        get() {
            return when (Alliance.alliance) {
                Alliance.RED -> {
                    when (limelight.redOrientation) {
                        Limelight.HiveOrientation.Audience -> Hive.RED_AUDIENCE
                        Limelight.HiveOrientation.Opposite -> Hive.RED_OPPOSITE
                    }
                }

                Alliance.BLUE -> {
                    when (limelight.blueOrientation) {
                        Limelight.HiveOrientation.Audience -> Hive.BLUE_AUDIENCE
                        Limelight.HiveOrientation.Opposite -> Hive.BLUE_OPPOSITE
                    }
                }
            }
        }

    fun tick() {
        when (mode) {
            Mode.FULL_AUTO -> {
                if (canReachHive()) {
                    val launchVelocity = calcLaunchVelocity()
                    servoPosition = servoPositionFromAngle(launchVelocity)
                    val outtakeVelocity = outtakeVelocityFromVelocity(launchVelocity)
                    outtakeMotorLeft.velocity = outtakeVelocity
                    outtakeMotorRight.velocity = outtakeVelocity
                }
            }
            Mode.AUTO_TARGET -> TODO()
            Mode.MANUAL -> TODO()
            Mode.OFF -> TODO()
        }
    }

    private fun calcLaunchVelocity() : Vector2D {
        val relativePosition = targetHive.position - follower.pose().toVector2D()
        val speed = launchSpeedFromDistanceSquared(relativePosition.magnitudeSquared())
        val absoluteVelocity = relativePosition * (speed / relativePosition.magnitude())
        return absoluteVelocity - follower.velocity().toVector2D()
    }
    fun canReachHive() : Boolean {
        val relativePosition = targetHive.position - follower.pose().toVector2D()
        val relativeAngle = AngleUnit.normalizeDegrees(Math.toDegrees(relativePosition.theta()) - targetHive.outwardsAngle)
        return abs(relativeAngle) < Math.toRadians(MAXIMUM_ATTACK_ANGLE)
    }
    fun isWithinShootingTolerance() : Boolean {
        val servosWithinTolerance = (
            abs(servoLeft.position - servoPosition) < SERVO_POSITION_TOLERANCE
            && abs(servoMid.position - servoPosition) < SERVO_POSITION_TOLERANCE
            && abs(servoMid.position - servoPosition) < SERVO_POSITION_TOLERANCE )
        val motorsWithinTolerance = (
            abs(outtakeMotorLeft.velocity - outtakeVelocity) < OUTTAKE_VELOCITY_TOLERANCE
            && abs(outtakeMotorRight.velocity - outtakeVelocity) < OUTTAKE_VELOCITY_TOLERANCE )
        return servosWithinTolerance && motorsWithinTolerance
    }

    override fun printState(telemetry: Telemetry) {
        TODO("Not yet implemented")
    }
}