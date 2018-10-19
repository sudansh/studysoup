package com.sudansh.apimodule.network

import android.content.Context
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import java.util.*

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

    @PUT("schedule")
    fun updateAppointment(@Query("appointment") appointment: Date): Call<ResponseBody>

    @DELETE("schedule")
    fun deleteAppointment(@Query("appointment") appointment: Date): Call<ResponseBody>

    @POST("schedule")
    fun createAppointment(@Query("guid") guid: Int, @Query("appointment") appointment: Date): Call<ResponseBody>

}