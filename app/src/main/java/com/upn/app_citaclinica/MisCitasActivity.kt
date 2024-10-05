package com.upn.app_citaclinica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MisCitasActivity : AppCompatActivity() {
    private  lateinit var  btnVolverMenu1 : Button
    private lateinit var rvCitas: RecyclerView
    private var adaptador:Adaptador = Adaptador()
    private var listaCitas = ArrayList<Citas>()

    /**/
    /*datos*/
    private var nombres:String =""
    private var apellidos:String=""
    private var idusuario:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_citas)
        asignarReferencias()
        cargarDatos()
        recibirDatos()
    }

    private fun asignarReferencias() {
        rvCitas = findViewById(R.id.rvCitas)
        rvCitas.layoutManager = LinearLayoutManager(this)
        rvCitas.adapter = adaptador

        btnVolverMenu1 = findViewById(R.id.btnVolverMenu1)
        btnVolverMenu1.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            intent.putExtra("var_nombres", nombres)
            intent.putExtra("var_apellidos",apellidos)
            intent.putExtra("var_usuario",idusuario)
            startActivity(intent)
        }
    }

    private fun cargarDatos(){
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitCliente.webService.obtenerCitas(idusuario)
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaCitas = rpta.body()!!.listaCitas
                    mostrarDatos()
                }
            }
        }
    }

    private fun mostrarDatos(){
        adaptador.agregarContexto(this)
        adaptador.agregarCitas(listaCitas)
        rvCitas.adapter = adaptador

    }

    private fun recibirDatos(){
        nombres = (intent.getStringExtra("var_nombres")).toString()
        apellidos = (intent.getStringExtra("var_apellidos")).toString()
        idusuario = intent.getIntExtra("var_usuario",0)
    }
}