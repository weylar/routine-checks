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
import com.weylar.routinechecks.repository.RoutineRepositoryImpl
import com.weylar.routinechecks.ui.MainActivity
import com.weylar.routinechecks.util.NotificationHelper
import kotlinx.coroutines.flow.collect
import java.util.*
import java.util.concurrent.TimeUnit


class RoutineWorker(
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
        sendNotification()
        updateDatabase(routine)
        triggerStatusWorker()
        rescheduleWorker(context, routine)
        return Result.success()
    }

    private fun triggerStatusWorker() {
        MissedWorker.startWorker(context, routine)
    }

    private suspend fun updateDatabase(routine: RoutineEntity) {
        val result = repository.getRoutine(routine.id)
            val updatedRoutine =
                result.copy(nextUpTime = Calendar.getInstance().timeInMillis + generateTime(routine.frequency))
            repository.saveRoutine(updatedRoutine)

    }

    private fun sendNotification() {
        NotificationHelper(context).showNotificationMessage(
            routine.title,
            routine.description ?: "",
            Calendar.getInstance().timeInMillis,
            Intent(context, MainActivity::class.java)
        )
    }


    companion object {
        const val ROUTINE = "routine";
        private const val FIVE_MINUTE = 5 * 60 * 1000

        fun startWorker(context: Context, routine: RoutineEntity) {
            val data = Data.Builder()
            data.putString(ROUTINE, Gson().toJson(routine))
            val delay = routine.date - Calendar.getInstance().timeInMillis
            val oneTimeRequest = OneTimeWorkRequestBuilder<RoutineWorker>()
                .setInitialDelay(
                    if (delay > FIVE_MINUTE) delay - FIVE_MINUTE else delay, TimeUnit.MILLISECONDS
                )
                .addTag(routine.id.toString())
                .setInputData(data.build())
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(ROUTINE, ExistingWorkPolicy.REPLACE, oneTimeRequest)
        }

        fun rescheduleWorker(context: Context, routine: RoutineEntity) {
            val data = Data.Builder()
            data.putString(ROUTINE, Gson().toJson(routine))
            val delay = generateTime(routine.frequency)
            val oneTimeRequest = OneTimeWorkRequestBuilder<RoutineWorker>()
                .setInitialDelay(
                    if (delay > FIVE_MINUTE) delay - FIVE_MINUTE else delay, TimeUnit.MILLISECONDS
                )
                .addTag(routine.id.toString())
                .setInputData(data.build())
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(ROUTINE, ExistingWorkPolicy.REPLACE, oneTimeRequest)
        }

        private fun generateTime(frequency: Frequency): Long {
            return when (frequency) {
                Frequency.HOURLY -> 1 * 60 * 60 * 1000L
                Frequency.DAILY -> 24 * 60 * 60 * 1000L
                Frequency.WEEKLY -> 168 * 60 * 60 * 1000L
                Frequency.MONTHLY -> 720 * 60 * 60 * 1000L
                Frequency.YEARLY -> 8760 * 60 * 60 * 1000L
            }
        }


        fun cancelWorker(context: Context, routine: RoutineEntity) {
            WorkManager.getInstance(context).cancelAllWorkByTag(routine.id.toString())
        }

    }
}