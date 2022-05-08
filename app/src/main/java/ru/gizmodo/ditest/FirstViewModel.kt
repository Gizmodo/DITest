package ru.gizmodo.ditest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class FirstViewModel : ViewModel() {
    private var _urovoScanner = MutableLiveData<String>()
    val urovoScanner: LiveData<String> get() = _urovoScanner

    init {
        App.instance().appGraph.embed(this)
        initObservables()
    }

    @Inject
    lateinit var scannerUrovo: ReceiverLiveData

    private fun initObservables() {
        _urovoScanner = scannerUrovo
    }
}