package com.gym.dao;

import com.gym.model.Entrenador;
import com.gym.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EntrenadorDAO {

    public void guardar(Entrenador entrenador) {
        String sql = "INSERT INTO entrenadores (nombre, apellido, cedula, fecha_nacimiento, telefono, correo, especialidad, horario, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entrenador.getNombre());
            ps.setString(2, entrenador.getApellido());
            ps.setString(3, entrenador.getCedula());
            ps.setDate(4, entrenador.getFechaNacimiento() != null ?
                    Date.valueOf(entrenador.getFechaNacimiento()) : null);
            ps.setString(5, entrenador.getTelefono());
            ps.setString(6, entrenador.getCorreo());
            ps.setString(7, entrenador.getEspecialidad());
            ps.setString(8, entrenador.getHorario());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) entrenador.setId(rs.getInt(1));

        } catch (SQLException e) {
            System.out.println("Error al guardar entrenador: " + e.getMessage());
        }
    }

    public void actualizar(Entrenador entrenador) {
        String sql = "UPDATE entrenadores SET nombre=?, apellido=?, cedula=?, fecha_nacimiento=?, telefono=?, correo=?, especialidad=?, horario=? WHERE id=?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entrenador.getNombre());
            ps.setString(2, entrenador.getApellido());
            ps.setString(3, entrenador.getCedula());
            ps.setDate(4, entrenador.getFechaNacimiento() != null ?
                    Date.valueOf(entrenador.getFechaNacimiento()) : null);
            ps.setString(5, entrenador.getTelefono());
            ps.setString(6, entrenador.getCorreo());
            ps.setString(7, entrenador.getEspecialidad());
            ps.setString(8, entrenador.getHorario());
            ps.setInt(9, entrenador.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar entrenador: " + e.getMessage());
        }
    }

    public Entrenador buscarPorId(int id) {
        String sql = "SELECT * FROM entrenadores WHERE id=? AND activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return construirEntrenador(rs);

        } catch (SQLException e) {
            System.out.println("Error al buscar entrenador: " + e.getMessage());
        }
        return null;
    }

    public List<Entrenador> listarTodos() {
        List<Entrenador> lista = new ArrayList<>();
        String sql = "SELECT * FROM entrenadores WHERE activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(construirEntrenador(rs));

        } catch (SQLException e) {
            System.out.println("Error al listar entrenadores: " + e.getMessage());
        }
        return lista;
    }

    public void eliminar(int id) {
        String sql = "UPDATE entrenadores SET activo=0 WHERE id=?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al eliminar entrenador: " + e.getMessage());
        }
    }

    private Entrenador construirEntrenador(ResultSet rs) throws SQLException {
        Date fechaNacSQL = rs.getDate("fecha_nacimiento");
        LocalDate fechaNac = fechaNacSQL != null ? fechaNacSQL.toLocalDate() : null;

        return new Entrenador(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getString("cedula"),
            fechaNac,
            rs.getString("telefono"),
            rs.getString("correo"),
            rs.getString("especialidad"),
            rs.getString("horario")
        );
    }
}