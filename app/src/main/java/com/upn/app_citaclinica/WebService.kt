package com.upn.app_citaclinica

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

object AppConstantes {
    const val BASE_URL = "http://192.168.56.1:3000"
}

interface WebService {
    @GET("/usuarios")
    suspend fun obtenerUsuarios(): Response<UsuarioResponse>

    @GET("/especialidades")
    suspend fun obtenerEspecialidades(): Response<EspecialidadResponse>

    @POST("/usuario/agregar")
    suspend fun agregarUsuario(@Body usuario: Usuario):Response<String>

    @GET("/usuario/{correo}")
    suspend fun buscarUsuarioxCorreo(@Path("correo")correo:String):Response<Usuario>

    @GET("/horarios/{parametro}")
    suspend fun obtenerHorarios(@Path("parametro")parametro:String): Response<HorarioResponse>

    @POST("/cita/agregar")
    suspend fun agregarCita(@Body cita: Cita):Response<String>

    @PUT("/horario/actualizar/{id_horario}")
    suspend fun actualizarHorario(@Path("id_horario")id_horario:Int):Response<String>

    @POST("/horario/liberar/{id_horario}")
    suspend fun liberarHorario(@Path("id_horario")id_horario:Int):Response<String>

    @GET("/citas/{usuario}")
    suspend fun obtenerCitas(@Path("usuario")usuario:Int): Response<CitaResponse>

    //Método para obtener la cita por id_cita
    @GET("/citamedica/{id_cita}")
    suspend fun buscarCitaPorId(@Path("id_cita") id_cita: Int): Response<CitaMedica>

    //Método para anular la cita por id_cita
    @PUT("/cita/anular/{id_cita}")
    suspend fun anularCita(@Path("id_cita") idCita: Int): Response<AnulacionResponse>
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