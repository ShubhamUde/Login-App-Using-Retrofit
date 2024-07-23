package com.shubham.retrofitexample.ModelResponse

import com.google.gson.annotations.SerializedName

class DeleteResponse {
    @SerializedName("error") var error: String? = null
    @SerializedName("message") var message: String? = null
}