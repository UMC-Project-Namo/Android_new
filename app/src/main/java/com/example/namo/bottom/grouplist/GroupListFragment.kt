package com.example.namo.bottom.grouplist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.namo.R
import com.example.namo.databinding.FragmentGroupListBinding


class GroupListFragment : Fragment() {

    lateinit var binding: FragmentGroupListBinding

    private var groupDatas = ArrayList<Group>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentGroupListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // 어댑터와 데이터리스트 연결
        val groupRVAdapter = GroupListRVAdapter(groupDatas)
        binding.groupListRv.adapter = groupRVAdapter
        binding.groupListRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        groupRVAdapter.setMyItemClickListener(object : GroupListRVAdapter.ItemClickListener {
            override fun onItemClick(group: Group) {
                Log.d("CLICK", "click item")
//                (context as MainActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_frm, GroupCalendarWrapperFragment(group))
//                    .commitAllowingStateLoss()
            }
        })

        // 데이터 리스트 생성 더미 데이터
        groupDatas.apply {
            add(Group("나모 앱 런칭 캘린더", R.drawable.app_logo_namo, 7, "코코아, 유즈, 지니, 앨리, 라나, 매실, 얼리시"))
            add(Group("나모 안드 캘린더", R.color.notyetGray, 3, "보리, 앨리, 지니, 코코아"))
            add(Group("가족 캘린더", R.color.notyetGray, 4, "엄마, 아빠, 오빠, 나"))
        }

    }
}