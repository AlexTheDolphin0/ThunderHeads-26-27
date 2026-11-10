package org.firstinspires.ftc.teamcode.pedro

import com.pedropathing.algorithm.ForesightConfig
import com.pedropathing.controllers.Controller
import com.pedropathing.math.Matrix
import com.pedropathing.math.Vector2D
import com.pedropathing.revhub.drivetrains.MecanumConfig
import com.pedropathing.revhub.localizers.PinpointConfig
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit


object Constants {
    val mecanumConfig: MecanumConfig = MecanumConfig { config: MecanumConfig ->
        config.apply {
            frontLeftName.set("leftFront")
            backLeftName.set("leftBack")
            frontRightName.set("rightFront")
            backRightName.set("rightBack")

            frontLeftDirection.set(DcMotorSimple.Direction.REVERSE)
            backLeftDirection.set(DcMotorSimple.Direction.REVERSE)
            frontRightDirection.set(DcMotorSimple.Direction.FORWARD)
            backRightDirection.set(DcMotorSimple.Direction.FORWARD)

            manualBrakeMode.set(false)
        }
    }
    val pinpointConfig: PinpointConfig = PinpointConfig { config: PinpointConfig ->
        config.apply {
            name.set("pinpoint")
            podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            xPodOffset.set(-6.223646749661664)
            yPodOffset.set(-2.8399841428741697)
            xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            globalDistanceUnit.set(DistanceUnit.INCH)
            offsetUnits.set(DistanceUnit.INCH)
        }
    }
    val foresightConfig: ForesightConfig = ForesightConfig { config: ForesightConfig ->
        config.apply {
            val primaryTranslationalForward = Controller.proportional(1.32134409702489)
            val secondaryTranslationalForward = Controller.proportional(0.4882013368861666)
            val primaryTranslationalLateral = Controller.proportional(113.62306315862948)
            val secondaryTranslationalLateral = Controller.proportional(41.98068577295)

            forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward))
            strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral))

            coast.set(Controller.proportionalFeedforward(0.04198067800686552))
            brake.set(Controller.proportionalFeedforward(0.03568357630583569))

            headingFeedback.set(Controller.proportional(9.272757305878146))
            headingBrakeCoefficients.set(Vector2D.cartesian(0.06295800018176542, 0.004697509017601684))

            linearBrakeCoefficients.set(Matrix.diag(0.049095096790093946, 0.037015896415005745))
            quadraticBrakeCoefficients.set(Matrix.diag(0.0022575972307257923, 0.002249260065691901))

            maxAchievableForwardVelocity.set(50.235653255484536)
            maxAchievableStrafeVelocity.set(35.14336327973978)
            naturalForwardDeceleration.set(82.81772577210577)
            naturalStrafeDeceleration.set(108.03534422255021)
        }
    }
}