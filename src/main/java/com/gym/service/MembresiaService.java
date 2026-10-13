package com.gym.service;

import com.gym.dao.MembresiaDAO;
import com.gym.model.Membresia;
import com.gym.model.Cliente;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MembresiaService {

    private MembresiaDAO membresiaDAO = new MembresiaDAO();

    public boolean asignarMembresia(Membresia membresia) {
        if (membresia.getCliente() == null) {
            System.out.println("Error: La membresia debe estar asociada a un cliente.");
            return false;
        }
        if (membresia.getTipo() == null || membresia.getTipo().isBlank()) {
            System.out.println("Error: El tipo de membresia es obligatorio.");
            return false;
        }
        membresiaDAO.guardar(membresia);
        return true;
    }

    public boolean renovarMembresia(int membresiaId) {
        Membresia m = membresiaDAO.buscarPorId(membresiaId);
        if (m == null) {
            System.out.println("Error: Membresia no encontrada.");
            return false;
        }
        m.setFechaInicio(LocalDate.now());
        m.setFechaFin(LocalDate.now().plusMonths(m.getDuracion()));
        m.activar();
        membresiaDAO.actualizar(m);
        return true;
    }

    // Se Verifica si un cliente tiene membresía activa y tambien si no esta vencida
    public boolean membresiaEstaActiva(Cliente cliente) {
        return membresiaDAO.listarTodos().stream()
            .anyMatch(m -> m.getCliente().getId() == cliente.getId()
                    && m.getEstado().equals("Activa")
                    && !m.estaVencida());
    }

    // LAS Membresias que se vencen en los prpximos 7 dias
    public List<Membresia> listarProximasAVencer() {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(7);
        return membresiaDAO.listarTodos().stream()
            .filter(m -> !m.getFechaFin().isBefore(hoy)
                    && m.getFechaFin().isBefore(limite))
            .collect(Collectors.toList());
    }

    public List<Membresia> listarTodas() {
        return membresiaDAO.listarTodos();
    }

    public Membresia buscarMembresia(int id) {
        return membresiaDAO.buscarPorId(id);
    }

    public void eliminarMembresia(int id) {
        membresiaDAO.eliminar(id);
    }
}