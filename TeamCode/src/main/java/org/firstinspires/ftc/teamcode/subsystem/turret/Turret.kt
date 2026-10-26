package org.firstinspires.ftc.teamcode.subsystem.turret

import org.firstinspires.ftc.teamcode.subsystem.Subsystem

sealed class Turret : Subsystem() {
    companion object {
        const val ANGLE_TOLERANCE: Double = 1.0
    }
    abstract var angle: Double
    abstract val isWithinTolerance: Boolean
    fun off() {
        angle = 0.0
    }
    init {
        off()
    }
}