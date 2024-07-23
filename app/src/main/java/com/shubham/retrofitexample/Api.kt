package com.shubham.retrofitexample

import com.shubham.retrofitexample.ModelResponse.DeleteResponse
import com.shubham.retrofitexample.ModelResponse.LoginResponse
import com.shubham.retrofitexample.ModelResponse.RegisterResponse
import com.shubham.retrofitexample.ModelResponse.UpdateResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("register.php")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("updateuser.php")
    fun updateUserAccount(
        @Field("id") userId: Int,
        @Field("username") userName: String,
        @Field("email") email: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("updatepassword.php")
    fun updateUserPassword(
        @Field("email") email: String,
        @Field("current") currentPassword: String,
        @Field("new") newPassword: String,
    ): Call<UpdateResponse>

    @FormUrlEncoded
    @POST("deleteaccount.php")
    fun deleteUser(
        @Field("id") userId: Int,
    ): Call<DeleteResponse>


}