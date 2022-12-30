package com.example.namo.bottom.home.calendar

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import org.joda.time.DateTime

class CalendarAdapter(fm : FragmentActivity) : FragmentStateAdapter(fm) {

    private var start : Long = DateTime().withTimeAtStartOfDay().millis

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): CalendarMonthFragment {
        val millis = getItemId(position)
        return CalendarMonthFragment.newInstance(millis)
    }

    override fun getItemId(position: Int): Long
        = DateTime(start).plusMonths(position - START_POSITION).millis

    override fun containsItem(itemId: Long): Boolean {
        val date = DateTime(itemId)

        return date.dayOfMonth == 1 && date.millisOfDay == 0
    }

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }


}