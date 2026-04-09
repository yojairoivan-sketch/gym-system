package com.gym.model;

// Representa un producto disponible para la venta en el gimnasio
public class Producto {

    private int id;
    private String nombre;
    private String descripcion;
    private String categoria;     // Suplemento, Bebida, etc.
    private double precio;
    private int stock;            // Cantidad disponible en inventario
    private int stockMinimo;      // Alerta cuando el stock baje de este número
    private boolean activo;       // Para borrado lógico

    // Constructor completo
    public Producto(int id, String nombre, String descripcion, String categoria,
                    double precio, int stock, int stockMinimo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.activo = true;
    }

    // Constructor sin ID (antes de guardar en BD)
    public Producto(String nombre, String descripcion, String categoria,
                    double precio, int stock, int stockMinimo) {
        this(0, nombre, descripcion, categoria, precio, stock, stockMinimo);
    }

    // Descuenta del inventario al realizar una venta
    public boolean descontarStock(int cantidad) {
        if (cantidad > this.stock) {
            return false; // No hay suficiente stock
        }
        this.stock -= cantidad;
        return true;
    }

    // Agrega stock al inventario (cuando llega mercancía)
    public void agregarStock(int cantidad) {
        this.stock += cantidad;
    }

    // Verifica si el stock está por debajo del mínimo
    public boolean stockBajo() {
        return this.stock <= this.stockMinimo;
    }

    // Verifica si hay suficiente stock para vender
    public boolean hayStock(int cantidad) {
        return this.stock >= cantidad;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}