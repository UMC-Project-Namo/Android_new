package com.mongmong.namo.presentation.ui.community.moim.diary

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mongmong.namo.databinding.DialogActivityPaymentBinding
import com.mongmong.namo.domain.model.ActivityPayment
import com.mongmong.namo.presentation.ui.community.moim.diary.adapter.ActivityPaymentsRVAdapter
import kotlinx.coroutines.flow.filter
import java.math.BigDecimal
import java.text.NumberFormat

class ActivityPaymentDialog(private val position: Int) : DialogFragment() {
    lateinit var binding: DialogActivityPaymentBinding
    private lateinit var participantsAdapter: ActivityPaymentsRVAdapter
    private val viewModel: MoimDiaryViewModel by activityViewModels()

    // 편집 중인 데이터를 저장할 복사본
    private var tempPayment: ActivityPayment? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogActivityPaymentBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        viewModel.activities.value?.get(position)?.let { activity ->
            tempPayment = activity.payment.copy(participants = activity.payment.participants.map { it.copy() })
        } ?: run {
            tempPayment = ActivityPayment(participants = emptyList())  // 기본값
        }
        binding.viewModel = viewModel
        binding.payment = tempPayment

        initRecyclerView()
        initObserve()
        initEventListener()

        return binding.root
    }

    // RecyclerView 초기화
    private fun initRecyclerView() {
        participantsAdapter = ActivityPaymentsRVAdapter(
            participants = tempPayment?.participants ?: emptyList(),
            onCheckedChanged = { updateParticipantsCount() },
            hasDiary = viewModel.diarySchedule.value?.hasDiary ?: false,
            isEdit = viewModel.isEditMode.value ?: false
        )

        binding.activityParticipantsRv.apply {
            adapter = participantsAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun initEventListener() {
        // TextWatcher 설정
        binding.activityPaymentTotalEt.addTextChangedListener(object : TextWatcher {
            private var currentText: String = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == currentText) return

                s?.let {
                    val cleanString = it.toString().replace("[^\\d]".toRegex(), "")
                    val parsed = cleanString.toLongOrNull() ?: 0L

                    if (parsed == 0L) {
                        currentText = ""
                        binding.activityPaymentTotalEt.setText("")
                        binding.activityPaymentTotalEt.setSelection(0)
                        tempPayment?.totalAmount = BigDecimal.ZERO
                        binding.activityPaymentResultTv.text = "0 원"
                    } else {
                        val formatted = formatAmount(parsed)
                        currentText = formatted

                        // 텍스트 변경 리스너를 일시적으로 제거하고 설정해야 함
                        binding.activityPaymentTotalEt.removeTextChangedListener(this)
                        binding.activityPaymentTotalEt.setText(formatted)
                        binding.activityPaymentTotalEt.setSelection(formatted.length - 2)
                        binding.activityPaymentTotalEt.addTextChangedListener(this)

                        tempPayment?.totalAmount = parsed.toBigDecimal()
                        updatePerPersonAmount()
                    }
                }
            }
        })

        binding.activityPaymentBackTv.setOnClickListener { dismiss() }
        binding.activityPaymentSaveTv.setOnClickListener { savePaymentData() }
    }

    private fun initObserve() {
        lifecycleScope.launchWhenStarted {
            viewModel.editActivityPaymentResult
                .collect { response ->
                    if (response?.isSuccess == true) {
                        tempPayment?.let { viewModel.updateActivityPayment(position, it) }
                        viewModel.setTotalMoimPayment()
                        dismiss()
                    } else {
                        Toast.makeText(requireActivity(), "${response?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    // 참가자 수 업데이트
    private fun updateParticipantsCount() {
        tempPayment?.divisionCount = participantsAdapter.getSelectedParticipantsCount()
        binding.activityPaymentCountTv.text = "${tempPayment?.divisionCount} 명"
        updatePerPersonAmount()
    }

    // 금액 포맷팅
    private fun formatAmount(amount: Long): String {
        return NumberFormat.getInstance().format(amount) + " 원"
    }

    // 인당 금액 업데이트
    private fun updatePerPersonAmount() {
        val selectedCount = tempPayment?.divisionCount ?: 0
        val totalAmount = tempPayment?.totalAmount ?: BigDecimal.ZERO

        if (selectedCount > 0) {
            val amountPerPerson = totalAmount / BigDecimal(selectedCount)
            binding.activityPaymentResultTv.text = formatAmount(amountPerPerson.toLong())
            tempPayment?.amountPerPerson = amountPerPerson
        } else {
            binding.activityPaymentResultTv.text = "0 원"
            tempPayment?.amountPerPerson = BigDecimal.ZERO
        }
    }

    private fun savePaymentData() {
        tempPayment?.let {
            it.participants = participantsAdapter.getUpdatedParticipants()

            val activityId = viewModel.activities.value?.get(position)?.activityId

            Log.d("savePaymentData", "${it}")

            if (activityId == 0L) {
                viewModel.updateActivityPayment(position, it)
                viewModel.setTotalMoimPayment()
                dismiss()
            }
            else viewModel.editActivityPayment(activityId!!, it)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
