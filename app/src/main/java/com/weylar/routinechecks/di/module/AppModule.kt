package com.weylar.routinechecks.di.module

import android.content.Context
import com.weylar.routinechecks.local.dataSource.LocalDataSource
import com.weylar.routinechecks.local.dataSource.LocalDataSourceImpl
import com.weylar.routinechecks.local.mapper.RoutineMapper
import com.weylar.routinechecks.local.mapper.RoutineMapperImpl
import com.weylar.routinechecks.local.room.RoutineDatabase
import com.weylar.routinechecks.navigator.AppNavDispatcher
import com.weylar.routinechecks.repository.RoutineRepository
import com.weylar.routinechecks.repository.RoutineRepositoryImpl
import com.weylar.routinechecks.ui.AppNavImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideHomeNavImpl(): AppNavDispatcher = AppNavImpl()

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): RoutineDatabase {
        return RoutineDatabase.getInstance(context)
    }

    @Provides
    fun provideLocalDataSource(database: RoutineDatabase): LocalDataSource {
        return LocalDataSourceImpl(database)
    }

    @Provides
    fun provideMapper(): RoutineMapper {
        return RoutineMapperImpl()
    }

    @Provides
    fun provideRoutineRepository(
        localDataSource: LocalDataSource,
        mapper: RoutineMapper
    ): RoutineRepository {
        return RoutineRepositoryImpl(localDataSource, mapper)
    }
}