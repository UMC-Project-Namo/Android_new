package com.example.namo.ui.bottom.group.calendar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.namo.data.entity.home.Event
import com.example.namo.R
import com.example.namo.data.entity.home.Category
import com.example.namo.data.remote.moim.MoimSchedule
import com.example.namo.databinding.ItemCalendarEventBinding
import org.joda.time.DateTime
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

class GroupDailyGroupRVAdapter() : RecyclerView.Adapter<GroupDailyGroupRVAdapter.ViewHolder>() {

    private val groupSchedule = ArrayList<MoimSchedule>()
    lateinit var colorArray: IntArray
    private lateinit var context : Context

    interface ContentClickListener {
        fun onContentClick(groupSchedule: MoimSchedule)
    }

    private lateinit var contentClickListener : ContentClickListener

    fun setContentClickListener(contentClickListener : ContentClickListener) {
        this.contentClickListener = contentClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType : Int) : ViewHolder {
        val binding : ItemCalendarEventBinding = ItemCalendarEventBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        context = viewGroup.context
        colorArray = context.resources.getIntArray(R.array.categoryColorArr)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        holder.bind(groupSchedule[position])

        holder.binding.itemCalendarEventContentLayout.setOnClickListener {
            contentClickListener.onContentClick(groupSchedule[position])
        }
    }

    override fun getItemCount(): Int = groupSchedule.size

    @SuppressLint("NotifyDataSetChanged")
    fun addGroupSchedule(personal : ArrayList<MoimSchedule>) {
        this.groupSchedule.clear()
        this.groupSchedule.addAll(personal)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding : ItemCalendarEventBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceType")
        fun bind(groupSchedule: MoimSchedule) {
            val time =
                DateTime(groupSchedule.startDate * 1000L).toString("HH:mm") + " - " + DateTime(groupSchedule.endDate * 1000L).toString(
                    "HH:mm"
                )
            val paletteId =
                if (groupSchedule.users.size < 2 && groupSchedule.users[0].color != 0) groupSchedule.users[0].color
                else 3

            binding.itemCalendarEventTitle.text = groupSchedule.name
            binding.itemCalendarEventTitle.isSelected = true
            binding.itemCalendarEventTime.text = time
            binding.itemCalendarEventColorView.background.setTint(colorArray[paletteId - 1])
        }
    }

}