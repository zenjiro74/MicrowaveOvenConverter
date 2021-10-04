package com.example.zenjiro74.microwaveconverter.data

import com.example.zenjiro74.microwaveconverter.dao.HistoryDao

class HistoryRepository(private val historyDao : HistoryDao) {
    val data = historyDao.getAll()

    suspend fun insert(history: History) {
        historyDao.insert(history)
    }
}