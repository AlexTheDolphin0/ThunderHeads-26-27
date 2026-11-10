package org.firstinspires.ftc.teamcode.opmode

import com.pedropathing.algorithm.Foresight
import com.pedropathing.follower.Follower
import com.pedropathing.math.Pose
import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.revhub.localizers.PinpointLocalizer
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystem.Brakes
import org.firstinspires.ftc.teamcode.subsystem.transfer.Deflowerer

@TeleOp
class Drive : OpMode() {
    val follower: Follower by lazy {
        with (Constants) {
            Follower(
                PinpointLocalizer(hardwareMap, pinpointConfig),
                Mecanum(hardwareMap, mecanumConfig),
                Foresight(foresightConfig)
            )
        }
    }

    override fun init() {
        follower.setPose(Pose(0.0, 0.0, 0.0))
    }

    override fun loop() {
        follower.manual(
            -(gamepad1.left_stick_y).toDouble(),
            (gamepad1.left_trigger - gamepad1.right_trigger).toDouble(),
            -(gamepad1.left_stick_x).toDouble()
        )
        follower.update()
        telemetry.addData("X", follower.pose().x().toString() + " Inches")
        telemetry.addData("Y", follower.pose().y().toString() + " Inches")
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()).toString() + " Degrees")
    }
}