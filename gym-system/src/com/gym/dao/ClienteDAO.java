package com.gym.dao;

import com.gym.model.Cliente;
import com.gym.model.Consultable;
import com.gym.model.Eliminable;
import com.gym.model.Registrable;
import com.gym.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements Registrable<Cliente>, Consultable<Cliente>, Eliminable {

    // Guarda un nuevo cliente en la base de datos
    @Override
    public void guardar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, apellido, cedula, fecha_nacimiento, " +
                     "telefono, correo, fecha_inscripcion, estado, activo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getCedula());
            ps.setDate(4, cliente.getFechaNacimiento() != null ?
                    Date.valueOf(cliente.getFechaNacimiento()) : null);
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getCorreo());
            ps.setDate(7, Date.valueOf(cliente.getFechaInscripcion()));
            ps.setString(8, cliente.getEstado());
            ps.executeUpdate();

            // Asigna el ID generado por MySQL al objeto
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                cliente.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar cliente: " + e.getMessage());
        }
    }

    // Actualiza los datos de un cliente existente
    @Override
    public void actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre=?, apellido=?, cedula=?, fecha_nacimiento=?, " +
                     "telefono=?, correo=?, fecha_inscripcion=?, estado=? WHERE id=?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getCedula());
            ps.setDate(4, cliente.getFechaNacimiento() != null ?
                    Date.valueOf(cliente.getFechaNacimiento()) : null);
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getCorreo());
            ps.setDate(7, Date.valueOf(cliente.getFechaInscripcion()));
            ps.setString(8, cliente.getEstado());
            ps.setInt(9, cliente.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
        }
    }

    // Busca un cliente por su ID
    @Override
    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id=? AND activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return construirCliente(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }

    // Retorna todos los clientes activos
    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE activo=1";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(construirCliente(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    // Borrado lógico — marca activo=0 en vez de borrar el registro
    @Override
    public void eliminar(int id) {
        String sql = "UPDATE clientes SET activo=0 WHERE id=?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
        }
    }

    // Convierte una fila del ResultSet en un objeto Cliente
    private Cliente construirCliente(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String cedula = rs.getString("cedula");
        Date fechaNac = rs.getDate("fecha_nacimiento");
        LocalDate fechaNacimiento = fechaNac != null ? fechaNac.toLocalDate() : null;
        String telefono = rs.getString("telefono");
        String correo = rs.getString("correo");
        LocalDate fechaInscripcion = rs.getDate("fecha_inscripcion").toLocalDate();
        String estado = rs.getString("estado");

        return new Cliente(id, nombre, apellido, cedula, fechaNacimiento,
                           telefono, correo, fechaInscripcion, estado);
    }
}