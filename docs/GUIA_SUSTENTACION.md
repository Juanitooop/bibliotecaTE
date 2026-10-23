# Guía para explicar el avance

## ¿Angular está conectado a Java?

Sí. Angular muestra el formulario. Al guardar, ApiService envía los datos en JSON a Spring Boot. Java los revisa y los guarda en la base de datos. Después devuelve la respuesta para actualizar la pantalla.

Recorrido de un libro:
1. `frontend/src/app/paginas/admin.component.html`: formulario.
2. `admin.component.ts`, método guardar: recoge los datos.
3. `servicios/api.service.ts`, método guardar: POST si es nuevo; PUT si tiene id.
4. `frontend/proxy.conf.json`: reenvía /api al puerto 8081.
5. `backend/.../controlador/LibroController.java`: recibe la petición.
6. `dto/LibroDTO.java`: revisa campos obligatorios y formatos.
7. `repositorio/LibroRepository.java`: guarda o consulta mediante JPA.
8. `modelo/Libro.java`: representa una fila de la tabla libro.

## Palabras que necesitas entender

| Concepto | Explicación sencilla |
|---|---|
| Cliente | Angular, la parte que usa la persona |
| Servidor | Spring Boot, recibe y procesa las peticiones |
| HTTP | Forma en la que se comunican cliente y servidor |
| JSON | Texto con nombres y valores que se envía entre ambos |
| CRUD | Crear, consultar, editar y eliminar |
| DTO | Objeto con los datos que recibe el formulario |
| JPA y repositorio | Permiten trabajar con las tablas desde Java |
| id | Número que identifica un registro |
| autorId | Id del autor al que pertenece un libro |
| async y await | Permiten esperar la respuesta del servidor |
| firstValueFrom | Convierte la respuesta de HttpClient en algo que podemos esperar con await |
| T en ApiService | Tipo de dato que esperamos recibir, por ejemplo Libro |
| ngModel | Une un campo del formulario con una variable |
| ngIf y ngFor | Muestran algo según una condición o repiten una fila |
| @Transactional | Si falla la operación, cancela sus cambios en la base de datos |
| Hash de contraseña | Forma de guardar la clave sin guardar su texto original; no es un token |

## Qué decir sobre la seguridad

“Para este avance presento la conexión y los CRUD sin login. Los campos rol y estado están en la base de datos. El inicio de sesión con JWT y los permisos por rol se incorporarán para la entrega final.”

No hay token, interceptor ni rutas protegidas en esta versión. Cualquier persona que acceda localmente puede usar las pantallas.

## Demostración de la conexión

1. Abre Angular en localhost:4200 y entra en Administración → Libros.
2. Crea un libro con un ISBN nuevo y un autor existente.
3. En Postman haz GET http://127.0.0.1:8081/api/libros?q=el_titulo (No Auth).
4. Comprueba que aparece el mismo título e id.
5. Cambia el título desde Angular y vuelve a consultar en Postman.
6. Elimina solo ese libro de prueba y comprueba que desaparece.

Esto muestra que la interfaz usa la información del backend. No son datos de prueba escritos dentro de Angular.

## Si la docente cambia algo

| Problema | Qué revisar |
|---|---|
| No aparecen libros | Backend encendido, conexión a base de datos y pestaña Network del navegador |
| Cambia el puerto de Java | server.port en application.properties y target en proxy.conf.json; reiniciar ambos |
| Cambia /api/libros | RequestMapping en LibroController debe coincidir con la ruta usada por Angular |
| El título llega vacío | name y ngModel del input, variable libro.titulo y cuerpo JSON |
| POST falla con 400 | Campos obligatorios del DTO y mensaje de respuesta |
| POST falla con 409 | ISBN repetido o préstamo pendiente del mismo libro |
| GET devuelve 404 | Revisar ruta y que exista el id |
| No se puede borrar autor | Tiene libros asociados; probar con un autor de práctica sin libros |
| Angular muestra login viejo | Reiniciar npm start y recargar; la ruta /login ahora redirige a libros |
| Aparece 401 en Postman | Está ejecutándose la versión antigua; detener y ejecutar el backend actualizado |
| No compila Java | Leer el primer error: archivo y línea; revisar llaves, punto y coma e imports |

## Lo que se simplificó

Se retiraron JWT, filtros de seguridad, guards y el interceptor. Se reemplazaron varias expresiones compactas por if y for. Se mantuvieron DTO, JPA, componentes y servicios porque permiten cumplir y explicar la separación cliente-servidor.

La comprobación de libro ocupado funciona para operaciones consecutivas. Este avance no incluye bloqueo para evitar que dos peticiones simultáneas presten el mismo libro; queda como mejora antes de un uso concurrente.

Practica siguiendo un registro desde el formulario hasta el controlador. No necesitas memorizar cada línea, pero sí explicar qué recibe, qué revisa y qué devuelve cada método.
