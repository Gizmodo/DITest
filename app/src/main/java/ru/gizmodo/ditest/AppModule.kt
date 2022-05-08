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
class TestModule @Inject constructor(private val context: Context) {

    @Provides
    fun provideReceiver2(
        filter: IntentFilter,
    ): ReceiverLiveData {
        return ReceiverLiveData(context, filter)
    }

    @Provides
    fun provideIntentFilter(): IntentFilter {
        return IntentFilter("android.intent.ACTION_DECODE_DATA")
    }
}

class ReceiverLiveData @Inject constructor(
    private val context: Context,
    private val filter: IntentFilter,
) : MutableLiveData<String>() {

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(mBroadcastReceiver)
    }

    override fun onActive() {
        super.onActive()
        //value = mapFunc.apply(context, Intent())
        context.registerReceiver(mBroadcastReceiver, filter)
    }

    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var data = ""
            intent.extras?.let { data = it["barcode_string"].toString() }
            value = data
        }
    }
}