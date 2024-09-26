package com.upn.app_citaclinica

import com.google.gson.annotations.SerializedName

data class UsuarioResponse (
    @SerializedName("listaUsuarios") var listaUsuario:ArrayList<Usuario>
)