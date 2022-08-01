/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.controller;

import com.amazonaws.util.StringUtils;
import com.example.demo.model.Usuario;
import com.example.demo.service.S3Service;
import com.example.demo.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    
    @Autowired
    S3Service s3Service;
    
    @Value("${app.bucket}")
    private String bucket;
    
    //Separador de archivos del sistema
    private String sep = File.separator;

    @Operation(summary = "Crear un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado correctamente.", content = {
            @Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "400", description = "No se puede crear un Usuario", content = {
            @Content(mediaType = "application/json"),
        }),
        @ApiResponse(responseCode = "428", description = "No se encuentra la foto para subirla a s3", content = {
            @Content(mediaType = "application/json"),
        }),
        @ApiResponse(responseCode = "412", description = "No se encuenta la cedula para subirla a s3", content = {
            @Content(mediaType = "application/json"),
        })
    })
    @PostMapping("/crear")
    public ResponseEntity<Usuario> crear(@RequestParam(value = "clave") String clave, @RequestParam(value = "email") String email,
            @RequestParam(value = "estado") String estado, @RequestParam(value = "nombre") String nombre, 
            @RequestPart(value = "foto", required = true) MultipartFile foto, @RequestPart(value = "cedula", required = true) MultipartFile cedula) {
        try {
            
            Usuario usuario = new Usuario();
            usuario.setId(0l);
            usuario.setClave(clave);
            usuario.setEmail(email);
            usuario.setEstado(estado);
            usuario.setNombre(nombre);
            
            
            
            //Validar si el archivo es del tipo correcto.
            if(!(StringUtils.lowerCase(foto.getOriginalFilename()).endsWith("jpg") || 
                    StringUtils.lowerCase(foto.getOriginalFilename()).endsWith("jpeg") || 
                    StringUtils.lowerCase(foto.getOriginalFilename()).endsWith("png") )){
                return new ResponseEntity<>(new Usuario("Formato de imagen no admitido (jpg, jpeg, png)"), HttpStatus.BAD_REQUEST);
            }
           
            if(!(StringUtils.lowerCase(cedula.getOriginalFilename()).endsWith(".pdf"))){
                return new ResponseEntity<>(new Usuario("La cédula debe ser en formato PDF"), HttpStatus.BAD_REQUEST);
            }            
            
            String keyFoto = s3Service.uploadMultipart(bucket, foto.getOriginalFilename(), foto);
            String keyCedula = s3Service.uploadMultipart(bucket, cedula.getOriginalFilename(), cedula);
            
            usuario.setUriFoto(getURLS3(keyFoto));
            usuario.setUriCedula(getURLS3(keyCedula));
            usuario = usuarioService.crear(usuario);
            
            return new ResponseEntity<>(usuario, HttpStatus.CREATED);
        } catch (Exception e) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            Usuario response = new Usuario();
            response.setMensaje("Error en la creación.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    } 
    
    public String getURLS3(String key){
        return String.format("https://%s.s3.us-east-2.amazonaws.com/%s", bucket, key);
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
    public ResponseEntity<Usuario> editarUsuario(@PathVariable(name = "id", required = true)Long id,
            @RequestParam(value = "clave") String clave, @RequestParam(value = "email") String email,
            @RequestParam(value = "estado") String estado, @RequestParam(value = "nombre") String nombre,
            @RequestPart(value = "foto", required = true) MultipartFile foto, @RequestPart(value = "cedula", required = true) MultipartFile cedula){
        try{
            
            Usuario usuario = new Usuario();
            usuario.setId(id);
            usuario.setClave(clave);
            usuario.setEmail(email);
            usuario.setEstado(estado);
            usuario.setNombre(nombre);
            
            //Validar si el archivo es del tipo correcto.
            if(!(StringUtils.lowerCase(foto.getOriginalFilename()).endsWith("jpg") || 
                    StringUtils.lowerCase(foto.getOriginalFilename()).endsWith("jpeg") || 
                    StringUtils.lowerCase(foto.getOriginalFilename()).endsWith("png") )){
                return new ResponseEntity<>(new Usuario("Formato de imagen no admitido (jpg, jpeg, png)"), HttpStatus.BAD_REQUEST);
            }
           
            if(!(StringUtils.lowerCase(cedula.getOriginalFilename()).endsWith(".pdf"))){
                return new ResponseEntity<>(new Usuario("La cédula debe ser en formato PDF"), HttpStatus.BAD_REQUEST);
            }            
            
            String keyFoto = s3Service.uploadMultipart(bucket, foto.getOriginalFilename(), foto);
            String keyCedula = s3Service.uploadMultipart(bucket, cedula.getOriginalFilename(), cedula);
            
            usuario.setUriFoto(getURLS3(keyFoto));
            usuario.setUriCedula(getURLS3(keyCedula));
            
            
            
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
