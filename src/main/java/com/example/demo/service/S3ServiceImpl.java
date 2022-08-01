/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.service;

import com.example.demo.repository.S3Repository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author raquel
 */
@Service
public class S3ServiceImpl  implements S3Service{

    @Autowired
    S3Repository s3Repository;
    
    
    @Override
    public String uploadMultipart(String bucket, String nombreArchivo, MultipartFile archivo) {        
        return uploadFile(bucket, nombreArchivo, convertMultiPartFileToFile(archivo));
    }    
    
    @Override
    public String uploadFile(String bucket, String nombreArchivo, File archivo) {
        return s3Repository.uploadFile(bucket, nombreArchivo, archivo);
    }
    
    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            return null;
        }
        return convertedFile;
    }
}
