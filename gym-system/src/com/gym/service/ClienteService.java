package com.gym.service;

import com.gym.dao.ClienteDAO;
import com.gym.model.Cliente;
import java.util.List;

public class ClienteService {

    private ClienteDAO clienteDAO = new ClienteDAO();

    public boolean registrarCliente(Cliente cliente) {
        if (cliente.getCedula() == null || cliente.getCedula().isBlank()) {
            System.out.println("Error: La cedula es obligatoria.");
            return false;
        }
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            System.out.println("Error: El nombre es obligatorio.");
            return false;
        }
        clienteDAO.guardar(cliente);
        return true;
    }

    public boolean actualizarCliente(Cliente cliente) {
        if (cliente.getId() <= 0) {
            System.out.println("Error: ID de cliente invalido.");
            return false;
        }
        clienteDAO.actualizar(cliente);
        return true;
    }

    public Cliente buscarCliente(int id) {
        return clienteDAO.buscarPorId(id);
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listarTodos();
    }

    public void eliminarCliente(int id) {
        clienteDAO.eliminar(id);
    }
}