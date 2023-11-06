package com.example.sogating_app.message.fcm

import com.example.sogating_app.message.fcm.Repository.Companion.CONTENT_TYPE
import com.example.sogating_app.message.fcm.Repository.Companion.SERVER_KEY
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotiApi {
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: PushNotification): Response<ResponseBody>
}