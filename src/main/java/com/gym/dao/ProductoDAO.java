package com.gym.dao;

import com.gym.model.Producto;
import com.gym.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public void guardar(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, categoria, precio, stock, stock_minimo, activo) VALUES (?, ?, ?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setString(3, producto.getCategoria());
            ps.setDouble(4, producto.getPrecio());
            ps.setInt(5, producto.getStock());
            ps.setInt(6, producto.getStockMinimo());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                producto.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar producto: " + e.getMessage());
        }
    }

    public void actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre=?, descripcion=?, categoria=?, precio=?, stock=?, stock_minimo=? WHERE id=?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setString(3, producto.getCategoria());
            ps.setDouble(4, producto.getPrecio());
            ps.setInt(5, producto.getStock());
            ps.setInt(6, producto.getStockMinimo());
            ps.setInt(7, producto.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
        }
    }

    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id=? AND activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return construirProducto(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }
        return null;
    }

    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(construirProducto(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    public void eliminar(int id) {
        String sql = "UPDATE productos SET activo=0 WHERE id=?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
        }
    }

   
private Producto construirProducto(ResultSet rs) throws SQLException {
    return new Producto(
        rs.getInt("id"),
        rs.getString("nombre"),
        rs.getString("descripcion"),
        rs.getString("categoria"),
        rs.getDouble("precio"),
        rs.getInt("stock"),
        rs.getInt("stock_minimo")
    );
}
}
