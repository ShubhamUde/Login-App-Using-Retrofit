package com.shubham.retrofitexample.ModelResponse

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("error") val error: String?,
    @SerializedName("message") val message: String?
)
