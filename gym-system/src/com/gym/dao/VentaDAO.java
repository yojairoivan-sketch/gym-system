package com.gym.dao;

import com.gym.model.Cliente;
import com.gym.model.Producto;
import com.gym.model.Venta;
import com.gym.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public void guardar(Venta venta) {
        String sql = "INSERT INTO ventas (fecha, hora, cantidad, precio_unitario, total, metodo_pago, activo, id_producto, id_cliente) VALUES (?, ?, ?, ?, ?, ?, 1, ?, ?)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(venta.getFecha()));
            ps.setTime(2, Time.valueOf(venta.getHora()));
            ps.setInt(3, venta.getCantidad());
            ps.setDouble(4, venta.getPrecioUnitario());
            ps.setDouble(5, venta.getTotal());
            ps.setString(6, venta.getMetodoPago());
            ps.setInt(7, venta.getProducto().getId());
            if (venta.getCliente() != null) {
                ps.setInt(8, venta.getCliente().getId());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                venta.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar venta: " + e.getMessage());
        }
    }

    public Venta buscarPorId(int id) {
        String sql = "SELECT * FROM ventas WHERE id=? AND activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return construirVenta(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar venta: " + e.getMessage());
        }
        return null;
    }

    public List<Venta> listarTodos() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(construirVenta(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar ventas: " + e.getMessage());
        }
        return lista;
    }

    public void eliminar(int id) {
        String sql = "UPDATE ventas SET activo=0 WHERE id=?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al eliminar venta: " + e.getMessage());
        }
    }

    private Venta construirVenta(ResultSet rs) throws SQLException {
        // Recupera el producto desde la BD
        ProductoDAO productoDAO = new ProductoDAO();
        Producto producto = productoDAO.buscarPorId(rs.getInt("id_producto"));

        // Recupera el cliente si existe
        Cliente cliente = null;
        int idCliente = rs.getInt("id_cliente");
        if (!rs.wasNull()) {
            ClienteDAO clienteDAO = new ClienteDAO();
            cliente = clienteDAO.buscarPorId(idCliente);
        }

        LocalDate fecha = rs.getDate("fecha").toLocalDate();
        LocalTime hora = rs.getTime("hora").toLocalTime();

        Venta venta = new Venta(
            rs.getInt("id"),
            fecha,
            hora,
            rs.getInt("cantidad"),
            rs.getString("metodo_pago"),
            producto,
            cliente
        );

        return venta;
    }
}