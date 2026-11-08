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
        follower.update()
        brakes.tick()

        if (gamepad1.aWasPressed()) deflowerer.mode = when (deflowerer.mode) {
            Deflowerer.Mode.UP -> Deflowerer.Mode.ROLLER
            Deflowerer.Mode.ROLLER -> Deflowerer.Mode.DOWN
            Deflowerer.Mode.DOWN -> Deflowerer.Mode.UP
        }
        if (gamepad1.bWasPressed()) brakes.mode = !brakes.mode

        val selectedServo = when {
            gamepad1.a -> deflowerer.intakeServo
            gamepad1.b -> deflowerer.plateServo
            gamepad1.x -> brakes.servoLeft
            gamepad1.y -> brakes.servoRight
            else -> null
        }

        if (gamepad1.dpadRightWasPressed()) selectedServo?.position += 0.1
        if (gamepad1.dpadLeftWasReleased()) selectedServo?.position -= 0.1
        if (gamepad1.dpadUpWasPressed()) selectedServo?.position += 0.01
        if (gamepad1.dpadDownWasReleased()) selectedServo?.position -= 0.01

        telemetry.addLine("A: intake, B: plate, X: brakes left, Y: brakes right")
        telemetry.addData("Selected Servo", when (selectedServo) {
            deflowerer.intakeServo -> "Intake"
            deflowerer.plateServo -> "Plate"
            brakes.servoLeft -> "Brakes Left"
            brakes.servoRight -> "Brakes Left"
            else -> "None"
        })
        telemetry.addData("Pose", follower.pose())
        telemetry.addData("Intake Position", deflowerer.intakeServo.position)
        telemetry.addData("Plate Position", deflowerer.plateServo.position)
        telemetry.addData("Brakes Left Position", brakes.servoLeft.position)
        telemetry.addData("Brakes Right Position", brakes.servoRight.position)
    }
}