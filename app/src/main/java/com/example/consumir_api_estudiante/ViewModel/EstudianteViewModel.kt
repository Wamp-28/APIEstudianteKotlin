package com.example.consumir_api_estudiante.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consumir_api_estudiante.Repository.EstudianteRepository
import com.example.consumir_api_estudiante.model.Estudiante
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EstudianteViewModel : ViewModel() {

    private val repository = EstudianteRepository()

    // MutableLiveData para mantener los estudiantes
    private val _estudiantes = MutableLiveData<List<Estudiante>?>(emptyList())
    val estudiantes: MutableLiveData<List<Estudiante>?> = _estudiantes

    // Obtener todos los estudiantes
    fun obtenerEstudiantes() {
        viewModelScope.launch {
            val estudiantesList = withContext(Dispatchers.IO) {
                repository.obtenerEstudiantes()
            }
            _estudiantes.postValue(estudiantesList)
        }
    }

    // Obtener un estudiante por ID
    fun obtenerEstudiante(id: Long) {
        viewModelScope.launch {
            val estudiante = withContext(Dispatchers.IO) {
                repository.obtenerEstudiante(id)
            }

        }
    }

    // Guardar un estudiante
    fun guardarEstudiante(estudiante: Estudiante) {
        viewModelScope.launch {
            repository.guardarEstudiante(estudiante)
            obtenerEstudiantes() // <-- Esto es clave para que la UI se actualice
        }
    }

    fun eliminarEstudiante(id: Long) {
        viewModelScope.launch {
            repository.eliminarEstudiante(id)
            obtenerEstudiantes() // <-- Igualmente aquí
        }
    }

}
