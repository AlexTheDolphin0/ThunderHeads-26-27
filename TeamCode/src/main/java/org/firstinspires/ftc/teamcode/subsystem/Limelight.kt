package org.firstinspires.ftc.teamcode.Subsystems

import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import org.firstinspires.ftc.teamcode.subsystem.Hive
import org.firstinspires.ftc.teamcode.subsystem.Subsystem

class Limelight(val limelight: Limelight3A) : Subsystem() {
    companion object {
        const val PIPELINE_ID: Int = 0
        const val POLL_RATE: Int = 20

        private fun isUpsideDown(result: FiducialResult): Boolean {
            return result.targetPoseCameraSpace.orientation.getRoll(AngleUnit.DEGREES) > 180
        }
    }
    init {
        limelight.pipelineSwitch(PIPELINE_ID)
        limelight.setPollRateHz(POLL_RATE)
        limelight.start()
    }

    fun updateHiveOrientations() {
        val results = limelight.latestResult.fiducialResults
        for (result in results) {
            for (hive in Hive.entries) {
                if (hive.aprilTagIds.contains(result.fiducialId)) {
                    Alliance.alliance.orientation = when {
                        isUpsideDown(result) -> !hive.orientation
                        else -> hive.orientation
                    }
                }
            }
        }
    }

    override fun printState(telemetry: Telemetry) {
        TODO("Not yet implemented")
    }
}