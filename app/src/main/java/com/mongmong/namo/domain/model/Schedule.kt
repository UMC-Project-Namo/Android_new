package com.mongmong.namo.domain.model

import com.mongmong.namo.presentation.config.BaseResponse
import com.google.gson.annotations.SerializedName
import com.mongmong.namo.data.local.entity.home.Schedule
import com.mongmong.namo.presentation.config.RoomState
import com.mongmong.namo.presentation.config.UploadState

// 캘린더에 표시되는 색상 정보 (친구: 카테고리 정보, 참석자: 색상 & 이름)
data class CalendarColorInfo(
    val colorId: Int,
    val name: String
)

// 개인
/** 일정 월별 조회 */
data class GetMonthScheduleResponse (
    @SerializedName("result") val result : List<GetMonthScheduleResult>
) : BaseResponse()

data class GetMonthScheduleResult (
    @SerializedName("scheduleId") val scheduleId : Long,
    @SerializedName("name") val name : String,
    @SerializedName("startDate") val startDate : Long,
    @SerializedName("endDate") val endDate : Long,
    @SerializedName("alarmDate") val alarmDate : List<Int>,
    @SerializedName("interval") val interval : Int,
    @SerializedName("x") val placeX : Double,
    @SerializedName("y") val placeY : Double,
    @SerializedName("locationName") val placeName : String,
    @SerializedName("categoryId") val categoryId : Long,
    @SerializedName("hasDiary") val hasDiary : Boolean?,
    @SerializedName("moimSchedule") val moimSchedule : Boolean,
) {
    fun convertServerScheduleResponseToLocal(): Schedule {
        return Schedule(
            this.scheduleId, // localId
            this.name,
            this.startDate,
            this.endDate,
            this.interval,
            this.categoryId,
            this.placeName,
            this.placeX,
            this.placeY,
            0,
            this.alarmDate ?: listOf(),
            UploadState.IS_UPLOAD.state,
            RoomState.DEFAULT.state,
            this.scheduleId,
            this.categoryId,
            this.hasDiary,
            this.moimSchedule
        )
    }
}

/** 일정 생성 */
data class PostScheduleResponse (
    val result : PostScheduleResult
) : BaseResponse()

data class PostScheduleResult (
    @SerializedName("scheduleId") val scheduleId : Long
)

data class ScheduleRequestBody(
    var title: String = "",
    var categoryId: Long = 0L,
    var period: Period,
    var location: Location,
    var reminderTrigger: List<Int>? = listOf(), //TODO: String으로 변경
)

data class Period(
    var startDate: Long = 0L,
    var endDate: Long = 0L,
)

data class Location(
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var locationName: String = "없음",
    var kakaoLocationId: String? = ""
)

/** 일정 수정 */
data class EditScheduleResponse (
    @SerializedName("result") val result : EditScheduleResult
) : BaseResponse()

data class EditScheduleResult (
    @SerializedName("scheduleId")  val scheduleId : Long
)

/** 일정 삭제 */
data class DeleteScheduleResponse (
    @SerializedName("result") val result : String
) : BaseResponse()

// 모임
/** 모임 일정 카테고리 수정 */
data class PatchMoimScheduleCategoryRequestBody(
    @SerializedName("moimScheduleId") val moimScheduleId: Long,
    @SerializedName("categoryId") val categoryId : Long
)

/** 모임 일정 알림 리스트 수정 */
data class PatchMoimScheduleAlarmRequestBody(
    @SerializedName("moimScheduleId") val moimScheduleId: Long,
    @SerializedName("alarmDates") val alarmDates : List<Int>
)