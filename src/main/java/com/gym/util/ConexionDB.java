package com.gym.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/*
  Crea conexiones a MySQL leyendo la configuración desde:
    1. Variables de entorno GYM_DB_URL, GYM_DB_USER y GYM_DB_PASSWORD, o
    2. El archivo db.properties en la carpeta desde donde se ejecuta la app
       (ver db.properties.example).
  Las credenciales nunca se guardan en el código.
 */
public class ConexionDB {

    private static final String ARCHIVO_CONFIG = "db.properties";
    private static final String URL_POR_DEFECTO =
            "jdbc:mysql://localhost:3306/gimnasio_db?serverTimezone=UTC";

    private static Properties config;

    public static Connection getConexion() throws SQLException {
        Properties cfg = cargarConfig();
        String url      = leer(cfg, "GYM_DB_URL", "db.url", URL_POR_DEFECTO);
        String usuario  = leer(cfg, "GYM_DB_USER", "db.user", null);
        String password = leer(cfg, "GYM_DB_PASSWORD", "db.password", "");

        if (usuario == null) {
            throw new SQLException("Falta la configuración de la base de datos: copia "
                    + "db.properties.example como db.properties o define GYM_DB_USER y GYM_DB_PASSWORD.");
        }
        return DriverManager.getConnection(url, usuario, password);
    }

    // La variable de entorno tiene prioridad sobre el archivo
    private static String leer(Properties cfg, String variableEntorno, String clave, String porDefecto) {
        String valor = System.getenv(variableEntorno);
        if (valor == null || valor.isBlank()) {
            valor = cfg.getProperty(clave, porDefecto);
        }
        return valor;
    }

    private static synchronized Properties cargarConfig() {
        if (config == null) {
            config = new Properties();
            Path archivo = Path.of(ARCHIVO_CONFIG);
            if (Files.exists(archivo)) {
                try (InputStream in = Files.newInputStream(archivo)) {
                    config.load(in);
                } catch (IOException e) {
                    System.err.println("No se pudo leer " + ARCHIVO_CONFIG + ": " + e.getMessage());
                }
            }
        }
        return config;
    }
}
