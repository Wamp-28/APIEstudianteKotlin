package com.example.consumir_api_estudiante.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.consumir_api_estudiante.ViewModel.EstudianteViewModel
import com.example.consumir_api_estudiante.model.Estudiante
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun EstudianteScreen(viewModel: EstudianteViewModel = viewModel()) {
    val estudiantes by viewModel.estudiantes.observeAsState(emptyList())
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var codigoBusqueda by remember { mutableStateOf("") }  // Variable para el código de búsqueda

    // Estado para controlar el diálogo de confirmación
    var showDialog by remember { mutableStateOf(false) }
    var estudianteAEliminar by remember { mutableStateOf<Estudiante?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current // Para el contexto del Toast

    // Cargar estudiantes al iniciar
    LaunchedEffect(true) {
        viewModel.obtenerEstudiantes()
    }

    val estudiantesFiltrados = estudiantes?.filter {
        it.codigo?.toString()?.contains(codigoBusqueda, ignoreCase = true) ?: false
    } ?: emptyList() // Si estudiantes es null, devolver una lista vacía

    Column(modifier = Modifier.padding(top = 60.dp)) {
        Text(text = "Lista de Estudiantes", style = MaterialTheme.typography.headlineSmall)

        // Campo para buscar por código
        TextField(
            value = codigoBusqueda,
            onValueChange = { codigoBusqueda = it },
            label = { Text("Buscar por código") },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Codigo Icon")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Text(
            text = "Agregar Nuevo Estudiante",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Campo de Nombre con ícono
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            leadingIcon = {
                Icon(Icons.Filled.Person, contentDescription = "Nombre Icon")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            isError = nombre.isBlank() // Validación de nombre obligatorio
        )

        // Mensaje de error si el nombre está vacío
        if (nombre.isBlank()) {
            Text(
                text = "El nombre es obligatorio",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Campo de Teléfono con ícono
        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            leadingIcon = {
                Icon(Icons.Filled.Phone, contentDescription = "Teléfono Icon")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            isError = telefono.length != 10 // Validación de teléfono con 10 caracteres exactos
        )

        // Mensaje de error si el teléfono no tiene 10 caracteres
        if (telefono.isNotEmpty() && telefono.length != 10) {
            Text(
                text = "El teléfono debe tener exactamente 10 dígitos.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Botón para Agregar Estudiante con ícono
        Button(
            onClick = {
                if (nombre.isNotEmpty() && telefono.length == 10) {
                    scope.launch {
                        val nuevoEstudiante = Estudiante(nombre = nombre, telefono = telefono)
                        viewModel.guardarEstudiante(nuevoEstudiante)
                        nombre = ""
                        telefono = ""
                        // Mostrar el Toast de éxito
                        Toast.makeText(context, "Usuario insertado", Toast.LENGTH_SHORT).show()
                        viewModel.obtenerEstudiantes()
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            enabled = nombre.isNotEmpty() && telefono.length == 10 // Habilitar solo si todo es válido
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar Estudiante Icon")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Agregar Estudiante")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn para mostrar estudiantes filtrados
        if (estudiantesFiltrados.isEmpty()) {
            Text(text = "No hay estudiantes disponibles.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(estudiantesFiltrados) { estudiante ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Código: ${estudiante.codigo}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Nombre: ${estudiante.nombre}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Teléfono: ${estudiante.telefono}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            // Botón de Eliminar Estudiante con ícono
                            Button(
                                onClick = {
                                    // Mostrar el diálogo de confirmación de eliminación
                                    estudianteAEliminar = estudiante
                                    showDialog = true
                                },
                                modifier = Modifier.padding(top = 8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Eliminar Estudiante Icon"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }


    // Diálogo de confirmación de eliminación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmación") },
            text = { Text("¿Está seguro que desea eliminar a este estudiante?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Eliminar el estudiante si el usuario confirma
                        estudianteAEliminar?.let {
                            viewModel.eliminarEstudiante(it.codigo)
                            // Mostrar el Toast de éxito
                            Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                            viewModel.obtenerEstudiantes()
                        }
                        showDialog = false
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }
}
