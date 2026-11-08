package org.firstinspires.ftc.teamcode.opmode

import com.pedropathing.algorithm.Foresight
import com.pedropathing.follower.Follower
import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.revhub.localizers.PinpointLocalizer
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystem.Brakes
import org.firstinspires.ftc.teamcode.subsystem.transfer.Deflowerer

@TeleOp
class Testing : OpMode() {
    val follower: Follower by lazy {
        with (Constants) {
            Follower(
                PinpointLocalizer(hardwareMap, pinpointConfig),
                Mecanum(hardwareMap, mecanumConfig),
                Foresight(foresightConfig)
            )
        }
    }

    val brakes: Brakes by lazy {
        Brakes(
            follower = follower,
            servoLeft = hardwareMap["brakesLeft"] as Servo,
            servoRight = hardwareMap["brakesRight"] as Servo,
        )
    }
    val deflowerer: Deflowerer by lazy {
        Deflowerer(
            servo = hardwareMap["deflowerer"] as Servo
        )
    }

    override fun init() {
        follower
        brakes
        deflowerer
    }

    override fun loop() {
        follower.manual(
            -(gamepad1.left_stick_y).toDouble(),
            (gamepad1.left_trigger - gamepad1.right_trigger).toDouble(),
            -(gamepad1.left_stick_x).toDouble()
        )
        brakes.tick()

        if (gamepad1.aWasPressed()) deflowerer.mode = !deflowerer.mode
        if (gamepad1.bWasPressed()) brakes.mode = !brakes.mode

        if (gamepad1.dpadUpWasPressed()) deflowerer.servo.position += 0.05
        if (gamepad1.dpadDownWasReleased()) deflowerer.servo.position -= 0.05

        telemetry.addData("Pose", follower.pose())
        telemetry.addData("Deflowerer Position", deflowerer.servo.position)
    }
}