package com.upn.app_citaclinica

data class Usuario (
    var usuario_id:Int,
    var usuario_dni:String,
    var usuario_nombres:String,
    var usuario_apellidos:String,
    var usuario_correo: String,
    var usuario_password: String
)