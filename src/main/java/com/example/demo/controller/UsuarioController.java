/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author raquel
 */
@RestController
@Controller
@RequestMapping("/servicios/usuario")
@Api(tags = "Servicio CRUD de USUARIO")
@Tag(name = "Servicio CRUD de USUARIO")
public class UsuarioController {
    
    @Autowired
    UsuarioService usuarioService;

    @Operation(summary = "Crear un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado correctamente.", content = {
            @Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "400", description = "No se puede crear un Usuario", content = {
            @Content(mediaType = "application/json")})
    })
    @PostMapping("/crear")
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        try {
            return new ResponseEntity<>(usuarioService.crear(usuario), HttpStatus.CREATED);
        } catch (Exception e) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = " Buscar un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Su busqueda ha sido exitosa.", content = {
            @Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "400", description = "Error al buscar", content = {
            @Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = {
            @Content(mediaType = "application/json")})
    })
    @GetMapping("/buscarCodigo/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable("id") Long id) {
        try {
            Usuario response = usuarioService.findById(id);
            if(response == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(summary = " Listar todos los Usuarios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ejecucion exitosa.", content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "400", description = "Error al listar", content = {@Content(mediaType = "application/json")})
        
    }) 
    @GetMapping("/listAll")
    public ResponseEntity<List<Usuario>> listAll(){
        try{
            return new ResponseEntity<>(usuarioService.findAll(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(summary = " Editar usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Modificacion exitosa", content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "400", description = "Error al modificar", content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("/editar/{id}")
    public ResponseEntity<Usuario> editarUsuario(@PathVariable(name = "id", required = true)Integer id,
            @RequestBody Usuario usuario){
        try{
             //usuario.setId(id);
            return new ResponseEntity<>(usuarioService.editar(usuario), HttpStatus.OK);
        }catch(Exception ex){
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE,"NO SE PUDO EDITAR");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    
    @Operation(summary = "Eliminar usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Eliminacion exitosa", content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "400", description = "Error al modificar", content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/eliminar/{id}")
    public ResponseEntity<Usuario> eliminarUsuario (@PathVariable(name = "id", required = true)String id){
        try{
            Usuario usuario = usuarioService.findById(Long.parseLong(id));
            usuarioService.eliminar(usuario);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception ex){
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, "NO SE PUDO ELIMINAR");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
}
