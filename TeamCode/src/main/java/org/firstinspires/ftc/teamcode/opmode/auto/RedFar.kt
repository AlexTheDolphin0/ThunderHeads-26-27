package org.firstinspires.ftc.teamcode.opmode.auto

import com.pedropathing.math.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import org.firstinspires.ftc.teamcode.subsystem.Event

@Autonomous
class RedFar : AutoBase() {
    override val alliance = Alliance.RED
    override val startPose: Pose = Pose(34.0, 132.0, Math.toRadians(-90.0))
    override val eventList: List<Event> = listOf()
}