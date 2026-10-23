package org.firstinspires.ftc.teamcode.Subsystems

import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.subsystem.Alliance
import java.util.EnumMap
import java.util.Optional

class Limelight(val limelight: Limelight3A) {
    companion object {
        const val PIPELINE_ID: Int = 0
        const val POLL_RATE: Int = 20
        object AprilTagIds {
            var RED_OPPOSITE: IntArray = intArrayOf(30, 31, 32, 33)
            var RED_AUDIENCE: IntArray = intArrayOf(34, 35, 36, 37)
            var BLUE_OPPOSITE: IntArray = intArrayOf(38, 39, 40, 41)
            var BLUE_AUDIENCE: IntArray = intArrayOf(42, 43, 44, 45)
        }

        private fun isUpsideDown(result: FiducialResult): Boolean {
            return result.targetPoseCameraSpace.orientation.getRoll(AngleUnit.DEGREES) > 180
        }
    }
    init {
        limelight.pipelineSwitch(PIPELINE_ID)
        limelight.setPollRateHz(POLL_RATE)
        limelight.start()
    }
    var redOrientation: HiveOrientation = HiveOrientation.Audience
        private set
    var blueOrientation: HiveOrientation = HiveOrientation.Opposite
        private set

    fun updateHiveOrientations() { //I have no idea how to make this cleaner
        val results = limelight.latestResult.fiducialResults
        for (result in results) {
            if (AprilTagIds.RED_AUDIENCE.contains(result.fiducialId)) {
                if (isUpsideDown(result)) {
                    redOrientation = HiveOrientation.Audience
                }
                redOrientation = HiveOrientation.Opposite
            }
            if (AprilTagIds.RED_OPPOSITE.contains(result.fiducialId)) {
                if (isUpsideDown(result)) {
                    redOrientation = HiveOrientation.Opposite
                }
                redOrientation = HiveOrientation.Audience
            }
            if (AprilTagIds.BLUE_AUDIENCE.contains(result.fiducialId)) {
                if (isUpsideDown(result)) {
                    blueOrientation = HiveOrientation.Audience
                }
                blueOrientation = HiveOrientation.Opposite
            }
            if (AprilTagIds.BLUE_OPPOSITE.contains(result.fiducialId)) {
                if (isUpsideDown(result)) {
                    blueOrientation = HiveOrientation.Opposite
                }
                blueOrientation = HiveOrientation.Audience
            }
        }
    }
    enum class HiveOrientation {
        Audience,
        Opposite
    }
}