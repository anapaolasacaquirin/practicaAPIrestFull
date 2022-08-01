/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.service;

import java.io.File;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author raquel
 */
public interface S3Service {
    String uploadMultipart(String bucket, String nombreArchivo, MultipartFile archivo);
    
    String uploadFile(String bucket, String nombreArchivo, File archivo);
}
