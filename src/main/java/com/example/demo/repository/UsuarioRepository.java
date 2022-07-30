/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.demo.repository;

import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author raquel
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    @Query(value = "SELECT * FROM Usuario where id = :id", nativeQuery = true)
    public Usuario findByCodigo(@Param("id") Long id);
    
    
}
