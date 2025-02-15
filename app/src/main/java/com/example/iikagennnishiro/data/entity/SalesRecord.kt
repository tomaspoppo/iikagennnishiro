package com.example.iikagennnishiro.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales_records")
data class SalesRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val amount: Double
)
