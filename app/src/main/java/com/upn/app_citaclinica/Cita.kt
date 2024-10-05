package com.upn.app_citaclinica

data class Cita (
    var id_cita:Int,
    var id_usuario:Int,
    var id_medico:Int,
    var cita_fecha:String,
    var cita_hora: String,
    var cita_estado: Int
)