package org.firstinspires.ftc.teamcode.subsystem.shooter.turret

import com.bylazar.configurables.annotations.Configurable
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit

@Configurable
sealed class Turret {
    companion object {
        @JvmField var ANGLE_TOLERANCE: Double = 1.0
        @JvmField var MIN_ANGLE: Double = -120.0
        @JvmField var MAX_ANGLE: Double = 120.0
    }
    open var angle: Double = 0.0
        set(angle) {
            field = AngleUnit.normalizeDegrees(angle)
        }
    abstract val isWithinTolerance: Boolean
    fun off() {
        angle = 0.0
    }
}