package com.example.namo.ui.bottom.diary

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.namo.R
import com.example.namo.data.NamoDatabase
import com.example.namo.data.entity.home.Event
import com.example.namo.databinding.FragmentDiaryBinding
import com.example.namo.ui.bottom.diary.adapter.DiaryMultiAdapter
import com.example.namo.ui.bottom.diary.adapter.TaskListItem
import org.joda.time.DateTime
import java.lang.Boolean.TRUE
import java.text.ParseException
import java.text.SimpleDateFormat

class DiaryFragment: Fragment() {

    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!

    private var dateTime = DateTime().withDayOfMonth(1).withTimeAtStartOfDay().millis
    private var datelist=listOf<Long>()
    private var diarylist= listOf<Event>()

    lateinit var diarydateAdapter: DiaryMultiAdapter
    private lateinit var yearMonth:String
    private lateinit var day:String
    private lateinit var db: NamoDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDiaryBinding.inflate(inflater, container, false)

        db=NamoDatabase.getInstance(requireContext())

        binding.diaryMonth.text=DateTime(dateTime).toString("yyyy.MM")
        yearMonth=binding.diaryMonth.text.toString()

        getList()
        binding.diaryMonth.setOnClickListener {
            dialogCreate()
        }

        return binding.root
    }

    /** string 형식 날짜를 long 타입으로 변환 **/
    @SuppressLint("SimpleDateFormat")
    fun dateTimeToMillSec(dateTime: String): Long{
        var timeInMilliseconds: Long = 0
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        try {
            val mDate = sdf.parse(dateTime)
            if (mDate != null) {
                timeInMilliseconds = mDate.time
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return timeInMilliseconds
    }

    /** 같은 날짜끼리 묶어서 그룹 헤더로 추가 **/
    private fun List<Event>.toListItems(): List<TaskListItem> {
        val result = arrayListOf<TaskListItem>() // 결과를 리턴할 리스트

        var groupHeaderDate : Long=0 // 그룹날짜
        this.forEach { task ->
            // 날짜가 달라지면 그룹 헤더를 추가
            if (groupHeaderDate != task.startLong) {
                result.add(TaskListItem.Header(task))
            }
            //  task 추가
            result.add(TaskListItem.Item(task))

            // 그룹 날짜를 바로 이전 날짜로 설정
            groupHeaderDate = task.startLong
        }

        return result
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getList(){
        val r = Runnable {
            try {
                getDayInMonth()
                day="$yearMonth.32"

                val nextMonth=dateTimeToMillSec(day)
                val startMonth=dateTimeToMillSec( "$yearMonth.01")

                datelist = db.diaryDao.getDateList(startMonth,nextMonth,TRUE)
                diarylist = db.diaryDao.getDiaryList(startMonth,nextMonth,TRUE)

                val month = diarylist.toListItems()

                diarydateAdapter= DiaryMultiAdapter(requireContext(), month as ArrayList<TaskListItem>)

                // 수정 버튼 클릭리스너
                diarydateAdapter.setRecordClickListener(object :DiaryMultiAdapter.DiaryEditInterface{
                    override fun onEditClicked(allData: TaskListItem) {
                        val bundle=Bundle()
                        bundle.putInt("scheduleIdx", allData.task.eventId.toInt())

                        val editFrag=DiaryModifyFragment()
                        editFrag.arguments=bundle
                        view?.findNavController()?.navigate(R.id.action_diaryFragment_to_diaryModifyFragment,bundle)
                    }
                })

                requireActivity().runOnUiThread {

                    // 달 별 메모 없으면 없다고 띄우기
                    if (datelist.isNotEmpty()){
                        binding.diaryListRv.visibility=View.VISIBLE }
                    else{ binding.diaryListRv.visibility=View.GONE
                            binding.diaryListEmptyTv.visibility=View.VISIBLE}

                    binding.diaryListRv.apply {
                        adapter=diarydateAdapter
                        layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        setHasFixedSize(TRUE)
                        (adapter as DiaryMultiAdapter).notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                Log.d("tag", "Error - $e")
            }
        }
        val thread = Thread(r)
        thread.start()

    }

    /** 월별 일수 계산 **/
    @SuppressLint("SimpleDateFormat")
    private fun getDayInMonth(){

        val year:String = SimpleDateFormat("yyyy").format(dateTime)

        if(yearMonth=="$year.04" ||yearMonth=="$year.06" ||yearMonth=="$year.09" ||yearMonth=="$year.11")
        { day="$yearMonth.31"}
        if(yearMonth=="$year.02") {
            day = "$yearMonth.29"
            if (year.toInt() % 4 == 0 && year.toInt() % 100 != 0 || year.toInt() % 400 == 0) {
                day = "$yearMonth.30"
            }
        }
    }

    /** 다이얼로그 띄우기 **/
    private fun dialogCreate() {

        YearMonthDialog(dateTime){
            yearMonth= DateTime(it).toString("yyyy.MM")
            binding.diaryMonth.text=yearMonth
            getList()
        }.show(parentFragmentManager,"test")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
