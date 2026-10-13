package com.gym.dao;

import com.gym.model.Asistencia;
import com.gym.model.Cliente;
import com.gym.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaDAO {

    public void guardar(Asistencia asistencia) {
        String sql = "INSERT INTO asistencia (cliente_id, fecha, hora_entrada, hora_salida, activo) VALUES (?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, asistencia.getCliente().getId());
            ps.setDate(2, Date.valueOf(asistencia.getFecha()));
            ps.setTime(3, Time.valueOf(asistencia.getHoraEntrada()));
            if (asistencia.getHoraSalida() != null) {
                ps.setTime(4, Time.valueOf(asistencia.getHoraSalida()));
            } else {
                ps.setNull(4, Types.TIME);
            }
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) asistencia.setId(rs.getInt(1));

        } catch (SQLException e) {
            System.out.println("Error al guardar asistencia: " + e.getMessage());
        }
    }

    public void actualizar(Asistencia asistencia) {
        String sql = "UPDATE asistencia SET hora_salida=? WHERE id=?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (asistencia.getHoraSalida() != null) {
                ps.setTime(1, Time.valueOf(asistencia.getHoraSalida()));
            } else {
                ps.setNull(1, Types.TIME);
            }
            ps.setInt(2, asistencia.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar asistencia: " + e.getMessage());
        }
    }

    public Asistencia buscarPorId(int id) {
        String sql = "SELECT * FROM asistencia WHERE id=? AND activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return construirAsistencia(rs);

        } catch (SQLException e) {
            System.out.println("Error al buscar asistencia: " + e.getMessage());
        }
        return null;
    }

    public List<Asistencia> listarTodos() {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia WHERE activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(construirAsistencia(rs));

        } catch (SQLException e) {
            System.out.println("Error al listar asistencia: " + e.getMessage());
        }
        return lista;
    }

    public void eliminar(int id) {
        String sql = "UPDATE asistencia SET activo=0 WHERE id=?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al eliminar asistencia: " + e.getMessage());
        }
    }

    private Asistencia construirAsistencia(ResultSet rs) throws SQLException {
        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente cliente = clienteDAO.buscarPorId(rs.getInt("cliente_id"));
        LocalDate fecha = rs.getDate("fecha").toLocalDate();
        LocalTime horaEntrada = rs.getTime("hora_entrada").toLocalTime();
        Time horaSalidaSQL = rs.getTime("hora_salida");
        LocalTime horaSalida = horaSalidaSQL != null ? horaSalidaSQL.toLocalTime() : null;

        return new Asistencia(
            rs.getInt("id"), fecha, horaEntrada, horaSalida, cliente
        );
    }
}
