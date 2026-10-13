package com.gym.model;
 
// Interfaz para eliminar entidades del sistema
public interface Eliminable {
 
    void eliminar(int id); // Elimina un registro por su ID (borrado lógico recomendado)
}