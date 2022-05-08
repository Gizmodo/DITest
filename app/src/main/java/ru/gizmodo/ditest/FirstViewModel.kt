package ru.gizmodo.ditest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class FirstViewModel : ViewModel() {
    private var _urovoScannerMutableLiveData = MutableLiveData<String>()
    val urovoScannerLiveData: LiveData<String> get() = _urovoScannerMutableLiveData

    private var _superScannerLMutableLiveData = MutableLiveData<SuperResults>()
    val superScannerLiveData: LiveData<SuperResults> get() = _superScannerLMutableLiveData

    init {
        App.instance().appGraph.embed(this)
        initObservables()
    }

    @Inject
    lateinit var scannerUrovo: ReceiverLiveData

    @Inject
    lateinit var superScanner: ReceiverLiveData2

    private fun initObservables() {
        _urovoScannerMutableLiveData = scannerUrovo
        _superScannerLMutableLiveData = superScanner
    }
}