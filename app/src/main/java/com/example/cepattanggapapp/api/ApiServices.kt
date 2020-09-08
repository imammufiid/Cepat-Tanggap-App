package com.example.cepattanggapapp.api

import com.example.cepattanggapapp.models.User
import com.example.cepattanggapapp.utils.Responses
import com.example.cepattanggapapp.utils.messageResponses
import io.reactivex.Observable
import io.reactivex.Observer
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiServices {

    // EDIT PROFILE
    @FormUrlEncoded
    @POST("auth/editProfile")
    fun editProfile(
        @Field("token") token: String?,
        @Field("nama_lengkap") nama_lengkap: String?,
        @Field("email") email: String?,
        @Field("alamat") alamat: String?
    ) : Observable<Responses<User>>

    // UPLOAD
    @Multipart
    @POST("auth/editProfile")
    fun uploadProfile(
        @Part("token") token: String?,
        @Part image: MultipartBody.Part
    ): Observable<messageResponses>

    // LOGIN -------------
    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("username") email: String,
        @Field("password") password: String
    ): Observable<Responses<User>>

    // REGISTER -------------
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("nama_user") nama_lengkap: String?,
        @Field("email") email: String?,
        @Field("alamat") alamat: String?,
        @Field("password") password: String?
    ): Observable<messageResponses>
}