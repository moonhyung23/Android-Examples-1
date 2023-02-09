package com.example.a220127_retrofit_2

data class SampleData(
    val `data`: List<Data>,
    val statusCode: String,
    val statusMessage: String
)

data class Data(
    val email: String,
    val id: String,
    val nick_name: String,
    val profile: String
)