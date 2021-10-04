package com.example.zenjiro74.microwaveconverter.ui.main

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenjiro74.microwaveconverter.data.History
import com.example.zenjiro74.microwaveconverter.HistoryDatabase
import com.example.zenjiro74.microwaveconverter.data.HistoryRepository
import com.example.zenjiro74.microwaveconverter.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var _result: MutableLiveData<String> = MutableLiveData()
    val result = _result
    private var _memo: MutableLiveData<String> = MutableLiveData()
    val memo = _memo

    private var minVal = 0
    private var secVal = 0
    private var _beforeIdx = MutableLiveData(0)
    private var _afterIdx = MutableLiveData(1)

    val beforeIdx = _beforeIdx
    val afterIdx = _afterIdx

    val wattageArr = arrayListOf(
        500, 600, 700, 800, 900, 1000, 1200, 1500
    )

    val timeMinutesArr = arrayListOf(
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    )

    val timeSecondsArr = arrayListOf(
        0, 10, 20, 30, 40, 50
    )

    fun onClickSave(view: View) {
        viewModelScope.launch(Dispatchers.IO) {
            val database = HistoryDatabase.getDatabase(view.context)
            val historyRepo = HistoryRepository(database.historyDao())
            historyRepo.insert(
                History(
                    id = 0,
                    beforeW = "${wattageArr[_beforeIdx.value!!]}W",
                    beforeTime = "${minVal}分${secVal}秒",
                    afterW = "${wattageArr[_afterIdx.value!!]}W",
                    afterTime = _result.value!!,
                    memo = _memo.value ?: "",
                )
            )
        }
    }

    fun onClickLoad(view: View) {

        viewModelScope.launch(Dispatchers.IO) {
            val database = HistoryDatabase.getDatabase(view.context)
            val historyRepo = HistoryRepository(database.historyDao())
            for (d in historyRepo.data) {
                launch(Dispatchers.Main) {
                    val text =
                        "id:${d.id}, before[${d.beforeTime} x ${d.beforeW}]\nafter[${d.afterTime} x ${d.afterW}]\n${d.memo}"
                    Toast.makeText(view.context, text, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onItemSelectedWattage(parent: AdapterView<*>, pos: Int) {
        when (parent.id) {
            R.id.spnBefore -> {
                _beforeIdx.value = pos
                calcTimer()
            }
            R.id.spnAfter -> {
                _afterIdx.value = pos
                calcTimer()
            }
        }
    }

    fun onItemSelectedTimer(parent: AdapterView<*>, pos: Int) {
        when (parent.id) {
            R.id.spnMinutes -> {
                minVal = timeMinutesArr[pos]
                calcTimer()

            }
            R.id.spnSeconds -> {
                secVal = timeSecondsArr[pos]
                calcTimer()
            }
        }
    }

    private fun calcTimer() {
        val tempBefore = minVal * 60 + secVal
        val jule = wattageArr[beforeIdx.value!!] * tempBefore

        val tempAfter = jule / wattageArr[afterIdx.value!!]
        val tempMin = tempAfter / 60
        val tempSec = tempAfter % 60

        _result.value = "${tempMin}分${tempSec}秒"
    }
}