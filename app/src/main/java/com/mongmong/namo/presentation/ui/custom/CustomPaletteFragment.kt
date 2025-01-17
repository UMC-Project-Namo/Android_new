package com.mongmong.namo.presentation.ui.custom

import androidx.recyclerview.widget.LinearLayoutManager
import com.mongmong.namo.R
import com.mongmong.namo.domain.model.Palette
import com.mongmong.namo.databinding.FragmentCustomPaletteBinding
import com.mongmong.namo.presentation.config.BaseFragment
import com.mongmong.namo.presentation.enums.CategoryColor
import com.mongmong.namo.presentation.ui.custom.adapter.PaletteRVAdapter

class CustomPaletteFragment : BaseFragment<FragmentCustomPaletteBinding>(R.layout.fragment_custom_palette) {
    override fun setup() {
        // 팔레트 색 Arr 넣어주기
        val paletteDatas = arrayListOf(
            Palette("기본 팔레트", CategoryColor.getAllHexColors())
        )

        //어댑터 연결
        binding.customPaletteRv.apply {
            adapter = PaletteRVAdapter(requireContext()).build(paletteDatas)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}