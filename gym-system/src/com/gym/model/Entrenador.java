package com.gym.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Representa a un entrenador del gimnasio
public class Entrenador extends Persona {

    private String especialidad;
    private String horario;
    private boolean activo;                    // Para borrado lógico
    private List<Cliente> clientesAsignados;   // Clientes a su cargo

    // Constructor completo
    public Entrenador(int id, String nombre, String apellido, String cedula,
                      LocalDate fechaNacimiento, String telefono, String correo,
                      String especialidad, String horario) {
        super(id, nombre, apellido, cedula, fechaNacimiento, telefono, correo);
        this.especialidad = especialidad;
        this.horario = horario;
        this.activo = true;
        this.clientesAsignados = new ArrayList<>();
    }

    // Constructor sin ID (antes de guardar en BD)
    public Entrenador(String nombre, String apellido, String cedula,
                      LocalDate fechaNacimiento, String telefono, String correo,
                      String especialidad, String horario) {
        super(nombre, apellido, cedula, fechaNacimiento, telefono, correo);
        this.especialidad = especialidad;
        this.horario = horario;
        this.activo = true;
        this.clientesAsignados = new ArrayList<>();
    }

    // Asigna un cliente a este entrenador
    public void agregarCliente(Cliente cliente) {
        clientesAsignados.add(cliente);
    }

    // Retorna el tipo de persona
    @Override
    public String obtenerTipoPersona() {
        return "Entrenador";
    }

    // Muestra la información principal del entrenador
    @Override
    public String mostrarInformacion() {
        return "Entrenador: " + getNombreCompleto() +
               " | Cédula: " + getCedula() +
               " | Especialidad: " + especialidad +
               " | Horario: " + horario;
    }

    // Getters y Setters
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public List<Cliente> getClientesAsignados() { return clientesAsignados; }
    public void setClientesAsignados(List<Cliente> clientesAsignados) { this.clientesAsignados = clientesAsignados; }
}