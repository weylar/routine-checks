package com.weylar.routinechecks.worker

import android.content.Context
import android.content.Intent
import androidx.work.*
import com.google.gson.Gson
import com.weylar.routinechecks.local.dataSource.LocalDataSourceImpl
import com.weylar.routinechecks.local.entity.RoutineEntity
import com.weylar.routinechecks.local.mapper.RoutineMapperImpl
import com.weylar.routinechecks.local.room.RoutineDatabase
import com.weylar.routinechecks.model.Frequency
import com.weylar.routinechecks.model.Status
import com.weylar.routinechecks.repository.RoutineRepositoryImpl
import com.weylar.routinechecks.ui.MainActivity
import com.weylar.routinechecks.util.NotificationHelper
import kotlinx.coroutines.flow.collect
import java.util.*
import java.util.concurrent.TimeUnit


class MissedWorker(
    val context: Context, workerParams: WorkerParameters
) :
    CoroutineWorker(context, workerParams) {
    private lateinit var routine: RoutineEntity
    private val repository = RoutineRepositoryImpl(
        LocalDataSourceImpl(RoutineDatabase.getInstance(context)),
        RoutineMapperImpl()
    )

    override suspend fun doWork(): Result {
        routine = Gson().fromJson(inputData.getString(ROUTINE), RoutineEntity::class.java)
        updateDatabase(routine)
        return Result.success()
    }

    private suspend fun updateDatabase(routine: RoutineEntity) {
        val result = repository.getRoutine(routine.id)
            if(result.lastStatus == Status.UNKNOWN){
                val updatedRoutine = result.copy(lastStatus = Status.DONE, missedCount = result.missedCount++)
                repository.saveRoutine(updatedRoutine)
        }
    }



    companion object {
        const val ROUTINE = "routine";
        private const val FIVE_MINUTE = 5 * 60 * 1000L

        fun startWorker(context: Context, routine: RoutineEntity) {
            val data = Data.Builder()
            data.putString(ROUTINE, Gson().toJson(routine))
            val oneTimeRequest = OneTimeWorkRequestBuilder<MissedWorker>()
                .setInitialDelay(FIVE_MINUTE, TimeUnit.MILLISECONDS)
                .setInputData(data.build())
                .build()
            WorkManager.getInstance(context).enqueue(oneTimeRequest)
        }


    }
}