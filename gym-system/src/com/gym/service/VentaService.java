package com.gym.service;

import com.gym.dao.VentaDAO;
import com.gym.dao.ProductoDAO;
import com.gym.model.Venta;
import com.gym.model.Producto;
import java.util.List;

public class VentaService {

    private VentaDAO ventaDAO = new VentaDAO();
    private ProductoDAO productoDAO = new ProductoDAO();

    public boolean registrarVenta(Venta venta) {
        if (venta.getProducto() == null) {
            System.out.println("Error: La venta debe tener un producto asociado.");
            return false;
        }
        if (venta.getCantidad() <= 0) {
            System.out.println("Error: La cantidad debe ser mayor a cero.");
            return false;
        }

        Producto producto = venta.getProducto();
        if (!producto.hayStock(venta.getCantidad())) {
            System.out.println("Error: Stock insuficiente. Disponible: " + producto.getStock());
            return false;
        }

        // Descuenta el stock automaticamente y actualiza en la BD
        producto.descontarStock(venta.getCantidad());
        productoDAO.actualizar(producto);

        ventaDAO.guardar(venta);
        return true;
    }

    public List<Venta> listarTodas() {
        return ventaDAO.listarTodos();
    }

    public Venta buscarVenta(int id) {
        return ventaDAO.buscarPorId(id);
    }

    public void eliminarVenta(int id) {
        ventaDAO.eliminar(id);
    }
}