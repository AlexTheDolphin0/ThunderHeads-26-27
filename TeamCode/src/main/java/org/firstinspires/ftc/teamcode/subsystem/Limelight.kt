package org.firstinspires.ftc.teamcode.Subsystems

import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.subsystem.Hive
import org.firstinspires.ftc.teamcode.subsystem.Subsystem

class Limelight(val limelight3A: Limelight3A) : Subsystem() {
    companion object {
        const val PIPELINE_ID: Int = 0
        const val POLL_RATE: Int = 20
        private fun isUpsideDown(result: FiducialResult): Boolean {
            return result.targetPoseCameraSpace.orientation.getRoll(AngleUnit.DEGREES) > 180
        }
    }
    init {
        with (limelight3A) {
            pipelineSwitch(PIPELINE_ID)
            setPollRateHz(POLL_RATE)
            start()
        }
    }

    fun updateHiveOrientations() {
        val results = limelight3A.latestResult.fiducialResults
        for (result in results) {
            with(Hive[result.fiducialId]) {
                this?: continue
                alliance.orientation = when {
                    isUpsideDown(result) -> !orientation
                    else -> orientation
                }
            }
        }
    }

    override fun printState(telemetry: Telemetry) {
        TODO("Not yet implemented")
    }
}