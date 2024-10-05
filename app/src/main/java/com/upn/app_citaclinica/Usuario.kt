package com.upn.app_citaclinica

data class Usuario (
    var id_usuario:Int,
    var usuario_nombre:String,
    var usuario_apellido:String,
    var usuario_correo: String,
    var usuario_dni:String,
    var usuario_contrasena: String,
    var usuario_tipo:Int
)