package com.sudansh.appointments.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.sudansh.appointments.network.Api
import com.sudansh.appointments.network.Schedule
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainViewModel(app: Application) : AndroidViewModel(app) {
    val liveSchedules = MutableLiveData<List<Date>>()
    private val service = Api.createWebService(app.applicationContext)

    init {
        fetchSchedules()
    }

    private fun fetchSchedules() {
        service.getAppointment().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.i("Error", t.localizedMessage)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    parseResponse(response.body()?.string().orEmpty())
                }
            }

        })
    }

    private fun parseResponse(resposne: String) {
        try {
            val builder = GsonBuilder()
            builder.registerTypeAdapter(object : TypeToken<Schedule>() {}.type, ScheduleDeserializer())
            val gson = builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
            val cityList = gson.fromJson<Schedule>(resposne, object : TypeToken<Schedule>() {}.type)
            liveSchedules.value = cityList.dates
        } catch (e: Exception) {
            Log.i("Error", e.localizedMessage)
        }
    }
}