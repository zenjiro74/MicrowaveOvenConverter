package com.example.zenjiro74.microwaveconverter.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.zenjiro74.microwaveconverter.data.History

@Dao
interface HistoryDao {
    @Insert
    fun insert(history: History)

    @Query("select * from history")
    fun getAll(): List<History>

    @Delete
    fun delete(history: History)
}