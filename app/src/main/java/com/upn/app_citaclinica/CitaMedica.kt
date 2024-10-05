package com.upn.app_citaclinica

data class CitaMedica (
    val IdCita: Int,
    val UsuarioCita: String,
    val Especialidad: String,
    val Medico: String,
    val FechaCita: String,
    val HoraCita: String
)
