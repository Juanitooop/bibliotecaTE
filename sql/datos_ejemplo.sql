-- Opcional: ejecutar UNA VEZ despues de biblioteca.sql, en una base vacia.
USE biblioteca;
INSERT INTO autor (nombre, apellido, nacionalidad, fecha_nacimiento) VALUES
('Gabriel', 'Garcia Marquez', 'Colombiana', '1927-03-06'),
('Isabel', 'Allende', 'Chilena', '1942-08-02');

SET @gabriel = (SELECT id FROM autor WHERE nombre = 'Gabriel' AND apellido = 'Garcia Marquez' ORDER BY id LIMIT 1);
SET @isabel = (SELECT id FROM autor WHERE nombre = 'Isabel' AND apellido = 'Allende' ORDER BY id LIMIT 1);
INSERT INTO libro (titulo, isbn, anio, autor_id) VALUES
('Cien anos de soledad', '9780307474728', 1967, @gabriel),
('El amor en los tiempos del colera', '9780307389732', 1985, @gabriel),
('Cronica de una muerte anunciada', '9781400034956', 1981, @gabriel),
('El coronel no tiene quien le escriba', '9780307475437', 1961, @gabriel),
('La casa de los espiritus', '9788401352898', 1982, @isabel),
('Eva Luna', '9788401352911', 1987, @isabel);
