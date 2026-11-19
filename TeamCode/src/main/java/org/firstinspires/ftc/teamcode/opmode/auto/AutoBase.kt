package org.firstinspires.ftc.teamcode.opmode.auto

import com.pedropathing.algorithm.Foresight
import com.pedropathing.follower.Follower
import com.pedropathing.math.Pose
import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.revhub.localizers.PinpointLocalizer
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import org.firstinspires.ftc.teamcode.subsystem.Event
import org.firstinspires.ftc.teamcode.subsystem.EventLoop
import org.firstinspires.ftc.teamcode.subsystem.Hive
import org.firstinspires.ftc.teamcode.subsystem.Poses
import org.firstinspires.ftc.teamcode.subsystem.shooter.Outtake
import org.firstinspires.ftc.teamcode.subsystem.shooter.turret.PTurret
import org.firstinspires.ftc.teamcode.subsystem.transfer.Deflowerer
import org.firstinspires.ftc.teamcode.subsystem.transfer.Gate
import org.firstinspires.ftc.teamcode.subsystem.transfer.Intake
import org.firstinspires.ftc.teamcode.subsystem.transfer.Transfer

abstract class AutoBase : OpMode() {
    abstract val alliance: Alliance
    abstract val startPose: Pose
    abstract val eventList: List<Event>
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
    val elapsedTime: ElapsedTime = ElapsedTime()
    val eventLoop: EventLoop = EventLoop()
    var latestMillis: Double = 0.0
    override fun init() {
        follower
        outtake
        transfer.off()
        turret
        deflowerer.up()
        intake.up()
        gate.close()
        follower.setPose(startPose)
        follower.holdEnd
        eventLoop.addEvents(eventList)
        Alliance.alliance = alliance
    }
    override fun start() {
        elapsedTime.reset()
        latestMillis = 0.0
    }
    override fun loop() {
        val currentMillis = elapsedTime.milliseconds()
        val deltaMillis = currentMillis - latestMillis
        eventLoop.tick(deltaMillis)
        follower.update()
        Poses.currentPose = follower.pose()

        deflowerer.tick(currentMillis)
        intake.tick(deltaMillis)

        turret.angle = Math.toDegrees((follower.pose().toVector2D() - Hive[Alliance.alliance, Alliance.alliance.orientation].position).theta()) - Math.toDegrees(follower.pose().heading())
        telemetry.addData("Current Event", eventLoop.currentEvent?.name)
        telemetry.addData("Next Event", eventLoop.queue.firstOrNull()?.name)
        telemetry.addData("Distance To Endpoint", "${follower.distanceToEndpoint()} Inches")
        telemetry.addData("Event Time", "${eventLoop.millisSinceEventStart} Milliseconds")
        telemetry.addData("Measured Time", "${elapsedTime.milliseconds()} Milliseconds")
        telemetry.addData("Time", "$currentMillis Milliseconds")

        latestMillis = currentMillis
    }
    val spinUp: Event = Event(
        "Spin Up",
        3000.0,
        {
            outtake.velocity = 1300.0
        }
    )
    val shoot: Event = Event(
        "Shoot",
        4000.0,
        start = {
            gate.open()
            transfer.on()
        },
        end = {
            gate.close()
            transfer.off()
        }
    )
    val spinDown: Event = Event(
        "Spin Down",
        0.0,
        {
            outtake.velocity = 0.0
        }
    )
}