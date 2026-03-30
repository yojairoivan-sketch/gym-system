package com.gym.model;

import java.time.LocalDate;
import java.time.Period;
 
/*
  Clase abstracta que representa a cualquier persona dentro del sistema GymPro.
  Es la clase base de Cliente y Entrenador.
 */

public abstract class Persona {
 
    //Atributos
 
    private int id;
    private String nombre;
    private String apellido;
    private String cedula;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String correo;
 
    //Constructores
 
    /*
     Constructor completo.
     */
    public Persona(int id, String nombre, String apellido, String cedula,
                   LocalDate fechaNacimiento, String telefono, String correo) {
        this.id              = id;
        this.nombre          = nombre;
        this.apellido        = apellido;
        this.cedula          = cedula;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono        = telefono;
        this.correo          = correo;
    }
 
    /*
     Constructor sin ID.
     */
    public Persona(String nombre, String apellido, String cedula,
                   LocalDate fechaNacimiento, String telefono, String correo) {
        this(0, nombre, apellido, cedula, fechaNacimiento, telefono, correo);
    }
 
    //Métodos Abstractos
 
    /*
     Retorna el tipo de persona (ej: "Cliente", "Entrenador").
     Cada subclase debe implementarlo.
     */
    public abstract String obtenerTipoPersona();
 
    /*
      Retorna un String con la información relevante de la persona.
      Cada subclase puede personalizar el formato.
     */
    public abstract String mostrarInformacion();
 
    // Métodos Concretos
 
    /*
      Calcula la edad actual de la persona a partir de su fecha de nacimiento.
     */
    public int calcularEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
 
    /*
      Retorna el nombre completo (nombre + apellido).
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
 
    /*
      Representación básica en texto del objeto.
     */
    @Override
    public String toString() {
        return obtenerTipoPersona() + " | " + getNombreCompleto() + " | Cédula: " + cedula;
    }
 
    // ─── Getters y Setters ────────────────────────────────────────────────────────
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
 
    public String getApellido() {
        return apellido;
    }
 
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
 
    public String getCedula() {
        return cedula;
    }
 
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
 
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
 
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
 
    public String getTelefono() {
        return telefono;
    }
 
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
 
    public String getCorreo() {
        return correo;
    }
 
    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
 