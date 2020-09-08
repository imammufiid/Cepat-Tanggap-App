package com.example.cepattanggapapp.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("nama_user") var name: String? = null,
    @SerializedName("username") var email: String? = null,
    @SerializedName("alamat") var alamat: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("api_token") var api_token: String? = null
) {
}