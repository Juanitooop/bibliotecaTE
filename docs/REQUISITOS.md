# Estado del avance

| Requisito | Estado y ubicación |
|---|---|
| Frontend Angular conectado al backend | ApiService usa HttpClient; proxy.conf.json envía /api a Spring Boot |
| Spring Boot compatible con NetBeans | backend/pom.xml y nbactions.xml, Java 17 |
| Base de datos y relaciones | sql/biblioteca.sql y clases modelo |
| CRUD de las cuatro entidades | Controladores y pantallas Administración/Préstamos |
| Validaciones | DTO y PrestamoService |
| Búsqueda y paginación | Repositorios, Paginas y componente paginador |
| JWT e inicio de sesión | Pendiente para la entrega final; retirado a petición del estudiante para este avance |
| Permisos ADMIN/LECTOR | Pendiente para la entrega final; los roles siguen guardados como datos |

Esta versión es un avance, no el cumplimiento completo de la seguridad del documento.
El esquema de datos se conserva. El funcionamiento de la interfaz requiere Angular, backend y base de datos encendidos.
La ejecución local anterior utilizó MariaDB de XAMPP; el documento solicita MySQL. Verifica el avance también en el motor que exige la docente.
