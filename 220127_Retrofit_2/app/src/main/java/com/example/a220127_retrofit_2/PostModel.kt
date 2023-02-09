package com.example.a220127_retrofit_2

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostModel(
    @Expose
    @SerializedName("id")
    var id: String? = null,
    @Expose
    @SerializedName("pwd")
    var pwd: String? = null,
    @Expose
    @SerializedName("nick")
    var nick: String? = null,
)