package com.example.consumir_api_estudiante.Repository


import com.example.consumir_api_estudiante.interfaces.RetrofitClient
import com.example.consumir_api_estudiante.model.Estudiante


class EstudianteRepository {

    suspend fun obtenerEstudiantes(): List<Estudiante> {
        return RetrofitClient.apiService.obtenerEstudiantes()
    }

    suspend fun obtenerEstudiante(id: Long): Estudiante {
        return RetrofitClient.apiService.obtenerEstudiante(id)
    }

    suspend fun guardarEstudiante(estudiante: Estudiante): Estudiante {
        return RetrofitClient.apiService.guardarEstudiante(estudiante)
    }

    suspend fun eliminarEstudiante(id: Long) {
        RetrofitClient.apiService.eliminarEstudiante(id)
    }
}
