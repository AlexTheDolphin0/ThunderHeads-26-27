package org.firstinspires.ftc.teamcode.pedro

import com.pedropathing.config.Configuration
import com.pedropathing.follower.Follower
import com.pedropathing.revhub.drivetrains.MecanumConfig
import com.pedropathing.revhub.localizers.PinpointConfig
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap


object Constants {
    fun create(h: HardwareMap?): Follower? {
        // return new Follower(Drivetrain, Localizer, Foresight);
        return null
    }
    val drivetrainConfig: MecanumConfig = MecanumConfig { config: MecanumConfig ->
            config.frontLeftName.set("left_front")
            config.backLeftName.set("left_back")
            config.frontRightName.set("right_front")
            config.backRightName.set("right_back")

            config.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE)
            config.backLeftDirection.set(DcMotorSimple.Direction.REVERSE)
            config.frontRightDirection.set(DcMotorSimple.Direction.FORWARD)
            config.backRightDirection.set(DcMotorSimple.Direction.FORWARD)

            config.manualBrakeMode.set(false)
    }

    val localizerConfig: PinpointConfig = PinpointConfig { config: PinpointConfig ->
        config.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD)
    }
}