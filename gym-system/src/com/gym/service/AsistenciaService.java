package com.gym.service;

import com.gym.dao.AsistenciaDAO;
import com.gym.model.Asistencia;
import com.gym.model.Cliente;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class AsistenciaService {

    private AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
    private MembresiaService membresiaService = new MembresiaService();

    public boolean registrarEntrada(Cliente cliente) {
        if (!membresiaService.membresiaEstaActiva(cliente)) {
            System.out.println("Acceso rechazado: membresia inactiva o vencida.");
            return false;
        }

        Asistencia asistencia = new Asistencia(
            LocalDate.now(),
            LocalTime.now(),
            cliente
        );
        asistenciaDAO.guardar(asistencia);
        return true;
    }

    public boolean registrarSalida(Cliente cliente) {
        Asistencia registro = asistenciaDAO.listarTodos().stream()
            .filter(a -> a.getCliente().getId() == cliente.getId()
                    && a.getFecha().equals(LocalDate.now())
                    && !a.yaSalio())
            .findFirst()
            .orElse(null);

        if (registro == null) {
            System.out.println("No se encontro una entrada activa para este cliente hoy.");
            return false;
        }
        registro.setHoraSalida(LocalTime.now());
        asistenciaDAO.actualizar(registro);
        return true;
    }

    public List<Asistencia> listarPorFecha(LocalDate fecha) {
        return asistenciaDAO.listarTodos().stream()
            .filter(a -> a.getFecha().equals(fecha))
            .collect(Collectors.toList());
    }

    public List<Asistencia> listarPorCliente(Cliente cliente) {
        return asistenciaDAO.listarTodos().stream()
            .filter(a -> a.getCliente().getId() == cliente.getId())
            .collect(Collectors.toList());
    }
}