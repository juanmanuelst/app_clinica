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
app.post("/usuario/agregar", (req, res)=>{
    const usuario = {
        usuario_dni: req.body.usuario_dni,
        usuario_nombres: req.body.usuario_nombres,
        usuario_apellidos: req.body.usuario_apellidos,
        usuario_correo: req.body.usuario_correo,
        usuario_password: req.body.usuario_password
    }

    const consulta = "INSERT INTO usuarios SET ?"
    conexion.query(consulta, usuario, (error) =>{
        if(error) return console.error(error.message)
        res.json("Se insertó correctamente el usuario")
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