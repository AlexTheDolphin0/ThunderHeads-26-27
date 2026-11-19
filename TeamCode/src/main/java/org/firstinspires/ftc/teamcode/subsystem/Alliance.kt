package org.firstinspires.ftc.teamcode.subsystem

import com.bylazar.configurables.annotations.Configurable
import com.pedropathing.math.Vector2D

enum class Alliance(
    var orientation: HiveOrientation
) {
    RED(HiveOrientation.OPPOSITE),
    BLUE(HiveOrientation.AUDIENCE);
    companion object {
        var alliance: Alliance = RED
        val targetHive: Hive
            get() = Hive[alliance, alliance.orientation]
    }
    operator fun not(): Alliance = when (this) {
        RED -> BLUE
        BLUE -> RED
    }
}

enum class HiveOrientation(val outwardsAngle: Double) {
    AUDIENCE(
        outwardsAngle = -90.0
    ),
    OPPOSITE(
        outwardsAngle = 90.0
    );
    operator fun not(): HiveOrientation = when (this) {
        AUDIENCE -> OPPOSITE
        OPPOSITE -> AUDIENCE
    }
}
@Configurable
enum class Hive(
    val alliance: Alliance,
    val orientation: HiveOrientation,
    val aprilTagIds: IntArray,
    val position: Vector2D
) {
    RED_AUDIENCE(
        alliance = Alliance.RED,
        orientation = HiveOrientation.AUDIENCE,
        aprilTagIds = intArrayOf(34, 35, 36, 37),
        position = Vector2D.cartesian(58.0, 55.0)
    ),
    RED_OPPOSITE(
        alliance = Alliance.RED,
        orientation = HiveOrientation.OPPOSITE,
        aprilTagIds = intArrayOf(30, 31, 32, 33),
        position = Vector2D.cartesian(58.0, 87.0)
    ),
    BLUE_AUDIENCE(
        alliance = Alliance.BLUE,
        orientation = HiveOrientation.AUDIENCE,
        aprilTagIds = intArrayOf(42, 43, 44, 45),
        position = Vector2D.cartesian(84.5, 55.0)
    ),
    BLUE_OPPOSITE(
        alliance = Alliance.BLUE,
        orientation = HiveOrientation.OPPOSITE,
        aprilTagIds = intArrayOf(38, 39, 40, 41),
        position = Vector2D.cartesian(84.5, 87.0)
    );
    companion object {
        operator fun get(alliance: Alliance, orientation: HiveOrientation): Hive = when (alliance) {
            Alliance.RED -> {
                when (orientation) {
                    HiveOrientation.AUDIENCE -> RED_AUDIENCE
                    HiveOrientation.OPPOSITE -> RED_OPPOSITE
                }
            }
            Alliance.BLUE ->
                when (orientation) {
                    HiveOrientation.AUDIENCE -> BLUE_AUDIENCE
                    HiveOrientation.OPPOSITE -> BLUE_OPPOSITE
                }
        }
        operator fun get(aprilTagId: Int): Hive? {
            for (hive in Hive.entries) {
                if (aprilTagId in hive.aprilTagIds) return hive
            }
            return null
        }
    }
}