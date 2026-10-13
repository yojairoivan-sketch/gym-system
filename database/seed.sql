-- =============================================================
--  GymPro - Datos de ejemplo para probar la aplicación
--  Ejecutar después de schema.sql:
--      mysql -u root -p < database/seed.sql
--  NOTA: contraseñas en texto plano solo para demo
--        (el hash con BCrypt está en el roadmap).
-- =============================================================

USE gimnasio_db;

INSERT INTO usuarios (username, password, rol) VALUES
    ('admin',      'admin123',      'ADMINISTRADOR'),
    ('recepcion',  'recepcion123',  'RECEPCIONISTA'),
    ('entrenador', 'entrenador123', 'ENTRENADOR');

INSERT INTO clientes (nombre, apellido, cedula, fecha_nacimiento, telefono, correo, fecha_inscripcion, estado) VALUES
    ('Carlos',  'Martínez', '001-1234567-1', '1995-04-12', '809-555-0101', 'carlos.martinez@example.com', CURDATE() - INTERVAL 60 DAY, 'Activo'),
    ('María',   'Gómez',    '001-2345678-2', '1998-09-30', '809-555-0102', 'maria.gomez@example.com',     CURDATE() - INTERVAL 25 DAY, 'Activo'),
    ('Luis',    'Pérez',    '001-3456789-3', '1990-01-05', '829-555-0103', 'luis.perez@example.com',      CURDATE() - INTERVAL 10 DAY, 'Activo'),
    ('Ana',     'Reyes',    '001-4567890-4', '2001-07-18', '849-555-0104', 'ana.reyes@example.com',       CURDATE() - INTERVAL 120 DAY, 'Inactivo');

INSERT INTO entrenadores (nombre, apellido, cedula, fecha_nacimiento, telefono, correo, especialidad, horario) VALUES
    ('Pedro',   'Santana',  '001-5678901-5', '1988-03-22', '809-555-0201', 'pedro.santana@example.com', 'Fuerza e hipertrofia', 'Lunes a viernes 6:00-14:00'),
    ('Laura',   'Castillo', '001-6789012-6', '1993-11-02', '809-555-0202', 'laura.castillo@example.com', 'Funcional y cardio',  'Lunes a sábado 14:00-22:00');

INSERT INTO membresias (cliente_id, tipo, duracion, precio, fecha_inicio, fecha_fin, estado) VALUES
    (1, 'Pro — $2,500/mes',    3, 7500.00, CURDATE() - INTERVAL 60 DAY,  CURDATE() + INTERVAL 30 DAY,  'Activa'),
    (2, 'Básico — $1,800/mes', 1, 1800.00, CURDATE() - INTERVAL 25 DAY,  CURDATE() + INTERVAL 5 DAY,   'Activa'),
    (3, 'Elite — $4,200/mes',  1, 4200.00, CURDATE() - INTERVAL 10 DAY,  CURDATE() + INTERVAL 20 DAY,  'Activa'),
    (4, 'Básico — $1,800/mes', 1, 1800.00, CURDATE() - INTERVAL 120 DAY, CURDATE() - INTERVAL 90 DAY,  'Vencida');

INSERT INTO pagos (cliente_id, membresia_id, monto, fecha_pago, metodo_pago, referencia, estado) VALUES
    (1, 1, 7500.00, CURDATE() - INTERVAL 60 DAY,  'Tarjeta',       'TRX-10021', 'Completado'),
    (2, 2, 1800.00, CURDATE() - INTERVAL 25 DAY,  'Efectivo',      NULL,        'Completado'),
    (3, 3, 4200.00, CURDATE() - INTERVAL 10 DAY,  'Transferencia', 'TRF-55810', 'Completado'),
    (4, 4, 1800.00, CURDATE() - INTERVAL 120 DAY, 'Efectivo',      NULL,        'Completado');

INSERT INTO asistencia (cliente_id, fecha, hora_entrada, hora_salida) VALUES
    (1, CURDATE() - INTERVAL 1 DAY, '07:05:00', '08:30:00'),
    (2, CURDATE() - INTERVAL 1 DAY, '18:10:00', '19:25:00'),
    (3, CURDATE(),                  '06:45:00', NULL);

INSERT INTO productos (nombre, descripcion, categoria, precio, stock, stock_minimo) VALUES
    ('Proteína Whey 2 lb',    'Proteína de suero sabor chocolate', 'Suplemento', 2950.00, 12, 5),
    ('Creatina 300 g',        'Creatina monohidratada',            'Suplemento', 1650.00,  4, 5),
    ('Agua 500 ml',           'Agua purificada',                   'Bebida',       35.00, 48, 20),
    ('Bebida isotónica',      'Bebida hidratante 600 ml',          'Bebida',       90.00, 30, 10),
    ('Guantes de entrenamiento', 'Talla M, antideslizantes',       'Accesorio',   850.00,  8, 3),
    ('Camiseta GymPro',       'Camiseta dry-fit con logo',         'Ropa',        700.00, 15, 5);

INSERT INTO ventas (fecha, hora, cantidad, precio_unitario, total, metodo_pago, id_producto, id_cliente) VALUES
    (CURDATE() - INTERVAL 2 DAY, '08:40:00', 1, 2950.00, 2950.00, 'Tarjeta',  1, 1),
    (CURDATE() - INTERVAL 1 DAY, '19:30:00', 2,   90.00,  180.00, 'Efectivo', 4, 2),
    (CURDATE(),                  '07:00:00', 3,   35.00,  105.00, 'Efectivo', 3, NULL);
