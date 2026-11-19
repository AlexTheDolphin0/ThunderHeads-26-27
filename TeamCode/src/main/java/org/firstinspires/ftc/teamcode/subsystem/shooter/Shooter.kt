package org.firstinspires.ftc.teamcode.subsystem.shooter

import com.bylazar.configurables.annotations.Configurable
import com.pedropathing.follower.Follower
import com.pedropathing.math.Vector2D
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import org.firstinspires.ftc.teamcode.subsystem.transfer.Gate
import org.firstinspires.ftc.teamcode.subsystem.shooter.turret.Turret
import kotlin.math.abs

@Configurable
class Shooter(
    val follower: Follower,
    val outtake: Outtake,
    val turret: Turret,
    val gate: Gate
) {
    enum class Mode {
        FULL_AUTO,
        AUTO_TARGET,
        MANUAL,
        OFF
    }
    companion object {
        fun launchSpeedFromDistanceSquared(distanceSquared: Double): Double {
            return distanceSquared * 10.0
        }
        fun outtakeVelocityFromVelocityVector(velocity: Vector2D): Double {
            val speed = velocity.magnitude()
            return 40.0 * speed
        }
        fun turretAngleFromVelocityVector(velocityVector: Vector2D): Double {
            val angleRadians = velocityVector.theta()
            return Math.toDegrees(angleRadians)
        }
        @JvmField var MINIMUM_ATTACK_ANGLE: Double = 15.0
        @JvmField var MAXIMUM_ATTACK_ANGLE: Double = 60.0
        @JvmField var ANGULAR_VELOCITY_TURRET_ANGLE_ADJUSTMENT_COEFFICIENT: Double = 2 * Math.PI / (0.115 * 6.0)
    }
    var mode: Mode = Mode.FULL_AUTO
        set(mode) {
            field = mode
            gate.close()
            when (mode) {
                Mode.FULL_AUTO -> {}
                Mode.AUTO_TARGET -> {}
                Mode.MANUAL -> {}
                Mode.OFF -> {
                    outtake.off()
                    turret.off()
                }
            }
        }
    val canReachHive: Boolean
        get() {
            val relativePosition = Alliance.targetHive.position - follower.pose().toVector2D()
            val attackAngle = AngleUnit.normalizeDegrees(Math.toDegrees(relativePosition.theta()) - Alliance.targetHive.orientation.outwardsAngle)
            return Math.toDegrees(abs(attackAngle)) in MINIMUM_ATTACK_ANGLE..MAXIMUM_ATTACK_ANGLE
        }
    val launchVelocityVector: Vector2D
        get() {
            val relativePosition = Alliance.targetHive.position - follower.pose().toVector2D()
            val speed = launchSpeedFromDistanceSquared(relativePosition.magnitudeSquared())
            val absoluteVelocity = relativePosition * (speed / relativePosition.magnitude())
            return absoluteVelocity - follower.velocity().toVector2D()
        }

    fun tick() {
        when (mode) {
            Mode.FULL_AUTO -> {
                target()
                when (canReachHive && outtake.isWithinTolerance && turret.isWithinTolerance) {
                    true -> gate.open()
                    false -> gate.close()
                }
            }
            Mode.AUTO_TARGET -> {
                target()
            }
            else -> {}
        }
    }

    private fun target() {
        turret.angle = turretAngleFromVelocityVector(launchVelocityVector) - ANGULAR_VELOCITY_TURRET_ANGLE_ADJUSTMENT_COEFFICIENT * follower.velocity().omega
        outtake.velocity = outtakeVelocityFromVelocityVector(launchVelocityVector)
    }
}