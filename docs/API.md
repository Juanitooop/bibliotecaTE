# Probar el avance con Postman

Base: http://127.0.0.1:8081/api. El backend debe estar ejecutándose.
En Authorization usa No Auth: esta versión no tiene login ni JWT.
Para POST y PUT, selecciona Body → raw → JSON.

## Operaciones

Las cuatro rutas son `/autores`, `/libros`, `/usuarios` y `/prestamos`.

| Método | Ejemplo | Qué hace |
|---|---|---|
| GET | /libros?page=0&size=5&q=Gabriel | Lista y busca |
| GET | /libros/1 | Consulta un registro |
| POST | /libros | Crea un registro |
| PUT | /libros/1 | Edita el registro 1 |
| DELETE | /libros/1 | Elimina el registro 1 |

Usa el id que devuelve el servidor; no siempre será 1. La primera página es 0.
`content` contiene los registros de la página; `totalElements` cuenta todos los resultados.

## Cuerpos de ejemplo

Autor:
```json
{"nombre":"Ana","apellido":"Perez","nacionalidad":"Colombiana","fechaNacimiento":"1980-01-01"}
```
Libro (autorId debe existir; el ISBN no debe estar repetido):
```json
{"titulo":"Libro de prueba","isbn":"1234567890","anio":2024,"autorId":1}
```
Usuario:
```json
{"nombre":"Pedro","username":"pedro","email":"pedro@example.com","password":"Prueba12345","rol":"LECTOR","estado":"ACTIVO"}
```
Al editar un usuario, password vacío conserva su clave anterior.
Préstamo (libroId y usuarioId deben existir; usuarioId corresponde a un lector activo):
```json
{"libroId":1,"usuarioId":2,"fechaPrestamo":"2026-10-23","fechaDevolucion":"2026-10-30","devuelto":false}
```
Para devolverlo, haz PUT a `/prestamos/ID` con todos los campos y `devuelto: true`.
La fecha de préstamo no puede ser futura. Ajusta las fechas al día de tu demostración.

## Respuestas

200: consulta o edición correcta. 201: creado. 204: eliminado, sin cuerpo.
400: datos incompletos o incorrectos. 404: id inexistente. 409: dato repetido o relación que impide borrar.
Si recibes 401, comprueba que detuviste el backend anterior y ejecutaste la versión sin JWT.
No borres un autor con libros relacionados: elimina primero los registros de práctica que dependen de él.
