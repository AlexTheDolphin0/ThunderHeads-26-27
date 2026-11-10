package org.firstinspires.ftc.teamcode.subsystem.transfer

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.robotcore.external.Telemetry

class Intake(
    val intakeMotorLeft: DcMotorEx,
    val intakeMotorRight: DcMotorEx
) {
    enum class Mode(val power: Double) {
        ON(0.5),
        OFF(0.0),
        REVERSE(-0.5)
    }
    init {
        intakeMotorLeft.direction = DcMotorSimple.Direction.FORWARD
        intakeMotorLeft.direction = DcMotorSimple.Direction.REVERSE
        this.off()
    }
    var power: Double = 0.0
        set(power) {
            field = power
            intakeMotorLeft.power = power
            intakeMotorRight.power = power
        }
    var mode: Mode = Mode.OFF
        set(value) {
            field = value
            power = value.power
        }

    fun on() {
        mode = Mode.ON
    }

    fun off() {
        mode = Mode.OFF
    }

    fun reverse() {
        mode = Mode.REVERSE
    }

    fun toggle() {
        mode = when (mode) {
            Mode.ON -> Mode.OFF
            Mode.OFF -> Mode.ON
            Mode.REVERSE -> Mode.OFF
        }
    }
}