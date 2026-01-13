package com.example.myapplication

import retrofit2.http.GET

interface ApiService {
    @GET("students")
    suspend fun getStudents(): List<Student>
}