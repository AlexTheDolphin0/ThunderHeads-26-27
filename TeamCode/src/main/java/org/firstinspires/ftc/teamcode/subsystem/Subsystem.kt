package org.firstinspires.ftc.teamcode.subsystem

import org.firstinspires.ftc.robotcore.external.Telemetry

abstract class Subsystem {
    /**
     *
     */
    abstract fun printState(telemetry: Telemetry)
}