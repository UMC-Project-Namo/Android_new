package com.mongmong.namo.domain.repositories

import com.mongmong.namo.data.local.entity.home.Schedule
import com.mongmong.namo.domain.model.GetMonthScheduleResult

interface ScheduleRepository {
    /** 개인 */
    suspend fun getMonthSchedules(
        monthStart: Long,
        monthEnd: Long
    ): List<Schedule>

    suspend fun getDailySchedules(
        startDate: Long,
        endDate: Long
    ): List<Schedule>

    suspend fun addSchedule(
        schedule: Schedule
    )

    suspend fun editSchedule(
        schedule: Schedule
    )

    suspend fun deleteSchedule(
        localId: Long,
        serverId: Long
    )

    suspend fun uploadScheduleToServer()

    suspend fun postScheduleToServer(scheduleServerId: Long, scheduleId: Long)

    /** 모임 */
    suspend fun getMonthMoimSchedule(
        yearMonth: String
    ): List<GetMonthScheduleResult>
}