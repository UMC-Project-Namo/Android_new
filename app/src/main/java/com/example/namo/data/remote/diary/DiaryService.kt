package com.example.namo.data.remote.diary

import android.annotation.SuppressLint
import com.example.namo.config.ApplicationClass
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DiaryService {
    private val diaryRetrofitInterface: DiaryInterface =
        ApplicationClass.sRetrofit.create(DiaryInterface::class.java)

    private lateinit var diaryView: DiaryView
    private lateinit var diaryDetailView: DiaryDetailView
    private lateinit var getMonthDiaryView: GetMonthDiaryView
    private lateinit var addGroupDiaryView: AddGroupDiaryView
    private lateinit var getGroupDiaryView: GetGroupDiaryView

    fun addDiaryView(diaryView: DiaryView) {
        this.diaryView = diaryView
    }

    fun setDiaryView(diaryDetailView: DiaryDetailView) {
        this.diaryDetailView = diaryDetailView
    }

    fun getMonthDiaryView(getMonthDiaryView: GetMonthDiaryView) {
        this.getMonthDiaryView = getMonthDiaryView
    }

    fun addGroupDiaryView(addGroupView: AddGroupDiaryView) {
        this.addGroupDiaryView = addGroupView
    }

    fun getGroupDiaryView(getGroupDiaryView: GetGroupDiaryView) {
        this.getGroupDiaryView = getGroupDiaryView
    }

    /** 기록 추가 **/
    fun addDiary(
        localId: Long,
        images: List<MultipartBody.Part>?,
        content: RequestBody?,
        scheduleIdx: RequestBody
    ) {
        diaryRetrofitInterface.addDiary(scheduleIdx, content, images)
            .enqueue(object : Callback<DiaryResponse.DiaryAddResponse> {

                override fun onResponse(
                    call: Call<DiaryResponse.DiaryAddResponse>,
                    response: Response<DiaryResponse.DiaryAddResponse>
                ) {
                    val resp: DiaryResponse.DiaryAddResponse? = response.body()
                    when (response.code()) {

                        200 -> if (resp != null) {
                            diaryView.onAddDiarySuccess(
                                resp, localId
                            )
                        }
                        else -> diaryView.onAddDiaryFailure(response.toString())
                    }
                }

                override fun onFailure(call: Call<DiaryResponse.DiaryAddResponse>, t: Throwable) {
                    diaryView.onAddDiaryFailure(t.message.toString())
                }
            })
    }


    /** 기록 수정 **/
    fun editDiary(
        localId: Long,
        serverId: Long,
        images: List<MultipartBody.Part>?,
        content: RequestBody?,
        scheduleIdx: RequestBody
    ) {
        diaryRetrofitInterface.editDiary(scheduleIdx, content, images)
            .enqueue(object : Callback<DiaryResponse.DiaryEditResponse> {

                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    call: Call<DiaryResponse.DiaryEditResponse>,
                    response: Response<DiaryResponse.DiaryEditResponse>
                ) {
                    val resp: DiaryResponse.DiaryEditResponse? = response.body()
                    when (response.code()) {
                        200 -> if (resp != null) {
                            diaryDetailView.onEditDiarySuccess(
                                resp,
                                localId, serverId
                            )
                        }
                        else -> diaryDetailView.onEditDiaryFailure(response.toString())

                    }
                }

                override fun onFailure(call: Call<DiaryResponse.DiaryEditResponse>, t: Throwable) {
                    diaryDetailView.onEditDiaryFailure(t.message.toString())
                }
            })
    }


    /** 기록 삭제 **/
    fun deleteDiary(
        localId: Long,
        scheduleIdx: Long
    ) {
        diaryRetrofitInterface.deleteDiary(scheduleIdx)
            .enqueue(object : Callback<DiaryResponse.DiaryDeleteResponse> {

                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    call: Call<DiaryResponse.DiaryDeleteResponse>,
                    response: Response<DiaryResponse.DiaryDeleteResponse>
                ) {
                    val resp: DiaryResponse.DiaryDeleteResponse? = response.body()
                    when (response.code()) {
                        200 -> if (resp != null) {
                            diaryDetailView.onDeleteDiarySuccess(
                                resp, localId
                            )
                        }
                        else -> diaryDetailView.onDeleteDiaryFailure(
                            response.code().toString()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<DiaryResponse.DiaryDeleteResponse>,
                    t: Throwable
                ) {
                    diaryDetailView.onDeleteDiaryFailure(t.message.toString())
                }
            })
    }


    /** 기록 월 별 조회 **/
    fun getAllDiary() {
        diaryRetrofitInterface.getAllDiary()
            .enqueue(object : Callback<DiaryResponse.DiaryGetAllResponse> {

                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    call: Call<DiaryResponse.DiaryGetAllResponse>,
                    response: Response<DiaryResponse.DiaryGetAllResponse>
                ) {
                    val resp: DiaryResponse.DiaryGetAllResponse? = response.body()
                    when (response.code()) {
                        200 -> if (resp != null) {
                            getMonthDiaryView.onGetMonthDiarySuccess(
                                resp
                            )
                        }
                        else -> getMonthDiaryView.onGetMonthDiaryFailure(
                            response.toString()
                        )
                    }

                }

                override fun onFailure(
                    call: Call<DiaryResponse.DiaryGetAllResponse>,
                    t: Throwable
                ) {
                    getMonthDiaryView.onGetMonthDiaryFailure(
                        t.message.toString()
                    )
                }
            })
    }


    /** 그룹 메모 추가 **/
    fun addGroupDiary(
        moimScheduleIdx: Long,
        name: RequestBody,
        money: RequestBody,
        members: RequestBody?,
        imgs: List<MultipartBody.Part>?
    ) {
        diaryRetrofitInterface.addGroupDiary(moimScheduleIdx, name, money, members, imgs)
            .enqueue(object : Callback<DiaryResponse.AddGroupDiaryResponse> {

                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    call: Call<DiaryResponse.AddGroupDiaryResponse>,
                    response: Response<DiaryResponse.AddGroupDiaryResponse>
                ) {
                    val resp: DiaryResponse.AddGroupDiaryResponse? = response.body()
                    when (response.code()) {
                        200 -> if (resp != null) {
                            addGroupDiaryView.onAddGroupDiarySuccess(resp)
                        }
                        else -> addGroupDiaryView.onAddGroupDiaryFailure(response.toString())
                    }

                }

                override fun onFailure(
                    call: Call<DiaryResponse.AddGroupDiaryResponse>,
                    t: Throwable
                ) {
                    addGroupDiaryView.onAddGroupDiaryFailure(t.message.toString())
                }
            })
    }


    /** 그룹 기록 일 별 조회 **/
    fun getGroupDiary(
        moimScheduleIdx: Long
    ) {
        diaryRetrofitInterface.getGroupDiary(moimScheduleIdx)
            .enqueue(object : Callback<DiaryResponse.GetGroupDiaryResponse> {

                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    call: Call<DiaryResponse.GetGroupDiaryResponse>,
                    response: Response<DiaryResponse.GetGroupDiaryResponse>
                ) {
                    val resp: DiaryResponse.GetGroupDiaryResponse? = response.body()
                    when (response.code()) {
                        200 -> if (resp != null) {
                            getGroupDiaryView.onGetGroupDiarySuccess(resp)
                        }
                        else ->
                            getGroupDiaryView.onGetGroupDiaryFailure(response.toString())
                    }

                }

                override fun onFailure(
                    call: Call<DiaryResponse.GetGroupDiaryResponse>,
                    t: Throwable
                ) {
                    getGroupDiaryView.onGetGroupDiaryFailure(t.message.toString())
                }
            })
    }


}



