package org.firstinspires.ftc.teamcode.opmode

import com.bylazar.configurables.annotations.Configurable
import com.pedropathing.algorithm.Foresight
import com.pedropathing.drivetrain.DrivePowers
import com.pedropathing.follower.Follower
import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.revhub.localizers.PinpointLocalizer
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.Subsystems.Limelight
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystem.Brakes
import org.firstinspires.ftc.teamcode.subsystem.Deflowerer
import org.firstinspires.ftc.teamcode.subsystem.Gate
import org.firstinspires.ftc.teamcode.subsystem.Intake
import org.firstinspires.ftc.teamcode.subsystem.Outtake
import org.firstinspires.ftc.teamcode.subsystem.TaskManager
import org.firstinspires.ftc.teamcode.subsystem.Shooter
import org.firstinspires.ftc.teamcode.subsystem.turret.PTurret

@Configurable
@TeleOp
class TeleOp : OpMode() {

    val follower: Follower by lazy {
        with (Constants) {
            Follower(
                PinpointLocalizer(hardwareMap, pinpointConfig),
                Mecanum(hardwareMap, mecanumConfig),
                Foresight(foresightConfig)
            )
        }
    }
    val shooter: Shooter by lazy {
        Shooter(
            follower = follower,
            outtake = Outtake(
                motorLeft = hardwareMap["outtakeLeft"] as DcMotorEx,
                motorRight = hardwareMap["outtakeRight"] as DcMotorEx,
            ),
            turret = PTurret(
                servoLeft = hardwareMap["turretLeft"] as Servo,
                servoMid = hardwareMap["turretMid"] as Servo,
                servoRight = hardwareMap["turretRight"] as Servo,
            ),
            gate = Gate(
                servo = hardwareMap["gate"] as Servo
            )
        )
    }
    val intake: Intake by lazy {
        Intake(
            intakeMotorLeft = hardwareMap["intakeLeft"] as DcMotorEx,
            intakeMotorRight = hardwareMap["intakeRight"] as DcMotorEx
        )
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
            servoLeft = hardwareMap["deflowererLeft"] as Servo,
            servoRight = hardwareMap["deflowererRight"] as Servo,
        )
    }
    val limelight: Limelight by lazy {
        Limelight(
            limelight3A = hardwareMap["limelight"] as Limelight3A
        )
    }
    val taskManager: TaskManager = TaskManager()

    override fun init() {
        follower
    }

    override fun init_loop() {
    }

    override fun start() {
    }

    override fun loop() {
        follower.manual(
            -(gamepad1.left_stick_y).toDouble(),
            (gamepad1.left_trigger - gamepad1.right_trigger).toDouble(),
            -(gamepad1.left_stick_x).toDouble()
        )
    }
}
