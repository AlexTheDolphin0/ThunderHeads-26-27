package org.firstinspires.ftc.teamcode.pedro

import com.pedropathing.algorithm.ForesightConfig
import com.pedropathing.controllers.Controller
import com.pedropathing.math.Matrix
import com.pedropathing.math.Vector2D
import com.pedropathing.revhub.drivetrains.MecanumConfig
import com.pedropathing.revhub.localizers.PinpointConfig
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.hardware.DcMotorSimple


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
            podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD)
            xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            xPodOffset.set(0.0)
            yPodOffset.set(0.0)
        }
    }
    val foresightConfig: ForesightConfig = ForesightConfig { config: ForesightConfig ->
        config.apply {
        val primaryTranslationalForward = Controller.proportional(0.3)
        val secondaryTranslationalForward = Controller.proportional(0.1)
        val primaryTranslationalLateral = Controller.proportional(0.3)
        val secondaryTranslationalLateral = Controller.proportional(0.1)

        forwardTranslational.set(
            Controller.piecewise(secondaryTranslationalForward)
                .put(2.5, primaryTranslationalForward)
        )
        strafeTranslational.set(
            Controller.piecewise(secondaryTranslationalLateral)
                .put(2.5, primaryTranslationalLateral)
        )

        coast.set(Controller.proportionalFeedforward(0.010978350889324107))
        brake.set(Controller.proportionalFeedforward(0.008731598255925491))

        headingFeedback.set(Controller.proportional(5.258721785960744))
        headingBrakeCoefficients.set(
            Vector2D.cartesian(
                0.05642143125655298,
                0.0063829525363003695
            )
        )

        linearBrakeCoefficients.set(Matrix.diag(0.10605894992901523, 0.08719146175596092))
        quadraticBrakeCoefficients.set(Matrix.diag(0.0014663966976606565, 0.0013837064502458813))

        maxAchievableForwardVelocity.set(72.72923108818539)
        maxAchievableStrafeVelocity.set(52.34323936525474)
        naturalForwardDeceleration.set(85.01144677379789)
        naturalStrafeDeceleration.set(104.49787535782846)
        }
    }
}