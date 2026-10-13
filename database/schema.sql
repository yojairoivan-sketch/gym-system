-- =============================================================
--  GymPro - Esquema de base de datos (MySQL 8+)
--  Uso:  mysql -u root -p < database/schema.sql
--  Todas las tablas usan borrado lógico con la columna `activo`.
-- =============================================================

CREATE DATABASE IF NOT EXISTS gimnasio_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gimnasio_db;

-- Usuarios que inician sesión en el sistema
CREATE TABLE IF NOT EXISTS usuarios (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(50)  NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    rol       ENUM('ADMINISTRADOR', 'RECEPCIONISTA', 'ENTRENADOR') NOT NULL,
    activo    TINYINT(1)   NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS clientes (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    nombre             VARCHAR(80)  NOT NULL,
    apellido           VARCHAR(80)  NOT NULL,
    cedula             VARCHAR(20)  NOT NULL UNIQUE,
    fecha_nacimiento   DATE,
    telefono           VARCHAR(20),
    correo             VARCHAR(120),
    fecha_inscripcion  DATE         NOT NULL,
    estado             VARCHAR(20)  NOT NULL DEFAULT 'Activo',   -- Activo, Inactivo
    activo             TINYINT(1)   NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS entrenadores (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    nombre             VARCHAR(80)  NOT NULL,
    apellido           VARCHAR(80)  NOT NULL,
    cedula             VARCHAR(20)  NOT NULL UNIQUE,
    fecha_nacimiento   DATE,
    telefono           VARCHAR(20),
    correo             VARCHAR(120),
    especialidad       VARCHAR(80),
    horario            VARCHAR(80),
    activo             TINYINT(1)   NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS membresias (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id    INT            NOT NULL,
    tipo          VARCHAR(50)    NOT NULL,
    duracion      INT            NOT NULL,                       -- en meses
    precio        DECIMAL(10, 2) NOT NULL,
    fecha_inicio  DATE           NOT NULL,
    fecha_fin     DATE           NOT NULL,
    estado        VARCHAR(20)    NOT NULL DEFAULT 'Activa',      -- Activa, Vencida, Suspendida
    activo        TINYINT(1)     NOT NULL DEFAULT 1,
    CONSTRAINT fk_membresia_cliente FOREIGN KEY (cliente_id) REFERENCES clientes (id)
);

CREATE TABLE IF NOT EXISTS pagos (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id    INT            NOT NULL,
    membresia_id  INT            NOT NULL,
    monto         DECIMAL(10, 2) NOT NULL,
    fecha_pago    DATE           NOT NULL,
    metodo_pago   VARCHAR(20)    NOT NULL,                       -- Efectivo, Tarjeta, Transferencia
    referencia    VARCHAR(50),
    estado        VARCHAR(20)    NOT NULL DEFAULT 'Completado',  -- Completado, Pendiente
    activo        TINYINT(1)     NOT NULL DEFAULT 1,
    CONSTRAINT fk_pago_cliente   FOREIGN KEY (cliente_id)   REFERENCES clientes (id),
    CONSTRAINT fk_pago_membresia FOREIGN KEY (membresia_id) REFERENCES membresias (id)
);

CREATE TABLE IF NOT EXISTS asistencia (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id    INT        NOT NULL,
    fecha         DATE       NOT NULL,
    hora_entrada  TIME       NOT NULL,
    hora_salida   TIME,                                          -- NULL mientras el cliente sigue en el gimnasio
    activo        TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_asistencia_cliente FOREIGN KEY (cliente_id) REFERENCES clientes (id)
);

CREATE TABLE IF NOT EXISTS productos (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(100)   NOT NULL,
    descripcion   VARCHAR(255),
    categoria     VARCHAR(30)    NOT NULL,                       -- Suplemento, Bebida, Accesorio, Ropa, Otro
    precio        DECIMAL(10, 2) NOT NULL,
    stock         INT            NOT NULL DEFAULT 0,
    stock_minimo  INT            NOT NULL DEFAULT 0,
    activo        TINYINT(1)     NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS ventas (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    fecha            DATE           NOT NULL,
    hora             TIME           NOT NULL,
    cantidad         INT            NOT NULL,
    precio_unitario  DECIMAL(10, 2) NOT NULL,
    total            DECIMAL(10, 2) NOT NULL,
    metodo_pago      VARCHAR(20)    NOT NULL,
    activo           TINYINT(1)     NOT NULL DEFAULT 1,
    id_producto      INT            NOT NULL,
    id_cliente       INT,                                        -- NULL = venta sin cliente registrado
    CONSTRAINT fk_venta_producto FOREIGN KEY (id_producto) REFERENCES productos (id),
    CONSTRAINT fk_venta_cliente  FOREIGN KEY (id_cliente)  REFERENCES clientes (id)
);
