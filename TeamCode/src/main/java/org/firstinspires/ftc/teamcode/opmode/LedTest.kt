package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.prism.Color
import org.firstinspires.ftc.teamcode.prism.GoBildaPrismDriver
import org.firstinspires.ftc.teamcode.prism.PrismAnimations
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import org.firstinspires.ftc.teamcode.subsystem.LEDs

@TeleOp(name = "LED Test", group = "Tests")
class LedTest : OpMode() {
    var warningLightsDuration = 0.0
    val elapsedTime: ElapsedTime = ElapsedTime()
    var latestMillis = 0.0
    val leds: LEDs by lazy {
        LEDs(
            hardwareMap["ledLeft"] as GoBildaPrismDriver,
            hardwareMap["ledRight"] as GoBildaPrismDriver,
        )
    }
    override fun init() {
        leds.matchAlliance()
        latestMillis = elapsedTime.milliseconds()
    }

    override fun init_loop() {
        if (warningLightsDuration > 0.0) {
            warningLightsDuration -= elapsedTime.milliseconds() - latestMillis
        }
        if (warningLightsDuration < 0.0){
            warningLightsDuration = 0.0
            leds.matchAlliance()
        }
        if (gamepad2.ps) {
            warningLightsDuration = 1000.0
            leds.warningLights()
        }
        if (gamepad1.left_bumper) {
            Alliance.alliance = Alliance.RED
            leds.matchAlliance()
        }
        if (gamepad1.right_bumper) {
            Alliance.alliance = Alliance.BLUE
            leds.matchAlliance()
        }
        if (gamepad2.left_bumper && gamepad2.right_bumper) {
            leds.prismAnimation = PrismAnimations.Snakes(
                Color.GREEN,
                Color.TRANSPARENT
            )
        }
        telemetry.addData("Button", gamepad2.ps)
        latestMillis = elapsedTime.milliseconds()
    }

    override fun loop() {
        if (gamepad1.left_bumper) {
            Alliance.alliance = Alliance.RED
            leds.matchAlliance()
        }
        if (gamepad1.right_bumper) {
            Alliance.alliance = Alliance.BLUE
            leds.matchAlliance()
        }
        if (gamepad2.left_bumper && gamepad2.right_bumper) {
            leds.prismAnimation = PrismAnimations.Snakes(
                Color.GREEN,
                Color.TRANSPARENT
            )
        }
    }
}