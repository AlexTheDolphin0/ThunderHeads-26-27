package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.prism.GoBildaPrismDriver
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import org.firstinspires.ftc.teamcode.subsystem.LEDs

@TeleOp(name = "LEDs Off", group = "Controls")
class LedsOff : OpMode() {
    val leds: LEDs by lazy {
        LEDs(
            hardwareMap["ledLeft"] as GoBildaPrismDriver,
            hardwareMap["ledRight"] as GoBildaPrismDriver,
        )
    }
    override fun init() {
        leds.off()
    }

    override fun loop() { }
}