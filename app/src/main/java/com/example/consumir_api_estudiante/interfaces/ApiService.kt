package com.example.consumir_api_estudiante.interfaces


import com.example.consumir_api_estudiante.model.Estudiante
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// SUSPEND - FUNCIONES ASINCRONAS - SE PAUSAN SIN BLOQUEAR LA EJECUCION PRINCIPAL

interface ApiService {
    @GET("/api/listar")
    suspend fun obtenerEstudiantes(): List<Estudiante>

    @GET("/api/listar/{id}")
    suspend fun obtenerEstudiante(@Path("id") id: Long): Estudiante

    @POST("/api/guardar")
    suspend fun guardarEstudiante(@Body estudiante: Estudiante): Estudiante

    @DELETE("/api/eliminar/{id}")
    suspend fun eliminarEstudiante(@Path("id") id: Long)
}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8081"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
