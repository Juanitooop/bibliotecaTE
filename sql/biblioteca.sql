-- Ejecutar primero este archivo en MySQL 8 o superior.
CREATE DATABASE IF NOT EXISTS biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE biblioteca;

CREATE TABLE IF NOT EXISTS autor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    nacionalidad VARCHAR(80) NOT NULL,
    fecha_nacimiento DATE NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS libro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    anio INT NOT NULL,
    autor_id BIGINT NOT NULL,
    CONSTRAINT fk_libro_autor FOREIGN KEY (autor_id) REFERENCES autor(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    rol ENUM('ADMIN', 'LECTOR') NOT NULL,
    estado ENUM('ACTIVO', 'INACTIVO') NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS prestamo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    libro_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion DATE NOT NULL,
    devuelto BIT(1) NOT NULL DEFAULT b'0',
    CONSTRAINT fk_prestamo_libro FOREIGN KEY (libro_id) REFERENCES libro(id),
    CONSTRAINT fk_prestamo_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT ck_fechas CHECK (fecha_devolucion >= fecha_prestamo),
    INDEX idx_prestamo_estado (libro_id, devuelto)
) ENGINE=InnoDB;

-- Las cuentas de ejemplo se crean desde Java con la clave guardada como hash.
-- No se borran tablas ni registros al ejecutar este archivo.
