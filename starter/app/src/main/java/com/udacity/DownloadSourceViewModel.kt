package com.udacity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class DownloadSourceViewModel : ViewModel() {

    private val _downloadOption = MutableLiveData(DownloadOption.NONE)
    val downloadOption: DownloadOption
        get() = _downloadOption.value ?: DownloadOption.NONE

    val hasChosenDownloadOption: LiveData<Boolean> = Transformations.map(_downloadOption) {
        it?.takeUnless { it == DownloadOption.NONE }?.let { true } ?: false
    }

    fun setDownloadOption(downloadOption: DownloadOption) {
        _downloadOption.postValue(downloadOption)
    }

}