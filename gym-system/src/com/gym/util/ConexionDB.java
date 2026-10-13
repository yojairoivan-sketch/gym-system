package com.gym.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/gimnasio_db";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "1015sql2026"; 

    public static Connection getConexion() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver"); // 👈 CLAVE

        Connection conexion = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/gimnasio_db?useSSL=false&serverTimezone=UTC",
            "root",
            "1015sql2026"
        );

        System.out.println("Conexion exitosa"); // 👈 para probar
        return conexion;

    } catch (Exception e) {
        System.out.println("Error al conectar: " + e);
        return null;
    }
}
    }

