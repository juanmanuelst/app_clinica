package com.upn.app_citaclinica

import java.sql.Time
import java.sql.Date

data class Horario  (
    var id_horario:Int,
    var id_medico:Int,
    var horario_horas:String,
    var horario_fecha:String,
    var horario_estado:Int,
    var medico_nombre:String,
    var medico_apellido:String
)