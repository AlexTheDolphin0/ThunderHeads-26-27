package org.firstinspires.ftc.teamcode.opmode

import com.bylazar.configurables.annotations.Configurable
import com.pedropathing.follower.Follower
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystem.Brakes
import org.firstinspires.ftc.teamcode.subsystem.Deflowerer
import org.firstinspires.ftc.teamcode.subsystem.Gate
import org.firstinspires.ftc.teamcode.subsystem.Intake
import org.firstinspires.ftc.teamcode.subsystem.Outtake
import org.firstinspires.ftc.teamcode.subsystem.Shooter
import org.firstinspires.ftc.teamcode.subsystem.turret.PTurret

@Configurable
@TeleOp
class TeleOp : OpMode() {

    val follower: Follower by lazy {
        Constants.create(hardwareMap)!!
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

    override fun init() {
    }

    override fun init_loop() {
    }

    override fun start() {
    }

    override fun loop() {
        follower.update()
        shooter.tick()
        brakes.tick()
    }
}
