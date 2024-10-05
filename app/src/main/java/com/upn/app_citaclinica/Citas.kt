package com.upn.app_citaclinica

data class Citas (
    var id_cita:Int,
    var id_usuario:Int,
    var id_medico:Int,
    var cita_fecha:String,
    var cita_hora: String,
    var cita_estado: Int,
    var medico_nombre:String,
    var medico_apellido:String,
    var id_especialidad:String,
    var especialidad_nombre:String
)