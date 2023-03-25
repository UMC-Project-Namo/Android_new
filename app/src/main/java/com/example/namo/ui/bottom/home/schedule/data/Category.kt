package com.example.namo.ui.bottom.home.schedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="category_table")
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryIdx: Int,
    var name : String = "",
    var color : Int = 0,
    var share : Boolean = false
)
