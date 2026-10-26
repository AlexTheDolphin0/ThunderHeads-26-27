package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.math.Vector2D

enum class Alliance {
    RED {
        override var orientation: HiveOrientation = HiveOrientation.OPPOSITE
    },
    BLUE {
        override var orientation: HiveOrientation = HiveOrientation.AUDIENCE
    };
    companion object {
        var alliance: Alliance = RED
        val targetHive: Hive
            get() = Hive[alliance, alliance.orientation]
    }
    abstract var orientation: HiveOrientation
    operator fun not(): Alliance = when (this) {
        RED -> BLUE
        BLUE -> RED
    }
}

enum class HiveOrientation {
    AUDIENCE,
    OPPOSITE;
    operator fun not(): HiveOrientation = when (this) {
        AUDIENCE -> OPPOSITE
        OPPOSITE -> AUDIENCE
    }
}
enum class Hive(
    val alliance: Alliance,
    val orientation: HiveOrientation,
    val position: Vector2D,
    val outwardsAngle: Double,
    val aprilTagIds: IntArray
) {
    RED_AUDIENCE(
        alliance = Alliance.RED,
        orientation = HiveOrientation.AUDIENCE,
        position = Vector2D.cartesian(0.0, 0.0),
        outwardsAngle = 0.0,
        aprilTagIds = intArrayOf(34, 35, 36, 37)
    ),
    RED_OPPOSITE(
        alliance = Alliance.RED,
        orientation = HiveOrientation.OPPOSITE,
        position = Vector2D.cartesian(0.0, 0.0),
        outwardsAngle = 0.0,
        aprilTagIds = intArrayOf(30, 31, 32, 33)
    ),
    BLUE_AUDIENCE(
        alliance = Alliance.BLUE,
        orientation = HiveOrientation.AUDIENCE,
        position = Vector2D.cartesian(0.0, 0.0),
        outwardsAngle = 0.0,
        aprilTagIds = intArrayOf(42, 43, 44, 45)
    ),
    BLUE_OPPOSITE(
        alliance = Alliance.BLUE,
        orientation = HiveOrientation.OPPOSITE,
        position = Vector2D.cartesian(0.0, 0.0),
        outwardsAngle = 0.0,
        aprilTagIds = intArrayOf(38, 39, 40, 41)
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