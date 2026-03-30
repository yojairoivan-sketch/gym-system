package com.gym.model;

import java.time.LocalDate;

// Representa el registro de entrada/salida de un cliente
public class Asistencia {

    private int id;
    private LocalDate fecha;
    private String horaEntrada;
    private String horaSalida;   // Puede ser null si aún está en el gimnasio
    private boolean activo;      // Para borrado lógico
    private Cliente cliente;     // Cliente al que pertenece este registro

    // Constructor completo
    public Asistencia(int id, LocalDate fecha, String horaEntrada,
                      String horaSalida, Cliente cliente) {
        this.id = id;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.activo = true;
        this.cliente = cliente;
    }

    // Constructor sin ID y sin hora de salida (cuando el cliente entra)
    public Asistencia(LocalDate fecha, String horaEntrada, Cliente cliente) {
        this(0, fecha, horaEntrada, null, cliente);
    }

    // Verifica si el cliente ya salió
    public boolean yaSalio() {
        return horaSalida != null;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(String horaEntrada) { this.horaEntrada = horaEntrada; }

    public String getHoraSalida() { return horaSalida; }
    public void setHoraSalida(String horaSalida) { this.horaSalida = horaSalida; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}