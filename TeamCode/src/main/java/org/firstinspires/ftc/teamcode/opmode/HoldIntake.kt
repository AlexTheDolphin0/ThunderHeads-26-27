package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp(name = "Hold Intake", group = "Tests")
class HoldIntake : OpMode() {
    val servo by lazy { hardwareMap["intake"] as Servo }
    override fun init() {
        servo.position = 1.0
    }

    override fun loop() {
        servo.position = 1.0
    }
}