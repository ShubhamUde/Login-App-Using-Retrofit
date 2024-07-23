package com.shubham.retrofitexample.ModelResponse

import com.google.gson.annotations.SerializedName
import com.shubham.retrofitexample.User

class UpdateResponse {
    @SerializedName("error") var error: String? = null
    @SerializedName("message") var message: String? = null
}
