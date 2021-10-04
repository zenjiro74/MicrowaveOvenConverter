package com.example.zenjiro74.microwaveconverter.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val beforeW: String,
    val beforeTime: String,
    val afterW: String,
    val afterTime: String,
    val memo: String
)
