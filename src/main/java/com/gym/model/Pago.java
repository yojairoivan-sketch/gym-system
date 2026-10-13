package com.gym.model;

import java.time.LocalDate;

// Representa un pago realizado por un cliente
public class Pago {

    private int id;
    private double monto;
    private LocalDate fechaPago;
    private String metodoPago;    // Efectivo, Tarjeta, Transferencia
    private String referencia;    // Número de referencia (opcional)
    private String estado;        // Completado, Pendiente
    private boolean activo;       // Para borrado lógico
    private Cliente cliente;      // Cliente que realizó el pago
    private Membresia membresia;  // Membresía que cubre este pago

    // Constructor completo
    public Pago(int id, double monto, LocalDate fechaPago, String metodoPago,
                String referencia, Cliente cliente, Membresia membresia) {
        this.id = id;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.metodoPago = metodoPago;
        this.referencia = referencia;
        this.estado = "Completado";
        this.activo = true;
        this.cliente = cliente;
        this.membresia = membresia;
    }

    // Constructor sin ID (antes de guardar en BD)
    public Pago(double monto, LocalDate fechaPago, String metodoPago,
                String referencia, Cliente cliente, Membresia membresia) {
        this(0, monto, fechaPago, metodoPago, referencia, cliente, membresia);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Membresia getMembresia() { return membresia; }
    public void setMembresia(Membresia membresia) { this.membresia = membresia; }
}