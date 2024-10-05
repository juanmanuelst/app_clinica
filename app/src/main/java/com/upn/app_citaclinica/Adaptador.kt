package com.upn.app_citaclinica

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class Adaptador:RecyclerView.Adapter<Adaptador.MiViewHolder>() {

    private var listaCitas = ArrayList<Citas>()
    private lateinit var context: Context

    fun agregarContexto(context: Context){
        this.context = context
    }

    fun agregarCitas(citas:ArrayList<Citas>){
        this.listaCitas = citas
    }

    class MiViewHolder(var view: View):RecyclerView.ViewHolder(view) {
        private var filaNombresDoctor = view.findViewById<TextView>(R.id.filaNombresDoctor)
        private var filaFecha = view.findViewById<TextView>(R.id.filaFecha)
        val filaEstado = view.findViewById<TextView>(R.id.filaEstado)
        private var filaEspecialidad = view.findViewById<TextView>(R.id.filaEspecialidad)
        private var filaIdCita = view.findViewById<TextView>(R.id.filaIdCita)

        fun rellenarFila(cita: Citas){
            filaNombresDoctor.text = "Dr "+cita.medico_nombre+" "+cita.medico_apellido
            filaFecha.text = cita.cita_fecha+" "+cita.cita_hora
            filaEspecialidad.text = cita.especialidad_nombre
            filaEstado.text = if (cita.cita_estado == 1) "Confirmada" else "Cancelada"
            filaIdCita.text = String.format("C%03d", cita.id_cita)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MiViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.filacita,parent,false)
    )

    override fun onBindViewHolder(holder: Adaptador.MiViewHolder, position: Int) {
        val citaItem = listaCitas[position]

        if (citaItem.cita_estado == 1) {
            holder.filaEstado.setTextColor(Color.parseColor("#4CAF50")) // Verde
        } else {
            holder.filaEstado.setTextColor(Color.parseColor("#F44336")) // Rojo
        }
        holder.rellenarFila(citaItem)
    }

    override fun getItemCount(): Int {
        return  listaCitas.size
    }

}