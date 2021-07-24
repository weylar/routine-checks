package com.weylar.routinechecks.model

import androidx.room.PrimaryKey
import java.util.*

typealias Routines = List<Routine>
val dummyRoutine = Routine(title = "", description = "", date = 0L, frequency = Frequency.DAILY)

data class Routine(
    val id: Long = Calendar.getInstance().timeInMillis,
    val title: String,
    val description: String?,
    val date: Long,
    val frequency: Frequency,
    val lastStatus: Status = Status.UNKNOWN,
    val nextUpTime: Long = 0L,
    var missedCount: Int = 0,
    var doneCount: Int = 0
)



enum class Frequency {
    HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY
}
enum class Status {
    DONE, MISSED, UNKNOWN
}
