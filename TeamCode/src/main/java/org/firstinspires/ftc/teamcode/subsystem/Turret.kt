package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.follower.Follower
import com.pedropathing.math.Vector2D
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry

class Turret(
    val follower: Follower,
    val servoLeft: Servo,
    val servoMid: Servo,
    val servoRight: Servo,
    val outtakeMotorLeft: DcMotorEx,
    val outtakeMotorRight: DcMotorEx
) : Subsystem() {
    enum class Hive(val position: Vector2D) {
        RED_NEAR(Vector2D.cartesian(0.0, 0.0)),
        RED_FAR(Vector2D.cartesian(0.0, 0.0)),
        BLUE_NEAR(Vector2D.cartesian(0.0, 0.0)),
        BLUE_FAR(Vector2D.cartesian(0.0, 0.0))
    }
    enum class Mode {
        FULL_AUTO,
        AUTO_TARGET,
        MANUAL,
        OFF
    }
    companion object {


        fun speedFromDistanceSquared(distance: Double) : Double {
            TODO("Not yet implemented")
        }
        fun outtakeVelocityFromVelocity(velocity: Vector2D) : Double {
            val speedSquared = velocity.magnitudeSquared()
            TODO("Not yet implemented")
        }
        fun servoPositionFromAngle(velocity: Vector2D): Double {
            val angle = velocity.theta()
            TODO("Not yet implemented")
        }
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

    init {
        outtakeMotorLeft.mode = DcMotor.RunMode.RUN_USING_ENCODER
        outtakeMotorRight.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun tick() {
        when (mode) {
            Mode.FULL_AUTO -> {
                val targetHive = getTargetHive()
                if (canReach(targetHive)) {
                    val launchVelocity = calcLaunchVelocity(targetHive)
                    val servoPosition = servoPositionFromAngle(launchVelocity)
                    val outtakeVelocity = outtakeVelocityFromVelocity(launchVelocity)
                    servoLeft.position = servoPosition
                    servoMid.position = servoPosition
                    servoRight.position = servoPosition
                    outtakeMotorLeft.velocity = outtakeVelocity
                    outtakeMotorRight.velocity = outtakeVelocity
                }
            }
            Mode.AUTO_TARGET -> TODO()
            Mode.MANUAL -> TODO()
            Mode.OFF -> TODO()
        }
    }

    fun calcLaunchVelocity(hive: Hive) : Vector2D {
        val relativePosition = hive.position - follower.pose().toVector2D()
        val speed = speedFromDistanceSquared(relativePosition.magnitudeSquared())
        val absoluteVelocity = relativePosition * (speed / relativePosition.magnitude())
        return absoluteVelocity - follower.velocity().toVector2D()
    }

    fun canReach(hive: Hive) : Boolean {
        TODO("Not yet implemented")
        // May need to modify structure of calcLaunchVelocity to optimize and perform this in a single step
    }

    fun getTargetHive() : Hive {
        TODO("Not yet implemented")
        // Will likely be moved to Limelight file
    }

    override fun printState(telemetry: Telemetry) {
        TODO("Not yet implemented")
    }
}