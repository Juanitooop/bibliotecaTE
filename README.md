# Biblioteca para el taller de Cliente Servidor

Sistema de biblioteca con **Spring Boot, Angular y MySQL**. Versión de avance: CRUD de autores, libros, usuarios y préstamos; búsquedas y paginación. Se entra directamente, sin login ni JWT. Los roles son datos del usuario y no limitan el acceso en este avance.

El backend es un proyecto **Maven compatible con NetBeans y Java 17**, sin Lombok ni configuración de otro IDE. Angular es un proyecto separado: sus archivos se pueden editar en un editor, pero se ejecuta con Node.js y Angular CLI.

## Empieza aquí

1. Sigue los pasos de instalación de este archivo.
2. Abre la aplicación en `http://localhost:4200`.
3. Practica con `docs/GUIA_SUSTENTACION.md`.
4. Revisa `docs/REQUISITOS.md` para ubicar cada requisito.

## Qué contiene cada carpeta

| Carpeta | Contenido |
|---|---|
| `backend` | Proyecto Java con `pom.xml`, configuración de NetBeans y pruebas |
| `frontend` | Proyecto Angular, pantallas, formularios y conexión con el API |
| `sql` | Creación de tablas y datos de ejemplo opcionales |
| `docs` | Guía de sustentación, permisos, pruebas y ejemplos del API |

## Herramientas necesarias

- JDK 17 para el proyecto Java. El JDK con el que inicia NetBeans puede ser otro si esa versión del IDE lo requiere; configura Java 17 como plataforma del proyecto.
- NetBeans con soporte de proyectos Java y Maven. Maven 3.6.3 o posterior si lo ejecutas desde una terminal; NetBeans puede utilizar su Maven integrado.
- MySQL 8 o posterior, por ejemplo MySQL 8.4, y MySQL Workbench u otro cliente SQL.
- Node.js 22.12 o posterior dentro de la rama 22, o Node.js 24. Se compiló aquí con Node.js 24.19.

Versiones del código: Spring Boot 3.5.16 y Angular 20.3. Las dependencias exactas del frontend quedan en `frontend/package-lock.json`.

Referencias de compatibilidad: [Spring Boot y Java](https://docs.spring.io/spring-boot/3.5/system-requirements.html) y [Angular y Node.js](https://angular.dev/reference/versions).

## Paso 1 Preparar la base de datos

1. Enciende MySQL.
2. Abre una conexión en MySQL Workbench.
3. Abre y ejecuta **todo** el archivo `sql/biblioteca.sql`.
4. Opcionalmente, ejecuta `sql/datos_ejemplo.sql` una sola vez en la base recién creada. Agrega dos autores y seis libros, suficientes para mostrar dos páginas.

El primer archivo crea la base `biblioteca` y las cuatro tablas sin borrar datos. El archivo de ejemplos no debe repetirse: los ISBN son únicos. `CREATE TABLE IF NOT EXISTS` tampoco modifica una tabla antigua que tenga columnas distintas.

En este equipo se creó una base nueva `biblioteca` en el servidor de XAMPP que ya estaba encendido. XAMPP usa **MariaDB 10.4.32**, no MySQL de Oracle. La prueba local funcionó con esa base, pero el motor solicitado por el taller es MySQL: para una entrega estricta, ejecuta los mismos scripts en MySQL 8 o superior. Consulta el alcance de las pruebas en `docs/PRUEBAS.md`.

## Paso 2 Abrir el backend en NetBeans

1. Ve a **Archivo → Abrir proyecto**.
2. Selecciona la carpeta **backend**, que contiene `pom.xml`. No selecciones la carpeta `src` ni la carpeta `frontend`.
3. Espera a que Maven descargue las dependencias. La primera vez necesitas internet.
4. En las propiedades del proyecto, revisa que la plataforma Java sea JDK 17. Si no aparece, agrégala desde **Herramientas → Plataformas Java**.
5. En **Source Packages**, verás `com.taller.biblioteca` y sus subcarpetas.
6. En **Other Sources → src/main/resources**, abre `application.properties`.
7. Ajusta el usuario y la contraseña de tu MySQL. Si usas `root` sin contraseña, los valores actuales funcionan en un entorno local de práctica.

```properties
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:}
```

Por ejemplo, si tu contraseña local es `MiClave`, puedes dejar temporalmente `spring.datasource.password=MiClave`. No compartas una contraseña personal en la entrega. También puedes definir las variables `DB_USER` y `DB_PASSWORD` en el entorno desde el que abres NetBeans.

8. Usa **Limpiar y construir**. Esto compila Java y ejecuta las pruebas con una base temporal H2, sin tocar MySQL.
9. Usa **Ejecutar proyecto**. `nbactions.xml` indica a NetBeans que ejecute `spring-boot:run`.
10. Espera el mensaje `Started BibliotecaApplication` y el puerto **8081** en la salida.

Si NetBeans pregunta por una clase principal, usa `com.taller.biblioteca.BibliotecaApplication`. Si no aplica la acción de ejecución, ejecuta el objetivo Maven `spring-boot:run` desde las acciones de Maven del proyecto.

El puerto 8081 se eligió porque el 8080 ya estaba ocupado en este equipo. No necesitas instalar Tomcat: Spring Boot incluye su servidor.

### Alternativa desde una terminal

Dentro de `backend`, con Maven disponible:

```powershell
mvn clean verify
mvn spring-boot:run
```

Otra opción, después de construir:

```powershell
java -jar target/biblioteca-1.0.0.jar
```

Ejecuta solo una instancia del backend para evitar que el puerto quede ocupado. En NetBeans detén la ejecución anterior antes de iniciar otra.

## Paso 3 Ejecutar Angular

Abre PowerShell en la carpeta `frontend` y ejecuta:

```powershell
npm.cmd ci
npm.cmd start
```

`npm ci` descarga las dependencias según el archivo de versiones. `npm start` compila Angular y mantiene el frontend encendido. No necesitas instalar Angular CLI globalmente: está incluido en el proyecto.

Abre **http://localhost:4200** en el navegador. Mantén encendidos MySQL, Spring Boot y la terminal de Angular. No abras `index.html` con doble clic.

Angular envía las peticiones a `/api`. `proxy.conf.json` las reenvía a `http://127.0.0.1:8081`. Si cambias el puerto de Java, cambia también ese archivo y reinicia Angular. Para desplegar el resultado de `npm run build`, el servidor web también debe reenviar `/api` al backend; el proxy de Angular CLI solo funciona con `npm start`.

## Versión de avance sin JWT

Al abrir `http://localhost:4200` aparece el catálogo. Administración permite gestionar autores, libros y usuarios. Préstamos permite seleccionar un lector y registrar, editar o eliminar un préstamo. En Postman selecciona **Authorization → No Auth**.

Se conservan las columnas de usuario, rol, estado y contraseña del modelo solicitado. BCrypt guarda la contraseña como hash; no es JWT y no inicia sesión. Los préstamos nuevos requieren un lector activo. Las cuentas iniciales se crean solo si la tabla de usuarios está vacía.

Antes de probar estos cambios, detén y vuelve a ejecutar el backend en NetBeans. Reinicia también `npm.cmd start`, porque cambió el proxy. No necesitas volver a crear las tablas ni borrar datos.

El documento exige JWT y permisos por rol para la entrega final: quedan pendientes en este avance. Este repositorio contiene únicamente la versión sin JWT.

Cada libro representa un ejemplar. Un libro puede tener varios préstamos históricos, pero solo uno pendiente. `fecha_devolucion` significa la **fecha prevista** de devolución; `devuelto` confirma si ya se devolvió. No se añadió otra columna para conservar el modelo solicitado.

No se elimina un autor con libros ni un usuario o libro con préstamos relacionados. Para demostrar DELETE, usa registros de prueba sin relaciones o elimina primero el préstamo de práctica.

## Comprobar el proyecto

```powershell
# Desde backend:
mvn test

# Desde frontend:
npm.cmd run build
```

Las pruebas Java ejercitan los controladores sin token, las validaciones, búsquedas y CRUD. La guía `docs/PRUEBAS.md` separa lo probado automáticamente de lo comprobado en el navegador.

## Preparar la entrega

Entrega `backend`, `frontend`, `sql` y `docs`, junto con este README. No incluyas `node_modules`, `target`, `.angular` ni `.tools`: contienen dependencias o archivos generados que se pueden reconstruir. El archivo ZIP de la entrega contiene únicamente los archivos necesarios del proyecto.

Antes de sustentar, practica crear, consultar, editar y eliminar un préstamo. El documento asigna 60 % a la sustentación individual: estudia el recorrido del formulario hasta la base de datos y realiza los ejercicios de reparación de la guía.
