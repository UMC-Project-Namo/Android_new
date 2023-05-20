package com.example.namo.ui.bottom.home.schedule.category.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.namo.databinding.ItemPaletteColorBinding

class CategoryPaletteRVAdapter(
    val context: Context,
    private val colorList: ArrayList<Int>,
    initColor: Int,
    selectedPalettePosition: Int
    ): RecyclerView.Adapter<CategoryPaletteRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(position: Int, selectedColor: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    private var currentSelectPosition = if (colorList[selectedPalettePosition] == initColor) selectedPalettePosition else -1
    private var previousSelectPosition = currentSelectPosition

    fun setColorClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPaletteColorBinding = ItemPaletteColorBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)

        Log.d("bori", "최초 initColor: $currentSelectPosition")

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(colorList[position])
        holder.apply {

            itemView.setOnClickListener {
                currentSelectPosition = position.toInt()
                // 팔레트 색상 내에서 단일 선택
                if (previousSelectPosition != -1) {
                    Log.d("shine", "currentPosition: ${currentSelectPosition}, prevPosition: $previousSelectPosition")
                    holder.bind(colorList[position])
                    notifyItemChanged(previousSelectPosition)
                }

                // 클릭한 아이템 체크 표시
                binding.itemPaletteSelectIv.visibility = View.VISIBLE
                mItemClickListener.onItemClick(position, colorList[position])

                previousSelectPosition = currentSelectPosition
                Log.d("CategoryPaletteRVA", "selectedPosition: $position")
            }
        }
    }

    override fun getItemCount(): Int = colorList.size

    inner class ViewHolder(val binding: ItemPaletteColorBinding): RecyclerView.ViewHolder(binding.root) {
        val selectIv = binding.itemPaletteSelectIv

        fun bind(color : Int) {
            // 카드뷰에 색 넣어주기
            binding.itemPaletteColorCv.background.setTint(context.resources.getColor(color))
            // 체크 표시 초기화
            if (adapterPosition == currentSelectPosition) selectIv.visibility = View.VISIBLE
            else selectIv.visibility = View.GONE
        }
    }
}