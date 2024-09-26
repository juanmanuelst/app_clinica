package com.upn.app_citaclinica

import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

object AppConstantes {
    const val BASE_URL = "http://192.168.56.1:3000"
}

interface WebService {
    @GET("/usuarios")
    suspend fun obtenerUsuarios(): Response<UsuarioResponse>

    @POST("/usuario/agregar")
    suspend fun agregarUsuario(@Body usuario: Usuario):Response<String>
}

object RetrofitCliente {
    val webService: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WebService::class.java)
    }
}