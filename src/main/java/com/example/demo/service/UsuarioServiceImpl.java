/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author raquel
 */
@Service
public class UsuarioServiceImpl implements UsuarioService{
    
    @Autowired
    private UsuarioRepository repositorio;

    @Override
    public Usuario crear(Usuario c) {
        return repositorio.save(c);
    }

    @Override
    public List<Usuario> findAll() {
        return repositorio.findAll();
    }

    @Override
    public Usuario editar(Usuario c) {
        return repositorio.save(c);
    }

    @Override
    public void eliminar(Usuario c) {
        repositorio.delete(c);
    }

    @Override
    public Usuario findById(Long codigo) {
        return repositorio.findByCodigo(codigo);
    }
    
}
