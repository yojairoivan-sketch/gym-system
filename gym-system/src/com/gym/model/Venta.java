package com.gym.model;

import java.time.LocalDate;
import java.time.LocalTime;

// Representa una venta de producto realizada en el gimnasio
public class Venta {

    private int id;
    private LocalDate fecha;
    private LocalTime hora;
    private int cantidad;
    private double precioUnitario;  // Precio del producto al momento de la venta
    private double total;           // cantidad * precioUnitario
    private String metodoPago;      // Efectivo, Tarjeta, Transferencia
    private boolean activo;         // Para borrado lógico
    private Producto producto;      // Producto vendido
    private Cliente cliente;        // Cliente que compró (puede ser null si es venta general)

    // Constructor completo
    public Venta(int id, LocalDate fecha, LocalTime hora, int cantidad,
                 String metodoPago, Producto producto, Cliente cliente) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
        this.total = cantidad * producto.getPrecio(); // Calcula el total automáticamente
        this.metodoPago = metodoPago;
        this.activo = true;
        this.producto = producto;
        this.cliente = cliente;
    }

    // Constructor sin ID y sin cliente (venta general sin asociar a un cliente)
    public Venta(LocalDate fecha, LocalTime hora, int cantidad,
                 String metodoPago, Producto producto) {
        this(0, fecha, hora, cantidad, metodoPago, producto, null);
    }

    // Constructor sin ID con cliente
    public Venta(LocalDate fecha, LocalTime hora, int cantidad,
                 String metodoPago, Producto producto, Cliente cliente) {
        this(0, fecha, hora, cantidad, metodoPago, producto, cliente);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}