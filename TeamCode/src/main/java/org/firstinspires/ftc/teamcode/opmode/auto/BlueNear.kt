package org.firstinspires.ftc.teamcode.opmode.auto

import com.pedropathing.api.Paths
import com.pedropathing.math.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import org.firstinspires.ftc.teamcode.subsystem.Event

@Autonomous
class BlueNear : AutoBase() {
    override val alliance = Alliance.BLUE
    override val startPose: Pose = Pose(108.0, 132.0, Math.toRadians(0.0))
    val parkPose = Pose(133.0, 51.0, Math.toRadians(90.0))
    override val eventList: List<Event> = listOf(
        spinUp,
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