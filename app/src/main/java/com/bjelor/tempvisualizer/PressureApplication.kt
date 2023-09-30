package com.bjelor.tempvisualizer

import android.app.Application
import com.bjelor.tempvisualizer.data.PressureReadingDao
import com.bjelor.tempvisualizer.data.PressureReadingDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

@HiltAndroidApp
class PressureApplication : Application() {

    @Inject
    lateinit var dao: PressureReadingDao

    @OptIn(ExperimentalStdlibApi::class)
    override fun onCreate() {
        super.onCreate()

        MainScope().launch {

            if (dao.getAll().firstOrNull()?.isNotEmpty() == true) {
                return@launch
            }

            val stream = resources.openRawResource(R.raw.readings)
            val bufferedReader = BufferedReader(InputStreamReader(stream))
            val stringBuilder = StringBuilder()
            bufferedReader.useLines { lines ->
                lines.forEach {
                    stringBuilder.append(it)
                }
            }
            val moshi: Moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val jsonAdapter = moshi.adapter<List<PressureReadingDto>>()

            jsonAdapter.fromJson(stringBuilder.toString())?.let { readings ->
                dao.insertAll(readings)
            }
        }
    }
}
