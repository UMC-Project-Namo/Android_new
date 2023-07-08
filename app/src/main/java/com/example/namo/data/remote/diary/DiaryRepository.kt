package com.example.namo.data.remote.diary


import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import com.example.namo.data.dao.CategoryDao
import com.example.namo.data.dao.DiaryDao
import com.example.namo.data.entity.diary.Diary
import com.example.namo.data.entity.diary.DiaryEvent
import com.example.namo.data.entity.diary.DiaryItem
import com.example.namo.data.entity.home.Category
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

class DiaryRepository(
    private val diaryDao: DiaryDao,
    private val categoryDao: CategoryDao,
    private val diaryService: DiaryService,
    val context: Context
) {

    fun addDiary(scheduleId: Int, content: String, images: List<String>) {

        val diary = Diary(scheduleId, content, images)
        diaryDao.insertDiary(diary)

        val imageMultiPart = images.map { imgPath ->
            val file = File(absolutelyPath(imgPath.toUri(), context))
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imgs", file.name, requestFile)
        }

        val contentRequestBody = content.toRequestBody("text/plain".toMediaTypeOrNull())
        val scheduleIdRequestBody =
            scheduleId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        diaryService.addDiary(imageMultiPart, contentRequestBody, scheduleIdRequestBody)
    }

    // 이미지 절대 경로 변환
    @SuppressLint("Recycle")
    private fun absolutelyPath(path: Uri, context: Context): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context.contentResolver.query(path, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        val result = c?.getString(index!!)

        return result!!
    }


    fun editDiary(scheduleId: Int, content: String, images: List<String>) {

        val diary = Diary(scheduleId, content, images)
        diaryDao.updateDiary(diary)

        val imageMultiPart = images.map { imgPath ->
            val file = File(absolutelyPath(imgPath.toUri(), context))
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imgs", file.name, requestFile)
        }

        val contentRequestBody = content.toRequestBody("text/plain".toMediaTypeOrNull())
        val scheduleIdRequestBody =
            scheduleId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        diaryService.editDiary(imageMultiPart, contentRequestBody, scheduleIdRequestBody)
    }


    fun deleteDiary(scheduleId: Int, content: String, images: List<String>) {

        val diary = Diary(scheduleId, content, images)
        diaryDao.deleteDiary(diary)
        diaryService.deleteDiary(scheduleId)
    }


    fun getCategoryId(categoryId: Int): Category {
        return categoryDao.getCategoryContent(categoryId)
    }


    fun getDiaryDaily(scheduleId: Int): Diary {
        return diaryDao.getDiaryDaily(scheduleId)
    }

    fun getDiaryList(yearMonth: String): List<DiaryItem> {

//        val yearMonthSplit = yearMonth.split(".")
//        val year = yearMonthSplit[0]
//        val month = yearMonthSplit[1].removePrefix("0")
//        val formatYearMonth = "$year,$month"

        val diaryEvent = diaryDao.getDiaryEventList(yearMonth)


        return diaryEvent.toListItems()

    }

    fun getDiaryRetrofit(yearMonth: String){
        diaryService.getMonthDiary(yearMonth)
    }


    /** 같은 날짜끼리 묶어서 그룹 헤더로 추가 **/
    private fun List<DiaryEvent>.toListItems(): List<DiaryItem> {
        val result = arrayListOf<DiaryItem>() // 결과를 리턴할 리스트

        var groupHeaderDate: Long = 0 // 그룹날짜
        this.forEach { task ->
            // 날짜가 달라지면 그룹 헤더를 추가

            if (groupHeaderDate != task.startLong) {
                result.add(DiaryItem.Header(task.startLong))
            }
            //  task 추가

            val category = categoryDao.getCategoryContent(task.categoryIdx)

            result.add(
                DiaryItem.Content(

                    task.eventId,
                    task.title,
                    task.startLong,
                    task.endLong,
                    task.dayInterval,
                    category.color,
                    task.categoryName,
                    task.categoryIdx,
                    task.placeName,
                    task.placeX,
                    task.placeY,
                    task.placeId,
                    task.order,
                    task.alarmList,
                    task.hasDiary,
                    task.content,
                    task.images
                )
            )

            // 그룹 날짜를 바로 이전 날짜로 설정
            groupHeaderDate = task.startLong
        }

        return result
    }


    fun updateHasDiary(hasDiary: Boolean, scheduleIdx: Int){
        diaryDao.updateHasDiary(hasDiary, scheduleIdx)
    }

    fun deleteHasDiary(hasDiary: Boolean, scheduleIdx: Int){
        diaryDao.deleteHasDiary(hasDiary,scheduleIdx)
    }

}


