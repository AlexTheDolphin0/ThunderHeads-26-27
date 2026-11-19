package org.firstinspires.ftc.teamcode.opmode.auto

import com.pedropathing.math.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import org.firstinspires.ftc.teamcode.subsystem.Event

@Autonomous
class BlueFar : AutoBase() {
    override val alliance = Alliance.BLUE
    override val startPose: Pose = Pose(107.0, 9.0, Math.toRadians(90.0))
    override val eventList: List<Event> = listOf()
}