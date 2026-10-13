package com.gym.service;

import com.gym.dao.PagoDAO;
import com.gym.model.Pago;
import com.gym.model.Cliente;
import java.util.List;
import java.util.stream.Collectors;

public class PagoService {

    private PagoDAO pagoDAO = new PagoDAO();
    private MembresiaService membresiaService = new MembresiaService();

    public boolean registrarPago(Pago pago) {
        if (pago.getCliente() == null) {
            System.out.println("Error: El pago debe estar asociado a un cliente.");
            return false;
        }
        // Regla para que no se procese un pago sin una membresia activa
        if (!membresiaService.membresiaEstaActiva(pago.getCliente())) {
            System.out.println("Error: El cliente no tiene una membresia activa.");
            return false;
        }
        if (pago.getMonto() <= 0) {
            System.out.println("Error: El monto debe ser mayor a cero.");
            return false;
        }
        if (pago.getMembresia() == null) {
            System.out.println("Error: El pago debe estar asociado a una membresia.");
            return false;
        }
        pagoDAO.guardar(pago);
        return true;
    }

    public List<Pago> listarPagosPorCliente(Cliente cliente) {
        return pagoDAO.listarTodos().stream()
            .filter(p -> p.getCliente().getId() == cliente.getId())
            .collect(Collectors.toList());
    }

    public List<Pago> listarTodos() {
        return pagoDAO.listarTodos();
    }

    public Pago buscarPago(int id) {
        return pagoDAO.buscarPorId(id);
    }

    public void eliminarPago(int id) {
        pagoDAO.eliminar(id);
    }
}