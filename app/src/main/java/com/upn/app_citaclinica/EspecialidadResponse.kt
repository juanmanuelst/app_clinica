package com.upn.app_citaclinica

import com.google.gson.annotations.SerializedName

data class EspecialidadResponse (
    @SerializedName("listaEspecialidades") var listaEspecialidades:ArrayList<Especialidad>
)