package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.math.Pose

object Poses {
    var currentPose: Pose = Pose(0.0, 0.0, 0.0)
    object Start {
        val RED_NEAR: Pose = Pose(33.0, 9.0, Math.toRadians(180.0))
        val RED_FAR: Pose = Pose(34.0, 132.0, Math.toRadians(-90.0))
        val BLUE_NEAR: Pose = Pose(108.0, 132.0, Math.toRadians(0.0))
        val BLUE_FAR: Pose = Pose(107.0, 9.0, Math.toRadians(90.0))
    }
}