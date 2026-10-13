package com.gym.model;

// Representa un usuario del sistema con acceso al login
public class Usuario {

    // Roles disponibles en el sistema
    public enum Rol {
        ADMINISTRADOR,  // Acceso total
        RECEPCIONISTA,  // Clientes, asistencia y pagos
        ENTRENADOR      // Solo sus clientes asignados
    }

    private int id;
    private String username;
    private String password;
    private Rol rol;
    private boolean activo;  // Para borrado lógico

    // Constructor completo
    public Usuario(int id, String username, String password, Rol rol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.activo = true;
    }

    // Constructor sin ID (antes de guardar en BD)
    public Usuario(String username, String password, Rol rol) {
        this(0, username, password, rol);
    }

    // Verifica si la contraseña ingresada es correcta
    public boolean verificarPassword(String passwordIngresada) {
        return this.password.equals(passwordIngresada);
    }

    // Verifica si el usuario tiene un rol específico
    public boolean tieneRol(Rol rolRequerido) {
        return this.rol == rolRequerido;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}