package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.util.ElapsedTime

class TaskManager {
    class Call(
        val elapsedTime: ElapsedTime
    ) {
        val init: MutableSet<(ElapsedTime) -> Unit> = mutableSetOf()
        val action: MutableSet<(ElapsedTime) -> Unit> = mutableSetOf()
        operator fun invoke() {
            for (function in init) {
                function(elapsedTime)
            }
            for (function in action) {
                function(elapsedTime)
            }
        }
    }
    val elapsedTime: ElapsedTime = ElapsedTime()
    val initLoop: Call = Call(elapsedTime)
    val loop: Call = Call(elapsedTime)
}