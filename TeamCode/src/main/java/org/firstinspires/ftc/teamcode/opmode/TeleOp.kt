package org.firstinspires.ftc.teamcode.opmode

import com.pedropathing.algorithm.Foresight
import com.pedropathing.follower.Follower
import com.pedropathing.follower.ManualDrive
import com.pedropathing.math.Vector2D
import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.revhub.localizers.PinpointLocalizer
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystem.Hive
import org.firstinspires.ftc.teamcode.subsystem.Poses
import org.firstinspires.ftc.teamcode.subsystem.shooter.Outtake
import org.firstinspires.ftc.teamcode.subsystem.shooter.turret.PTurret
import org.firstinspires.ftc.teamcode.subsystem.transfer.Deflowerer
import org.firstinspires.ftc.teamcode.subsystem.transfer.Gate
import org.firstinspires.ftc.teamcode.subsystem.transfer.Intake
import org.firstinspires.ftc.teamcode.subsystem.transfer.Transfer

@TeleOp
class TeleOp : OpMode() {
    val elapsedTime: ElapsedTime = ElapsedTime()
    val follower: Follower by lazy {
        with (Constants) {
            Follower(
                PinpointLocalizer(hardwareMap, pinpointConfig),
                Mecanum(hardwareMap, mecanumConfig),
                Foresight(foresightConfig)
            )
        }
    }
    val outtake: Outtake by lazy {
        Outtake(
            motorLeft = hardwareMap["outtakeLeft"] as DcMotorEx,
            motorRight = hardwareMap["outtakeRight"] as DcMotorEx,
        )
    }
    val transfer: Transfer by lazy {
        Transfer(
            motorTop = hardwareMap["transferTop"] as DcMotorEx,
            motorBottom = hardwareMap["transferBottom"] as DcMotorEx
        )
    }
    val turret: PTurret by lazy {
        PTurret(
            hardwareMap["turretLeft"] as Servo,
            hardwareMap["turretMid"] as Servo,
            hardwareMap["turretRight"] as Servo
        )
    }
    val deflowerer: Deflowerer by lazy {
        Deflowerer(
            servo = hardwareMap["deflowerer"] as Servo,
        )
    }
    val intake: Intake by lazy {
        Intake(
            servo = hardwareMap["intake"] as Servo,
        )
    }
    val gate: Gate by lazy {
        Gate(
            hardwareMap["gate"] as Servo,
        )
    }
    var latestMilli: Double = 0.0
    var targetAngle = 0.0

    override fun init() {
        follower
        outtake
        turret
        transfer
        gate
        intake
        deflowerer
    }

    override fun start() {
        elapsedTime.reset()
        latestMilli = 0.0
    }

    override fun loop() {
        follower.manual(
            ManualDrive.fieldCentric(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                gamepad1.right_stick_x.toDouble(),
                follower.pose().heading()
            )
        )
        follower.update()
        Poses.currentPose = follower.pose()

        deflowerer.tick(elapsedTime.milliseconds())
        intake.tick(elapsedTime.milliseconds() - latestMilli)

        targetAngle -= gamepad2.right_stick_x

        outtake.velocity += when {
            gamepad2.dpad_up -> 1.0
            gamepad2.dpad_down -> -1.0
            gamepad2.dpad_left -> -10.0
            gamepad2.dpad_right -> 10.0
            else -> 0.0
        }
        transfer.mode = when {
            gamepad1.a -> Transfer.Mode.ON
            gamepad1.b -> Transfer.Mode.REVERSE
            else -> Transfer.Mode.OFF
        }
        if (gamepad1.xWasPressed()) intake.mode = when (intake.mode) {
            Intake.Mode.UP -> Intake.Mode.DOWN
            Intake.Mode.DOWN -> Intake.Mode.UP
        }
        if (gamepad1.leftBumperWasPressed()) deflowerer.mode = when (deflowerer.mode) {
            Deflowerer.Mode.UP -> Deflowerer.Mode.DOWN
            Deflowerer.Mode.DOWN -> Deflowerer.Mode.UP
        }
        if (gamepad2.right_trigger_pressed) gate.open()
        else gate.close()
        turret.angle = targetAngle - Math.toDegrees(follower.pose().heading())

        if (gamepad2.x || gamepad2.y || gamepad2.a || gamepad2.b) {
            val targetPosition = when {
                gamepad2.x -> Hive.BLUE_AUDIENCE.position
                gamepad2.y -> Hive.RED_AUDIENCE.position
                gamepad2.a -> Hive.BLUE_OPPOSITE.position
                gamepad2.b -> Hive.RED_OPPOSITE.position
                else -> Vector2D.zero()
            }
            val distanceToTarget = targetPosition - follower.pose().toVector2D()
            telemetry.addData("From Target", distanceToTarget.magnitude())
            targetAngle = Math.toDegrees((follower.pose().toVector2D() - targetPosition).theta())
        }

        telemetry.addData("Outtake Velocity", outtake.velocity)
        telemetry.addData("Read Velocity", outtake.motorLeft.velocity)
        telemetry.addData("Turret Angle", turret.angle)
        telemetry.addData("Turret Target", targetAngle)
        telemetry.addData("Gate Mode", gate.mode.name)
        telemetry.addData("Deflowerer Mode", deflowerer.mode.name)
        telemetry.addData("Intake Mode", intake.mode.name)
        telemetry.addData("Transfer Mode", transfer.mode.name)
        telemetry.addData("Position", follower.pose().x().toString() + ", " + follower.pose().y().toString())
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()).toString() + " Degrees")

        latestMilli = elapsedTime.milliseconds()
    }
}