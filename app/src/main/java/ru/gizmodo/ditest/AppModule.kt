package ru.gizmodo.ditest

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application
}

@Module
class SuperModule @Inject constructor(
    private val context: Context,
) {
    @Provides
    fun provideSuperData(): ReceiverLiveData2 = ReceiverLiveData2(context)
}

@Module
class TestModule @Inject constructor(
    private val context: Context,
    private val data: String,
//    private val data: String,
) {

    @Provides
    fun provideReceiver(): ReceiverLiveData = ReceiverLiveData(context, data)

/*  @Provides
      @Named("Settings")
      fun provideIntentFilter(): IntentFilter = IntentFilter(data)*/
}

data class SuperResults(
    val urovo: String,
    val idata: String,
    val isUrovoEnterPushed: Boolean,
)

class ReceiverLiveData2 @Inject constructor(
    private val context: Context,
) : MutableLiveData<SuperResults>() {

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(urovoScannerReceiver)
        context.unregisterReceiver(idataScannerReceiver)
        context.unregisterReceiver(urovoKeyboardReceiver)
    }

    var superResults: SuperResults = SuperResults("", "", false)

    override fun onActive() {
        super.onActive()
        //value = mapFunc.apply(context, Intent())
        context.registerReceiver(urovoScannerReceiver,
            IntentFilter("android.intent.ACTION_DECODE_DATA"))
        context.registerReceiver(idataScannerReceiver,
            IntentFilter("android.intent.action.SCANRESULT"))
        context.registerReceiver(urovoKeyboardReceiver,
            IntentFilter("android.intent.action_keyboard"))
    }

    private val urovoScannerReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.extras?.let {
                superResults = superResults.copy(urovo = it["barcode_string"].toString())
                this@ReceiverLiveData2.value = superResults
            }
        }
    }
    private val idataScannerReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.extras?.let {
                superResults = superResults.copy(idata = it["value"].toString())
                value = superResults
            }
        }
    }
    private val urovoKeyboardReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.extras?.let {
                superResults =
                    superResults.copy(isUrovoEnterPushed = it["kbrd_enter"].toString() == "enter")
                value = superResults
            }
        }
    }
}

class ReceiverLiveData @Inject constructor(
    private val context: Context,
    private val data: String,
//    private val filter: IntentFilter,
) : MutableLiveData<String>() {

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(mBroadcastReceiver)
    }

    override fun onActive() {
        super.onActive()
        //value = mapFunc.apply(context, Intent())
        context.registerReceiver(mBroadcastReceiver, IntentFilter(data))
    }

    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.extras?.let { value = it["barcode_string"].toString() }
        }
    }
}