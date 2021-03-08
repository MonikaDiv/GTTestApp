package com.example.gttest.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gttest.gtenums.FailureReasaonEnum
import com.example.gttest.gtenums.SuccessReasaonEnum
import java.math.BigDecimal

class DataViewModel(val mApplication: Application) : AndroidViewModel(mApplication) {

    private val _dataLiveData = MutableLiveData<String>()
    val dataLiveData: LiveData<String> = _dataLiveData

    fun isValidaData(enteredValue: String) {
        var message: String = ""
        var value = enteredValue.toBigDecimal()
        var valueinUSD: BigDecimal = calculateHKDtoUSD(value)

        if (valueinUSD > 10000.toBigDecimal()) {
            message = FailureReasaonEnum.GT_GREATER.value
        } else if (value <= 0.toBigDecimal()) {
            message = FailureReasaonEnum.GT_LESSTHANZERO.value
        } else if(valueinUSD <10000.toBigDecimal() && value > 0.toBigDecimal())
        {
            message ="Send USD $valueinUSD to {first name in contact} with phone number: {phone number}"
        }

        _dataLiveData.postValue(message)

    }

    private fun calculateHKDtoUSD(value: BigDecimal): BigDecimal {

        return value / (7.8).toBigDecimal()
    }

}