const express = require("express")
const mysql = require("mysql2")
const bodyParser = require("body-parser")

const app = express()
const PUERTO = 3000

app.use(bodyParser.json())

app.listen(PUERTO,()=>{
    console.log("Servidor corriendo en el puerto "+ PUERTO)
})

const conexion = mysql.createConnection(
    {
        host: 'localhost',
        database: 'app_clinica',
        user: 'root',
        password: 'admin123456',
        port: 3306
    }
)

conexion.connect(error =>{
    if(error) throw error
    console.log("Conexion exitosa a la base de datos")
})

app.get("/",(req,res)=>{
    res.send("Bienvenido a mi servicio web")
})

app.get("/usuarios",(req,res)=>{
    const consulta = "SELECT * FROM usuarios"
    conexion.query(consulta,(error,rpta) =>{
        if(error) return console.log(error.message)

            const obj = {}
            if(rpta.length > 0){
                obj.listaUsuarios = rpta
                res.json(obj)
            }else{
                res.json("no hay registros")
            }
    })
})

app.get("/especialidades",(req,res)=>{
    const consulta = "SELECT * FROM especialidades"
    conexion.query(consulta,(error,rpta) =>{
        if(error) return console.log(error.message)

            const obj = {}
            if(rpta.length > 0){
                obj.listaEspecialidades = rpta
                res.json(obj)
            }else{
                res.json("no hay registros")
            }
    })
})

app.post("/usuario/agregar", (req, res)=>{
    const usuario = {
        usuario_dni: req.body.usuario_dni,
        usuario_nombre: req.body.usuario_nombre,
        usuario_apellido: req.body.usuario_apellido,
        usuario_correo: req.body.usuario_correo,
        usuario_contrasena: req.body.usuario_contrasena
    }

    const consulta = "INSERT INTO usuarios SET ?"
    conexion.query(consulta, usuario, (error) =>{
        if(error) return console.error(error.message)
        res.json("Usuario registrado correctamente")
    })
})

app.get("/usuario/:correo",(req,res)=>{
    const {correo} = req.params
    const consulta = "SELECT * FROM usuarios WHERE usuario_correo='"+correo+"'"
    conexion.query(consulta,(error,rpta) =>{
        if(error) return console.log(error.message)
            if(rpta.length > 0){
                res.json(rpta[0])
            }else{
                res.json("no hay registros")
            }
    })
})




app.get("/horarios/:parametro",(req,res)=>{
    const valores = req.params.parametro.split("&");
    const fecha = valores[0]
    const especialidad = valores[1]
    const consulta = "select *,time_format(h.horario_hora,'%H:%i') as horario_horas from horarios_medicos h "+
    "inner join medicos m on h.id_medico=m.id_medico "+
    "inner join especialidades e on e.id_especialidad=m.id_especialidad "+
    "where h.horario_fecha ='"+fecha+"' and m.id_especialidad="+especialidad+
    " and h.horario_estado = 0 order by h.horario_hora asc"
    console.log(consulta)
    conexion.query(consulta,(error,rpta) =>{
        if(error) return console.log(error.message)

            const obj = {}
            if(rpta.length > 0){
                obj.listaHorarios = rpta
                res.json(obj)
            }else{
                obj.listaHorarios
                res.json(obj)
            }
    })
})


app.post("/cita/agregar", (req, res)=>{
    const cita = {
        id_usuario: req.body.id_usuario,
        id_medico: req.body.id_medico,
        cita_fecha: req.body.cita_fecha,
        cita_hora: req.body.cita_hora,
    }

    const consulta = "INSERT INTO citas SET ?"
    conexion.query(consulta, cita, (error) =>{
        if(error) return console.error(error.message)
        res.json("Cita registrada correctamente")
    })
})


app.put("/horario/actualizar/:id_horario",(req,res)=>{
    const {id_horario} = req.params
    const consulta = "UPDATE horarios_medicos SET horario_estado = 1 WHERE id_horario="+id_horario+""
    conexion.query(consulta,(error,rpta) =>{
        if(error) return console.error(error.message)
        res.json("Horario actualizado correctamente")
    })
})


app.get("/citas/:usuario",(req,res)=>{
    const {usuario} = req.params
    const consulta = "select c.id_cita,c.id_usuario,c.id_medico,date_format(c.cita_fecha, '%d/%m/%Y') "+
    "as cita_fecha,time_format(c.cita_hora,'%H:%i') as cita_hora, m.medico_nombre,m.medico_apellido,"+
    "e.id_especialidad,e.especialidad_nombre,c.cita_estado from citas c inner join medicos m on "+
    "c.id_medico=m.id_medico inner join especialidades e "+
    "on m.id_especialidad=e.id_especialidad where id_usuario ="+usuario+
    " order by cita_fecha,cita_hora desc"
    conexion.query(consulta,(error,rpta) =>{
        if(error) return console.log(error.message)

            const obj = {}
            if(rpta.length > 0){
                obj.listaCitas = rpta
                res.json(obj)
            }else{
                obj.listaCitas
                res.json(obj)
            }
    })
})

app.get("/citamedica/:id_cita", (req, res) => {
    const { id_cita } = req.params;
    console.log('id_cita recibido:', id_cita);  
    
    const consulta = `
   SELECT 
    cit.id_cita AS IdCita,
    CONCAT(us.usuario_nombre, ' ', us.usuario_apellido) AS UsuarioCita,
    esp.especialidad_nombre AS Especialidad,
    CONCAT(med.medico_nombre, ' ', med.medico_apellido) AS Medico,
    cit.cita_fecha AS FechaCita,
    cit.cita_hora AS HoraCita
    FROM citas cit
    INNER JOIN usuarios us ON us.id_usuario = cit.id_usuario
    INNER JOIN medicos med ON med.id_medico = cit.id_medico
    INNER JOIN especialidades esp ON esp.id_especialidad = med.id_especialidad
    WHERE cit.id_cita = ?`; 

 
    conexion.query(consulta, [id_cita], (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ error: "Error en la base de datos" });
        }
   
        if (results.length === 0) {
            return res.status(404).json({ mensaje: "Cita no encontrada" });
        }
        

        res.json(results[0]);
    });
});


app.put("/cita/anular/:id_cita", (req, res) => {
    const { id_cita } = req.params;
    console.log('id_cita recibido:', id_cita);
    const consulta = "UPDATE citas SET cita_estado = 0 WHERE id_cita = ?";

    conexion.query(consulta, [id_cita], (error, results) => {
        if (error) {
            console.error(error.message);
            return res.status(500).json({ error: "Error al cancelar la cita" });
        }

        if (results.affectedRows === 0) {
            return res.status(404).json({ mensaje: "Cita no encontrada" });
        }

        res.json({ mensaje: "Cita cancelada correctamente" });
    });
});



