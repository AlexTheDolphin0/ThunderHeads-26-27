package org.firstinspires.ftc.teamcode.opmode

import com.pedropathing.algorithm.Foresight
import com.pedropathing.follower.Follower
import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.revhub.localizers.PinpointLocalizer
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystem.Brakes
import org.firstinspires.ftc.teamcode.subsystem.transfer.Deflowerer
import org.firstinspires.ftc.teamcode.subsystem.transfer.Intake

@TeleOp
class Testing : OpMode() {
    val elapsedTime: ElapsedTime = ElapsedTime()
    val intake: Intake by lazy {
        Intake(
            intakeMotorLeft = hardwareMap["intakeLeft"] as DcMotorEx,
            intakeMotorRight = hardwareMap["intakeRight"] as DcMotorEx
        )
    }
    val deflowerer: Deflowerer by lazy {
        Deflowerer(
            intakeServo = hardwareMap["intake"] as Servo,
            plateServo = hardwareMap["plate"] as Servo,
        )
    }

    override fun init() {
        elapsedTime.reset()
        deflowerer
        intake
    }

    override fun start() {
        elapsedTime.reset()
    }

    override fun loop() {
        deflowerer.tick(elapsedTime.milliseconds())

        if (gamepad2.aWasPressed()) deflowerer.mode = when (deflowerer.mode) {
            Deflowerer.Mode.UP -> Deflowerer.Mode.ROLLER
            Deflowerer.Mode.ROLLER -> Deflowerer.Mode.DOWN
            Deflowerer.Mode.DOWN -> Deflowerer.Mode.WIGGLE
            Deflowerer.Mode.WIGGLE -> Deflowerer.Mode.UP
        }

        when {
            gamepad2.x -> intake.on()
            gamepad2.y -> intake.reverse()
            else -> intake.off()
        }

        val selectedServo = when {
            gamepad1.a -> deflowerer.intakeServo
            gamepad1.b -> deflowerer.plateServo
            else -> null
        }

        if (gamepad1.dpadRightWasPressed()) selectedServo?.position += 0.1
        if (gamepad1.dpadLeftWasReleased()) selectedServo?.position -= 0.1
        if (gamepad1.dpadUpWasPressed()) selectedServo?.position += 0.01
        if (gamepad1.dpadDownWasReleased()) selectedServo?.position -= 0.01

        telemetry.addLine("A: intake, B: plate")
        telemetry.addData("Selected Servo", when (selectedServo) {
            deflowerer.intakeServo -> "Intake"
            deflowerer.plateServo -> "Plate"
            else -> "None"
        })
        telemetry.addData("Intake Position", deflowerer.intakeServo.position)
        telemetry.addData("Plate Position", deflowerer.plateServo.position)
        telemetry.addData("Deflowerer Mode", deflowerer.mode.name)
        telemetry.addData("Intake Mode", intake.mode.name)
    }
}