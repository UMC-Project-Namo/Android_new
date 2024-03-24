package com.mongmong.namo.presentation.di

import com.mongmong.namo.data.datasource.schedule.LocalScheduleDataSource
import com.mongmong.namo.data.datasource.schedule.RemoteScheduleDataSource
import com.mongmong.namo.data.datasource.diary.DiaryPersonalPagingSource
import com.mongmong.namo.data.datasource.diary.LocalDiaryDataSource
import com.mongmong.namo.data.datasource.diary.RemoteDiaryDataSource
import com.mongmong.namo.data.local.dao.DiaryDao
import com.mongmong.namo.data.remote.diary.DiaryApiService
import com.mongmong.namo.data.remote.diary.DiaryService
import com.mongmong.namo.data.remote.diary.NetworkChecker
import com.mongmong.namo.data.repositoriyImpl.DiaryRepositoryImpl
import com.mongmong.namo.data.repositoriyImpl.ScheduleRepositoryImpl
import com.mongmong.namo.domain.repositories.DiaryRepository
import com.mongmong.namo.domain.repositories.ScheduleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    /** 일정 */
    @Provides
    fun provideScheduleRepository(
        localScheduleDataSource: LocalScheduleDataSource,
        remoteScheduleDataSource: RemoteScheduleDataSource,
        networkChecker: NetworkChecker
    ): ScheduleRepository = ScheduleRepositoryImpl(localScheduleDataSource, remoteScheduleDataSource, networkChecker)

    /** 기록 */
    @Provides
    fun provideDiaryRepository(
        localDiaryDataSource: LocalDiaryDataSource,
        remoteDiaryDataSource: RemoteDiaryDataSource,
        diaryDao: DiaryDao,
        diaryService: DiaryApiService,
        networkChecker: NetworkChecker
    ): DiaryRepository = DiaryRepositoryImpl(
        localDiaryDataSource,
        remoteDiaryDataSource,
        diaryDao,
        diaryService,
        networkChecker
    )
}