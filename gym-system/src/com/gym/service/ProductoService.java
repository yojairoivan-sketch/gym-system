package com.gym.service;

import com.gym.dao.ProductoDAO;
import com.gym.model.Producto;
import java.util.List;
import java.util.stream.Collectors;

public class ProductoService {

    private ProductoDAO productoDAO = new ProductoDAO();

    public boolean registrarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            System.out.println("Error: El nombre del producto es obligatorio.");
            return false;
        }
        if (producto.getPrecio() <= 0) {
            System.out.println("Error: El precio debe ser mayor a cero.");
            return false;
        }
        if (producto.getStock() < 0) {
            System.out.println("Error: El stock no puede ser negativo.");
            return false;
        }
        productoDAO.guardar(producto);
        return true;
    }

    public boolean actualizarProducto(Producto producto) {
        if (producto.getId() <= 0) {
            System.out.println("Error: ID de producto invalido.");
            return false;
        }
        productoDAO.actualizar(producto);
        return true;
    }

    public Producto buscarProducto(int id) {
        return productoDAO.buscarPorId(id);
    }

    public List<Producto> listarTodos() {
        return productoDAO.listarTodos();
    }

    // Los Prpductos con mercancia por debajo del minimo
    public List<Producto> listarConStockBajo() {
        return productoDAO.listarTodos().stream()
            .filter(Producto::stockBajo)
            .collect(Collectors.toList());
    }

    public void eliminarProducto(int id) {
        productoDAO.eliminar(id);
    }
}