package org.firstinspires.ftc.teamcode.opmode.auto

import com.pedropathing.api.Paths
import com.pedropathing.math.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import org.firstinspires.ftc.teamcode.subsystem.Event
import org.firstinspires.ftc.teamcode.subsystem.Hive
import org.firstinspires.ftc.teamcode.subsystem.HiveOrientation

@Autonomous
class RedFar : AutoBase() {
    override val alliance = Alliance.RED
    override val startPose: Pose = Pose(34.0, 132.0, Math.toRadians(-90.0))
    override val targetHive: Hive = Hive.RED_OPPOSITE
    val parkPose = Pose(11.0, 118.0, Math.toRadians(0.0))
    override val eventList: List<Event> = listOf(
        sleep(8000.0),
        spinUp(1780.0),
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
        )
    )
}