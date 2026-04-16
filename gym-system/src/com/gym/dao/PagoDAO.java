package com.gym.dao;

import com.gym.model.Cliente;
import com.gym.model.Membresia;
import com.gym.model.Pago;
import com.gym.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    public void guardar(Pago pago) {
        String sql = "INSERT INTO pagos (cliente_id, membresia_id, monto, fecha_pago, metodo_pago, referencia, estado, activo) VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pago.getCliente().getId());
            ps.setInt(2, pago.getMembresia().getId());
            ps.setDouble(3, pago.getMonto());
            ps.setDate(4, Date.valueOf(pago.getFechaPago()));
            ps.setString(5, pago.getMetodoPago());
            ps.setString(6, pago.getReferencia());
            ps.setString(7, pago.getEstado());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) pago.setId(rs.getInt(1));

        } catch (SQLException e) {
            System.out.println("Error al guardar pago: " + e.getMessage());
        }
    }

    public void actualizar(Pago pago) {
        String sql = "UPDATE pagos SET monto=?, fecha_pago=?, metodo_pago=?, referencia=?, estado=? WHERE id=?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, pago.getMonto());
            ps.setDate(2, Date.valueOf(pago.getFechaPago()));
            ps.setString(3, pago.getMetodoPago());
            ps.setString(4, pago.getReferencia());
            ps.setString(5, pago.getEstado());
            ps.setInt(6, pago.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar pago: " + e.getMessage());
        }
    }

    public Pago buscarPorId(int id) {
        String sql = "SELECT * FROM pagos WHERE id=? AND activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return construirPago(rs);

        } catch (SQLException e) {
            System.out.println("Error al buscar pago: " + e.getMessage());
        }
        return null;
    }

    public List<Pago> listarTodos() {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos WHERE activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(construirPago(rs));

        } catch (SQLException e) {
            System.out.println("Error al listar pagos: " + e.getMessage());
        }
        return lista;
    }

    public void eliminar(int id) {
        String sql = "UPDATE pagos SET activo=0 WHERE id=?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al eliminar pago: " + e.getMessage());
        }
    }

    private Pago construirPago(ResultSet rs) throws SQLException {
        ClienteDAO clienteDAO = new ClienteDAO();
        MembresiaDAO membresiaDAO = new MembresiaDAO();
        Cliente cliente = clienteDAO.buscarPorId(rs.getInt("cliente_id"));
        Membresia membresia = membresiaDAO.buscarPorId(rs.getInt("membresia_id"));
        LocalDate fechaPago = rs.getDate("fecha_pago").toLocalDate();

        Pago p = new Pago(
            rs.getInt("id"),
            rs.getDouble("monto"),
            fechaPago,
            rs.getString("metodo_pago"),
            rs.getString("referencia"),
            cliente,
            membresia
        );
        p.setEstado(rs.getString("estado"));
        return p;
    }
}