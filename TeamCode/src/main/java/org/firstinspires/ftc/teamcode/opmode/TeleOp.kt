package org.firstinspires.ftc.teamcode.opmode

import com.bylazar.battery.PanelsBattery
import com.bylazar.configurables.annotations.Configurable
import com.bylazar.gamepad.PanelsGamepad
import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.algorithm.Foresight
import com.pedropathing.follower.Follower
import com.pedropathing.follower.ManualDrive
import com.pedropathing.math.Pose
import com.pedropathing.math.Vector2D
import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.revhub.localizers.PinpointLocalizer
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import org.firstinspires.ftc.teamcode.subsystem.Hive
import org.firstinspires.ftc.teamcode.subsystem.Poses
import org.firstinspires.ftc.teamcode.subsystem.shooter.Outtake
import org.firstinspires.ftc.teamcode.subsystem.shooter.turret.PTurret
import org.firstinspires.ftc.teamcode.subsystem.transfer.Deflowerer
import org.firstinspires.ftc.teamcode.subsystem.transfer.Gate
import org.firstinspires.ftc.teamcode.subsystem.transfer.Intake
import org.firstinspires.ftc.teamcode.subsystem.transfer.Transfer

@Configurable
@TeleOp
class TeleOp : OpMode() {
    @Configurable
    companion object {
        @JvmField var outtakePidf: PIDFCoefficients = Outtake.PidfCoefficients.LEFT
    }
    val batteryProvider = PanelsBattery.provider
    val panelsTelemetry = PanelsTelemetry.telemetry
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
    var perspective: Perspective = Perspective.RED

    override fun init() {
        follower
        outtake
        turret
        transfer
        gate
        intake
        deflowerer
    }

    override fun init_loop() {
        telemetry.addData("Perspective", perspective.name)
        perspective = when {
            gamepad1.dpad_left -> Perspective.BLUE
            gamepad1.dpad_right -> Perspective.RED
            gamepad1.dpad_up -> Perspective.OPPOSITE
            gamepad1.dpad_down -> Perspective.AUDIENCE
            else -> perspective
        }
    }

    override fun start() {
        elapsedTime.reset()
        latestMilli = 0.0
    }

    override fun loop() {
        outtake.motorLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,outtakePidf)
        outtake.motorRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,outtakePidf)
        setDrivePowers()
        tickSubsystems()
        manualOuttake()
        controlTransfer()
        controlIntake()
        controlDeflowerer()
        controlGate()
        targetTurret()
        addTelemetry()
        latestMilli = elapsedTime.milliseconds()
    }
    enum class Perspective {
        RED,
        AUDIENCE,
        BLUE,
        OPPOSITE
    }
    fun setDrivePowers() {
        val forward = when (perspective) {
            Perspective.RED -> -gamepad1.left_stick_y
            Perspective.AUDIENCE -> gamepad1.left_stick_x
            Perspective.BLUE -> gamepad1.left_stick_y
            Perspective.OPPOSITE -> -gamepad1.left_stick_x
        }.toDouble()
        val lateral = when (perspective) {
            Perspective.RED -> -gamepad1.left_stick_x
            Perspective.AUDIENCE -> -gamepad1.left_stick_y
            Perspective.BLUE -> gamepad1.left_stick_x
            Perspective.OPPOSITE -> gamepad1.left_stick_y
        }.toDouble()
        follower.manual(
            ManualDrive.fieldCentric(
                forward,
                lateral,
                gamepad1.right_stick_x.toDouble(),
                follower.pose().heading()
            )
        )
    }
    fun tickSubsystems() {
        follower.update()
        Poses.currentPose = follower.pose()

        deflowerer.tick(elapsedTime.milliseconds())
        intake.tick(elapsedTime.milliseconds() - latestMilli)
    }
    fun manualOuttake() {
        outtake.velocity += when {
            gamepad2.dpad_up -> 1.0
            gamepad2.dpad_down -> -1.0
            gamepad2.dpad_left -> -10.0
            gamepad2.dpad_right -> 10.0
            else -> 0.0
        }
    }
    fun controlTransfer() {
        transfer.mode = when {
            gamepad1.a -> Transfer.Mode.ON
            gamepad1.b -> Transfer.Mode.REVERSE
            else -> Transfer.Mode.OFF
        }
    }
    fun controlIntake() {
        if (gamepad1.xWasPressed()) intake.mode = when (intake.mode) {
            Intake.Mode.UP -> Intake.Mode.DOWN
            Intake.Mode.DOWN -> Intake.Mode.UP
        }
    }
    fun controlDeflowerer() {
        if (gamepad1.leftBumperWasPressed()) deflowerer.mode = when (deflowerer.mode) {
            Deflowerer.Mode.UP -> Deflowerer.Mode.DOWN
            Deflowerer.Mode.DOWN -> Deflowerer.Mode.UP
        }
    }
    fun controlGate() {
        if (gamepad2.right_trigger_pressed) gate.open()
        else gate.close()
    }
    fun targetTurret() {
        if (gamepad2.x || gamepad2.y || gamepad2.a || gamepad2.b) {
            val targetPosition = when {
                gamepad2.x -> Hive.BLUE_AUDIENCE.position
                gamepad2.y -> Hive.RED_AUDIENCE.position
                gamepad2.a -> Hive.BLUE_OPPOSITE.position
                gamepad2.b -> Hive.RED_OPPOSITE.position
                else -> Vector2D.zero()
            }
            val distanceToTarget = targetPosition - follower.pose().toVector2D()
            panelsTelemetry.addData("From Target", distanceToTarget.magnitude())
            targetAngle = Math.toDegrees((follower.pose().toVector2D() - targetPosition).theta())

            if (gamepad2.left_trigger_pressed) {
                regressionOuttake(distanceToTarget.magnitude())
            }
        } else {
            targetAngle -= gamepad2.right_stick_x
            manualOuttake()
        }
        turret.angle = targetAngle - Math.toDegrees(follower.pose().heading())
    }
    fun regressionOuttake(distance: Double) {
        outtake.velocity = 5.37749 * distance + 1498.71795
        //6.24732 * distance + 1513.73475
    }
    fun controlResetPosition() {
        if (gamepad1.start && gamepad2.start) {
            val vec = follower.pose().toVector2D()
            val distanceToLeft = vec.x() - 9.0
            val distanceToRight = 135.0 - vec.x()
            val distanceToBottom = vec.y() - 9.0
            val distanceToTop = 135.0 - vec.y()
            val roundedVec = Vector2D.cartesian(
                if (distanceToLeft < distanceToRight) 9.0 else 135.0,
                if (distanceToBottom < distanceToTop) 9.0 else 135.0
            )

            val roundedHeading = when (Math.toRadians(follower.pose().heading())) {
                in -180.0..<-135.0 -> 180.0
                in -135.0..<-45.0 -> -90.0
                in -45.0..<45.0 -> 0.0
                in 45.0..<135.0 -> 90.0
                in 135.0..180.0 -> 180.0
                else -> 0.0
            }
            follower.setPose(
                Pose(roundedVec.x(), roundedVec.y(), roundedHeading)
            )
        }
    }
    fun addTelemetry() {
        with (panelsTelemetry) {
            addData("Voltage", batteryProvider.batteryVoltage)
            addData("Outtake Velocity", outtake.velocity)
            addData("Read Velocity", outtake.motorLeft.velocity)
            addData("Turret Angle", turret.angle)
            addData("Turret Target", targetAngle)
            addData("Gate Mode", gate.mode.name)
            addData("Deflowerer Mode", deflowerer.mode.name)
            addData("Intake Mode", intake.mode.name)
            addData("Transfer Mode", transfer.mode.name)
            addData("X", "${follower.pose().x()} Inches")
            addData("Y", "${follower.pose().y()} Inches")
            addData("Heading", "${Math.toDegrees(follower.pose().heading())} Degrees")
            update(telemetry)
        }
    }
}