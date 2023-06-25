package com.example.namo.ui.bottom.diary.mainDiary

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.namo.databinding.DialogImageFullBinding

class ImageDialog(val image:String)
    : DialogFragment(), View.OnClickListener{

    lateinit var binding: DialogImageFullBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogImageFullBinding.inflate(inflater, container, false)

        Glide.with(this)
            .load(image.toUri())
            .into(binding.galleryImgIv)

        initView()

        return binding.root
    }

    private fun initView(){

        binding.apply {

            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))  //배경 투명하게
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)  //dialog 모서리 둥글게

        }
    }


    /** 다이얼로그 화면 외 클릭시 사라짐 **/
    override fun onClick(p0: View?) {
        dismiss()
    }
}
