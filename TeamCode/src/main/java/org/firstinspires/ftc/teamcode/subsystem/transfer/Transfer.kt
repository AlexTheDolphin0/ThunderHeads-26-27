package org.firstinspires.ftc.teamcode.subsystem.transfer

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple

@Configurable
class Transfer(
    val motorTop: DcMotorEx,
    val motorBottom: DcMotorEx
) {
    object Powers {
        val ON = 1.0
        val OFF = 0.0
        val REVERSE = -1.0
    }
    enum class Mode(val power: Double) {
        ON(Powers.ON),
        OFF(Powers.OFF),
        REVERSE(Powers.REVERSE)
    }
    init {
        motorTop.direction = DcMotorSimple.Direction.REVERSE
        motorBottom.direction = DcMotorSimple.Direction.REVERSE
        this.off()
    }
    var power: Double = 0.0
        set(power) {
            field = power
            motorTop.power = power
            motorBottom.power = power
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