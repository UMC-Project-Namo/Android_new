package com.example.namo.bottom.grouplist

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import com.example.namo.databinding.DialogCreateGroupBinding

class CreateGroupDialog(
    context: Context,
    private val okCallback: (String) -> Unit,
) : Dialog(context) { // 뷰를 띄워야하므로 Dialog 클래스는 context를 인자로 받는다.

    private lateinit var binding: DialogCreateGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 만들어놓은 dialog_profile.xml 뷰를 띄운다.
        binding = DialogCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        // 뒤로가기 버튼, 빈 화면 터치를 통해 dialog가 사라지지 않도록
        setCancelable(false)

        // background를 투명하게 만듦
        // (중요) Dialog는 내부적으로 뒤에 흰 사각형 배경이 존재하므로, 배경을 투명하게 만들지 않으면
        // corner radius의 적용이 보이지 않는다.
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Button 클릭에 대한 Callback 처리. 이 부분은 상황에 따라 자유롭게!
        createGroupBackTv.setOnClickListener {
            dismiss()
        }
        createGroupSaveTv.setOnClickListener {
            if (createGroupTitleEt.text.isNullOrBlank()) {
                Toast.makeText(context, "이름을 입력하세요!", Toast.LENGTH_SHORT).show()
            } else {
                okCallback(createGroupTitleEt.text.toString())
                dismiss()
            }
        }
    }
}