package com.sudansh.appointments.network

import android.content.Context
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET

class Api {

    companion object {
        fun createWebService(context: Context): ApiService {
            return Retrofit.Builder()
                    .baseUrl("https://tutor.studysoup.com/api/")
                    .client(SelfSigningClientBuilder.createClient(context)!!)
                    .build().create(ApiService::class.java)
        }

    }


}

interface ApiService {
    @GET("schedule")
    fun getAppointment(): Call<ResponseBody>

}