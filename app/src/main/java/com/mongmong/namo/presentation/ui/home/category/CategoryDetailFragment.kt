package com.mongmong.namo.presentation.ui.home.category

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.mongmong.namo.R
import com.mongmong.namo.databinding.FragmentCategoryDetailBinding
import com.mongmong.namo.domain.model.CategoryModel
import com.mongmong.namo.presentation.config.BaseFragment
import com.mongmong.namo.presentation.enums.CategoryColor
import com.mongmong.namo.presentation.enums.SuccessType
import com.mongmong.namo.presentation.ui.home.category.CategorySettingFragment.Companion.CATEGORY_DATA
import com.mongmong.namo.presentation.ui.home.category.adapter.CategoryPaletteRVAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryDetailFragment(private val isEditMode: Boolean)
    : BaseFragment<FragmentCategoryDetailBinding>(R.layout.fragment_category_detail) {

    private lateinit var paletteAdapter: CategoryPaletteRVAdapter

    private val viewModel : CategoryViewModel by viewModels()

    override fun setup() {
        binding.viewModel = this@CategoryDetailFragment.viewModel

        setInit()
        switchToggle()
        onClickListener()

        initObservers()

        if (viewModel.color.value == null) {
            initPaletteColorRv(CategoryColor.NAMO_ORANGE)
        } else {
            initPaletteColorRv(viewModel.color.value!!)
        }
    }

    private fun onClickListener() {
        with(binding) {
            // 뒤로가기
            categoryDetailBackIv.setOnClickListener {
                // 편집 모드라면 CategoryEditActivity 위에 Fragment 씌어짐
                moveToSettingFrag(isEditMode)
            }

            // 저장하기
            categoryDetailSaveTv.setOnClickListener {
                if (!this@CategoryDetailFragment.viewModel.isValidInput()) {
                    Toast.makeText(requireContext(), "카테고리를 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                // 수정 모드 -> 카테고리 update
                if (isEditMode) {
                    updateData()
                }
                // 생성 모드 -> 카테고리 insert
                else {
                    insertData()
                }
            }
        }
    }

    private fun setInit() {
        // 편집 모드
        if (isEditMode) {
            // 이전 화면에서 저장한 spf 받아오기
            loadPref()
            return
        }
        viewModel.setCategory(CategoryModel(isShare = true))
    }

    private fun initObservers() {
        viewModel.isComplete.observe(requireActivity()) { isComplete ->
            // 추가 작업이 완료된 후 뒤로가기
            if (isComplete) {
                viewModel.completeState.observe(viewLifecycleOwner) { state ->
                    when(state) {
                        SuccessType.ADD -> Toast.makeText(requireContext(), "카테고리가 생성되었습니다.", Toast.LENGTH_SHORT).show()
                        SuccessType.EDIT -> Toast.makeText(requireContext(), "카테고리가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                        else -> {}
                    }
                }
                moveToSettingFrag(isEditMode)
            }
        }
    }

    /** 카테고리 추가 */
    private fun insertData() {
        // 새 카테고리 등록
        viewModel.addCategory()
    }

    private fun updateData() {
        // 카테고리 편집
        viewModel.editCategory()
    }

    private fun initPaletteColorRv(initCategory: CategoryColor) {
        if (viewModel.selectedPalettePosition.value == null) viewModel.updateSelectedPalettePosition(0)

        // 어댑터 연결
        paletteAdapter = CategoryPaletteRVAdapter(requireContext(), CategoryColor.getAllColors(), initCategory, viewModel.selectedPalettePosition.value!!)
        binding.categoryPaletteRv.apply {
            adapter = paletteAdapter
            layoutManager = GridLayoutManager(context, 5)
        }
        // 아이템 클릭
        paletteAdapter.setColorClickListener(object: CategoryPaletteRVAdapter.MyItemClickListener {
            override fun onItemClick(position: Int, selectedColor: CategoryColor) {
                // 색상값 세팅
                viewModel.updateCategoryColor(selectedColor)
                // notifyItemChanged()에서 인자로 넘겨주기 위함
                viewModel.updateSelectedPalettePosition(position)
            }
        })
    }

    private fun switchToggle() {
        val isShare = viewModel.category.value!!.isShare
        binding.categoryToggleIv.apply {
            // 첫 진입 시 토글 이미지 세팅
            isChecked = isShare
            // 토글 클릭 시 이미지 세팅
            setOnClickListener {
                (it as SwitchCompat).isChecked = !isShare
                viewModel.updateIsShare(!isShare)
            }
        }
    }

    private fun loadPref() {
        val spf = requireActivity().getSharedPreferences(CategorySettingFragment.CATEGORY_KEY_PREFS, Context.MODE_PRIVATE)

        if (spf.contains(CATEGORY_DATA)) {
            val gson = Gson()
            val json = spf.getString(CATEGORY_DATA, "")
            try {
                // 데이터에 타입을 부여하기 위한 typeToken
                val typeToken = object : TypeToken<CategoryModel>() {}.type
                // 데이터 받기
                val data: CategoryModel = gson.fromJson(json, typeToken)
                viewModel.setCategory(data)
            } catch (e: JsonParseException) { // 파싱이 안 될 경우
                e.printStackTrace()
            }
            Log.d("debug", "Category Data loaded")
        }
    }

    private fun moveToSettingFrag(isEditMode: Boolean) {
        if (isEditMode) { // 편집 모드에서의 화면 이동
            Intent(requireActivity(), CategoryActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }.also {
                startActivity(it)
            }
            activity?.finish()
        } else { // 생성 모드에서의 화면 이동
            requireActivity().supportFragmentManager
                .popBackStack() // 뒤로가기
        }
    }
}