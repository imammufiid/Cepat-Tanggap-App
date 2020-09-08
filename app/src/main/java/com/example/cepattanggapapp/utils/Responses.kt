package com.example.cepattanggapapp.utils

import com.google.gson.annotations.SerializedName

data class Responses<T> (
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("result") var result: T? = null
)

data class messageResponses (
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: Int? = null
)

data class ListResponses<T> (
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("result") var result: List<T>? = null
)