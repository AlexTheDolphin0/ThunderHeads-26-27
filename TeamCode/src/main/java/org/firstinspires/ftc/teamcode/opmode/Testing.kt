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
            intakeServo = hardwareMap["intakeServo"] as Servo,
            plateServo = hardwareMap["plateServo"] as Servo,
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

        if (gamepad1.aWasPressed()) deflowerer.mode = when (deflowerer.mode) {
            Deflowerer.Mode.UP -> Deflowerer.Mode.ROLLER
            Deflowerer.Mode.DOWN -> Deflowerer.Mode.DOWN
            Deflowerer.Mode.ROLLER -> Deflowerer.Mode.UP
        }
        if (gamepad1.bWasPressed()) brakes.mode = !brakes.mode

        if (gamepad1.a) {
            if (gamepad1.dpadUpWasPressed()) deflowerer.intakeServo.position += 0.05
            if (gamepad1.dpadDownWasReleased()) deflowerer.intakeServo.position -= 0.05
        }
        if (gamepad1.b) {
            if (gamepad1.dpadUpWasPressed()) deflowerer.plateServo.position += 0.05
            if (gamepad1.dpadDownWasReleased()) deflowerer.plateServo.position -= 0.05
        }
        if (gamepad1.x) {
            if (gamepad1.dpadUpWasPressed()) brakes.servoLeft.position += 0.05
            if (gamepad1.dpadDownWasReleased()) brakes.servoLeft.position -= 0.05
        }
        if (gamepad1.y) {
            if (gamepad1.dpadUpWasPressed()) brakes.servoRight.position += 0.05
            if (gamepad1.dpadDownWasReleased()) brakes.servoRight.position -= 0.05
        }
        telemetry.addLine("A: intake, B: plate, X: brakes left, Y: brakes right")
        telemetry.addData("Pose", follower.pose())
        telemetry.addData("Intake Position", deflowerer.intakeServo.position)
        telemetry.addData("Plate Position", deflowerer.plateServo.position)
        telemetry.addData("Brakes Left Position", brakes.servoLeft.position)
        telemetry.addData("Brakes Right Position", brakes.servoRight.position)
    }
}