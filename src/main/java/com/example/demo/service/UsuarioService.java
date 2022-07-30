/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.demo.service;

import com.example.demo.model.Usuario;
import java.util.List;

/**
 *
 * @author raquel
 */
public interface UsuarioService {
    public Usuario crear(Usuario c);//CREATE
    public List<Usuario> findAll();//READ
    public Usuario editar(Usuario c);//UPDATE
    public void eliminar(Usuario c);//DELETE
    public Usuario findById(Long codigo);//READ
    
}
