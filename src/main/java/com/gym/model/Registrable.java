package com.gym.model;
 
// Interfaz para guardar y actualizar cualquier entidad del sistema
public interface Registrable<T> {
 
    void guardar(T objeto);    // Guarda un nuevo registro
 
    void actualizar(T objeto); // Actualiza un registro existente
}
 