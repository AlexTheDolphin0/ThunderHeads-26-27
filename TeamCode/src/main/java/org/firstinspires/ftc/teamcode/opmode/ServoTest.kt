package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class ServoTest : OpMode() {
    val servos: List<Servo> by lazy {
        hardwareMap.getAll(Servo::class.java)
    }
    var selectedServo: Int = 0
        set(value) {
            field = value % servos.size
        }
    override fun init() {
        servos
    }

    override fun loop() {
        telemetry.addData("Selected Servo Index", selectedServo)
        telemetry.addData("Name", servos[selectedServo].deviceName)
        telemetry.addData("Port Number", servos[selectedServo].portNumber)
        telemetry.addData("Position", servos[selectedServo].position)

        if (gamepad1.rightBumperWasPressed()) selectedServo++
        if (gamepad1.leftBumperWasPressed()) selectedServo--
        if (gamepad1.dpadUpWasPressed()) servos[selectedServo].position += 0.01
        if (gamepad1.dpadDownWasPressed()) servos[selectedServo].position -= 0.01
        if (gamepad1.dpadRightWasPressed()) servos[selectedServo].position += 0.1
        if (gamepad1.dpadLeftWasPressed()) servos[selectedServo].position -= 0.1
    }
}