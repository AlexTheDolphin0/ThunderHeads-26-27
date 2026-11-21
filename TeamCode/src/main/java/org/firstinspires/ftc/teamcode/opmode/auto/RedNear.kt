package org.firstinspires.ftc.teamcode.opmode.auto

import com.bylazar.configurables.annotations.Configurable
import com.pedropathing.api.Paths
import com.pedropathing.math.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import org.firstinspires.ftc.teamcode.subsystem.Event
import org.firstinspires.ftc.teamcode.subsystem.Hive

@Autonomous
class RedNear : AutoBase() {
    override val alliance = Alliance.RED
    override val startPose: Pose = Pose(33.0, 9.0, Math.toRadians(180.0))
    val gardenPickupPose: Pose = Pose(15.0, 9.0, Math.toRadians(180.0))
    override val targetHive: Hive = Hive.RED_AUDIENCE
    val parkPose = Pose(11.0, 93.0, Math.toRadians(-90.0))
    val maxPickupSpeed = 0.8
    override val eventList: List<Event> = listOf(
        spinUp(1800.0),
        shoot,
        spinDown,
        Event(
            "Park",
            8000.0,
            start = {
                follower.follow(
                    Paths.line(startPose, parkPose).linear(startPose, parkPose)
                )
            },
        ),
    )
}