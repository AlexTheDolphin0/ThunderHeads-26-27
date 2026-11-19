package org.firstinspires.ftc.teamcode.opmode.auto

import com.bylazar.configurables.annotations.Configurable
import com.pedropathing.api.Paths
import com.pedropathing.math.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import org.firstinspires.ftc.teamcode.subsystem.Event

@Autonomous
class RedNear : AutoBase() {
    @Configurable
    companion object {
        @JvmField var GARDEN_PICKUP_POSE = Pose(15.0, 9.0, Math.toRadians(180.0))
        @JvmField var MAX_PICKUP_SPEED = 0.8
    }
    override val alliance = Alliance.RED
    override val startPose: Pose = Pose(33.0, 9.0, Math.toRadians(180.0))
    val gardenPickupPose: Pose = GARDEN_PICKUP_POSE
    override val eventList: List<Event> = listOf(
        spinUp,
        shoot,
        Event(
            "Garden Pickup",
            4000.0,
            {
                intake.down()
                transfer.on()
                follower.follow(
                    Paths.line(startPose, gardenPickupPose).linear(startPose, gardenPickupPose).with(
                        Constants.foresightConfig.maxPathSpeed.at(MAX_PICKUP_SPEED)
                    )
                )
            }
        ),
        Event(
            "Return To Start",
            4000.0,
            start = {
                follower.follow(
                    Paths.line(gardenPickupPose, startPose).linear(startPose, gardenPickupPose)
                )
                transfer.on()
            },
            end = {
                intake.up()
                transfer.off()
            },
        ),
        shoot,
        spinDown
    )
}