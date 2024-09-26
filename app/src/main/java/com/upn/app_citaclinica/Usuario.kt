package com.upn.app_citaclinica

data class Usuario (
    var us_id:Int = 0,
    var us_dni:String,
    var us_nombres:String,
    var us_apellidos:String,
    var us_correo: String,
    var us_password: String
)