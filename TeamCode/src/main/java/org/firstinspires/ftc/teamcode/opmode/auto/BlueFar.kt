package org.firstinspires.ftc.teamcode.opmode.auto

import com.pedropathing.api.Paths
import com.pedropathing.math.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import org.firstinspires.ftc.teamcode.subsystem.Event
import org.firstinspires.ftc.teamcode.subsystem.Hive

@Autonomous
class BlueFar : AutoBase() {
    override val alliance = Alliance.BLUE
    override val startPose: Pose = Pose(107.0, 9.0, Math.toRadians(90.0))
    override val targetHive: Hive = Hive.BLUE_AUDIENCE
    val parkPose = Pose(133.0, 26.0, Math.toRadians(180.0))
    override val eventList: List<Event> = listOf(
        sleep(8000.0),
        spinUp(1760.0),
        shoot,
        Event(
            "Park",
            8000.0,
            start = {
                follower.follow(
                    Paths.line(startPose, parkPose).linear(startPose, parkPose)
                )
            },
        ),
        spinDown,
    )
}