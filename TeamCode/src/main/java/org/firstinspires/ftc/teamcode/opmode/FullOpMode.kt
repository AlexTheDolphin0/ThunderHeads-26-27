package org.firstinspires.ftc.teamcode.opmode

import com.pedropathing.algorithm.Foresight
import com.pedropathing.follower.Follower
import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.revhub.localizers.PinpointLocalizer
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystem.Brakes
import org.firstinspires.ftc.teamcode.subsystem.transfer.Deflowerer
import org.firstinspires.ftc.teamcode.subsystem.transfer.Gate
import org.firstinspires.ftc.teamcode.subsystem.transfer.Intake
import org.firstinspires.ftc.teamcode.subsystem.Limelight
import org.firstinspires.ftc.teamcode.subsystem.shooter.Outtake
import org.firstinspires.ftc.teamcode.subsystem.shooter.Shooter
import org.firstinspires.ftc.teamcode.subsystem.shooter.turret.PTurret

abstract class FullOpMode : OpMode() {
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
    val deflowerer: Deflowerer by lazy {
        Deflowerer(
            intakeServo = hardwareMap["intakeServo"] as Servo,
            plateServo = hardwareMap["plateServo"] as Servo,
        )
    }
    val limelight: Limelight by lazy {
        Limelight(
            limelight3A = hardwareMap["limelight"] as Limelight3A
        )
    }

    override fun init() {
        follower
        shooter
        intake
        deflowerer
        limelight
    }

    override fun loop() {
        follower.update()
        limelight.tick()
        shooter.tick()
    }
}