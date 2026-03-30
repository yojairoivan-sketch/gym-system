package com.gym.model;
 
import java.util.List;
 
// Interfaz para consultar entidades del sistema
public interface Consultable<T> {
 
    T buscarPorId(int id);  // Busca un registro por su ID
 
    List<T> listarTodos();  // Retorna todos los registros
}
 