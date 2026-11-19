package org.firstinspires.ftc.teamcode.subsystem

class EventLoop {
    val queue: ArrayDeque<Event> = ArrayDeque()
    var currentEvent: Event? = null
        private set
    var millisSinceEventStart = 0.0
    fun tick(deltaTimeMillis: Double) {
        millisSinceEventStart += deltaTimeMillis
        if (currentEvent == null) {
            millisSinceEventStart = 0.0
            tryDequeue()
        }
        currentEvent?.let {
            if (millisSinceEventStart > it.durationMillis) {
                it.end()
                millisSinceEventStart %= it.durationMillis
                tryDequeue()
            }
            it.loop(millisSinceEventStart)
        }
    }
    private fun tryDequeue() {
        currentEvent = queue.removeFirstOrNull()?.apply { start() }
    }
    fun addEvents(events: List<Event>) {
        for (event in events) {
            queue.addLast(event)
        }
    }
}

data class Event(
    val name: String,
    val durationMillis: Double,
    val start: () -> Unit = {},
    val loop: (Double) -> Unit = {},
    val end: () -> Unit = {},
)