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
        use_dni: req.body.us_dni,
        use_nombres: req.body.us_nombres,
        use_apellidos: req.body.us_apellidos,
        use_correo: req.body.us_correo,
        use_password: req.body.us_password
    }

    const consulta = "INSERT INTO usuarios SET ?"
    conexion.query(consulta, usuario, (error) =>{
        if(error) return console.error(error.message)
        res.json("Se insertó correctamente el usuario")
    })
})