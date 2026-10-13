package com.gym.model;

import java.time.LocalDate;

// Representa a un cliente inscrito en el gimnasio
public class Cliente extends Persona {

    private LocalDate fechaInscripcion;
    private String estado;        // Activo, Inactivo
    private boolean activo;       // Para borrado lógico
    private Membresia membresia;  // Membresía activa del cliente

    // Constructor completo
    public Cliente(int id, String nombre, String apellido, String cedula,
                   LocalDate fechaNacimiento, String telefono, String correo,
                   LocalDate fechaInscripcion, String estado) {
        super(id, nombre, apellido, cedula, fechaNacimiento, telefono, correo);
        this.fechaInscripcion = fechaInscripcion;
        this.estado = estado;
        this.activo = true;
    }

    // Constructor sin ID (antes de guardar en BD)
    public Cliente(String nombre, String apellido, String cedula,
                   LocalDate fechaNacimiento, String telefono, String correo,
                   LocalDate fechaInscripcion, String estado) {
        super(nombre, apellido, cedula, fechaNacimiento, telefono, correo);
        this.fechaInscripcion = fechaInscripcion;
        this.estado = estado;
        this.activo = true;
    }

    // Retorna el tipo de persona
    @Override
    public String obtenerTipoPersona() {
        return "Cliente";
    }

    // Muestra la información principal del cliente
    @Override
    public String mostrarInformacion() {
        return "Cliente: " + getNombreCompleto() +
               " | Cédula: " + getCedula() +
               " | Estado: " + estado +
               " | Inscripción: " + fechaInscripcion;
    }

    // Getters y Setters
    public LocalDate getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(LocalDate fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Membresia getMembresia() { return membresia; }
    public void setMembresia(Membresia membresia) { this.membresia = membresia; }
}