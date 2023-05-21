package com.example.namo.ui.bottom.home.category

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.namo.R
import com.example.namo.data.NamoDatabase
import com.example.namo.databinding.ActivityCategoryEditBinding
import com.example.namo.data.entity.home.Category

class CategoryEditActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryEditBinding

    private lateinit var db: NamoDatabase
    private lateinit var category: Category

    var categoryIdx = -1
    var name = ""
    var color = 0
    var share = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NamoDatabase.getInstance(this)

        onClickListener()

        supportFragmentManager.beginTransaction()
            .replace(R.id.category_edit_frm, CategoryDetailFragment(true))
            .commitAllowingStateLoss()
    }

    private fun onClickListener() {

        // 다크뷰 클릭 시 화면 종료
//        binding.categoryDarkView.setOnClickListener {
//            finish()
//        }

        // 카테고리 삭제 진행
        binding.categoryDeleteIv.setOnClickListener {
            Log.d("CategoryEditActivity", "카테고리삭제 클릭")
            deleteCategory()
        }
    }

    private fun deleteCategory() {
        val spf = getSharedPreferences(CategorySettingFragment.CATEGORY_KEY_PREFS, Context.MODE_PRIVATE)
        categoryIdx = spf.getInt(CategorySettingFragment.CATEGORY_KEY_IDX, -1)

        if (categoryIdx == 1 || categoryIdx == 2) {
            Toast.makeText(this, "기본 카테고리는 삭제할 수 없습니다", Toast.LENGTH_SHORT).show()
        } else {
            Thread{
                category = db.categoryDao.getCategoryContent(categoryIdx)
                db.categoryDao.deleteCategory(category)
                Log.d("CategoryEditActivity", "deleteCategory: $category")
            }.start()
            finish()
        }
    }

}