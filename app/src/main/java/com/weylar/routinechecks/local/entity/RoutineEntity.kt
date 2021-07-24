package com.weylar.routinechecks.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weylar.routinechecks.model.Frequency
import com.weylar.routinechecks.model.Status
import java.util.*

@Entity(tableName = "Routine")
data class RoutineEntity (
    @PrimaryKey
    val id: Long = Calendar.getInstance().timeInMillis,
    val title: String,
    val description: String?,
    val date: Long,
    val frequency: Frequency,
    val lastStatus: Status,
    val nextUpTime: Long = 0L,
    val missedCount: Int = 0,
    val doneCount: Int = 0

)

