# Verificación del avance sin JWT

Fecha: 23 de octubre de 2026.

- Backend: Maven package con Java 17. Compilación correcta y 7 pruebas de integración aprobadas, sin fallos.
- Las pruebas usan H2 temporal y realizan CRUD de autores, libros, usuarios y préstamos sin cabecera Authorization.
- También comprueban contraseñas ocultas, conservación de contraseña al editar, préstamo ocupado, devolución con usuario inactivo, validaciones, relaciones, búsqueda y paginación.
- Frontend: npm run build correcto, sin advertencias de plantillas.
- Comprobación local: nueva instancia de Spring Boot en 8082 y Angular en 4201, con proxy temporal. Los puertos normales siguen siendo 8081 y 4200.
- GET /api/libros directo y a través del proxy devolvieron los mismos 6 registros totales y el mismo primer id. GET de autores, usuarios y préstamos por el proxy también respondió sin token.
- Navegador: /login redirigió a /libros. Se visualizaron el catálogo, administración de autores, préstamos y formulario con opciones reales de libros y lectores.
- No se modificaron registros de la base real durante esta comprobación. Las escrituras CRUD se verificaron con H2.

## Límites

La base local existente es MariaDB de XAMPP; Hibernate muestra una advertencia de versión del motor. La comprobación local no sustituye probar en MySQL 8, solicitado por el taller.
JWT, login y restricciones de acceso por rol están retirados para el avance. La comprobación de préstamo ocupado no incluye bloqueo ante peticiones simultáneas.
Las capturas antiguas de login pertenecen a la versión anterior y no se incluyen en el ZIP del avance.

## Repetir

En backend: mvn test. En frontend: npm.cmd run build.
Para probar normalmente, reinicia el backend en NetBeans y ejecuta npm.cmd start desde frontend. En Postman usa No Auth.
