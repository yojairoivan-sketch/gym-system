package com.gym.dao;

import com.gym.model.Cliente;
import com.gym.model.Membresia;
import com.gym.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MembresiaDAO {

    public void guardar(Membresia membresia) {
        String sql = "INSERT INTO membresias (cliente_id, tipo, duracion, precio, fecha_inicio, fecha_fin, estado, activo) VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, membresia.getCliente().getId());
            ps.setString(2, membresia.getTipo());
            ps.setInt(3, membresia.getDuracion());
            ps.setDouble(4, membresia.getPrecio());
            ps.setDate(5, Date.valueOf(membresia.getFechaInicio()));
            ps.setDate(6, Date.valueOf(membresia.getFechaFin()));
            ps.setString(7, membresia.getEstado());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) membresia.setId(rs.getInt(1));

        } catch (SQLException e) {
            System.out.println("Error al guardar membresía: " + e.getMessage());
        }
    }

    public void actualizar(Membresia membresia) {
        String sql = "UPDATE membresias SET tipo=?, duracion=?, precio=?, fecha_inicio=?, fecha_fin=?, estado=? WHERE id=?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, membresia.getTipo());
            ps.setInt(2, membresia.getDuracion());
            ps.setDouble(3, membresia.getPrecio());
            ps.setDate(4, Date.valueOf(membresia.getFechaInicio()));
            ps.setDate(5, Date.valueOf(membresia.getFechaFin()));
            ps.setString(6, membresia.getEstado());
            ps.setInt(7, membresia.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar membresía: " + e.getMessage());
        }
    }

    public Membresia buscarPorId(int id) {
        String sql = "SELECT * FROM membresias WHERE id=? AND activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return construirMembresia(rs);

        } catch (SQLException e) {
            System.out.println("Error al buscar membresía: " + e.getMessage());
        }
        return null;
    }

    public List<Membresia> listarTodos() {
        List<Membresia> lista = new ArrayList<>();
        String sql = "SELECT * FROM membresias WHERE activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(construirMembresia(rs));

        } catch (SQLException e) {
            System.out.println("Error al listar membresías: " + e.getMessage());
        }
        return lista;
    }

    public void eliminar(int id) {
        String sql = "UPDATE membresias SET activo=0 WHERE id=?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al eliminar membresía: " + e.getMessage());
        }
    }

    private Membresia construirMembresia(ResultSet rs) throws SQLException {
        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente cliente = clienteDAO.buscarPorId(rs.getInt("cliente_id"));
        LocalDate fechaInicio = rs.getDate("fecha_inicio").toLocalDate();

        Membresia m = new Membresia(
            rs.getInt("id"),
            rs.getString("tipo"),
            rs.getInt("duracion"),
            rs.getDouble("precio"),
            fechaInicio,
            cliente
        );
        m.setEstado(rs.getString("estado"));
        return m;
    }
}