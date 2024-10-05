package com.upn.app_citaclinica

import com.google.gson.annotations.SerializedName

data class CitaResponse (
    @SerializedName("listaCitas") var listaCitas:ArrayList<Citas>
)