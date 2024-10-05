package com.upn.app_citaclinica

import com.google.gson.annotations.SerializedName

data class HorarioResponse (
    @SerializedName("listaHorarios") var listaHorarios:ArrayList<Horario>
)