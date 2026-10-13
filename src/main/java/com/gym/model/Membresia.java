package com.gym.model;

import java.time.LocalDate;

// Representa una membresía asignada a un cliente
public class Membresia {

    private int id;
    private String tipo;          // Mensual, Trimestral, Anual
    private int duracion;         // Duración en meses
    private double precio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;        // Activa, Vencida, Suspendida
    private boolean activo;       // Para borrado lógico
    private Cliente cliente;      // Cliente al que pertenece

    // Constructor completo
    public Membresia(int id, String tipo, int duracion, double precio,
                     LocalDate fechaInicio, Cliente cliente) {
        this.id = id;
        this.tipo = tipo;
        this.duracion = duracion;
        this.precio = precio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaInicio.plusMonths(duracion); // Calcula fecha fin automáticamente
        this.estado = "Activa";
        this.activo = true;
        this.cliente = cliente;
    }

    // Constructor sin ID (antes de guardar en BD)
    public Membresia(String tipo, int duracion, double precio,
                     LocalDate fechaInicio, Cliente cliente) {
        this(0, tipo, duracion, precio, fechaInicio, cliente);
    }

    // Marca la membresía como vencida
    public void vencer() {
        this.estado = "Vencida";
    }

    // Marca la membresía como activa
    public void activar() {
        this.estado = "Activa";
    }

    // Verifica si la membresía ya venció
    public boolean estaVencida() {
        return LocalDate.now().isAfter(fechaFin);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}