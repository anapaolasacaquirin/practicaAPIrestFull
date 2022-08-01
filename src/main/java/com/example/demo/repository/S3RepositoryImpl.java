/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author raquel
 */
@Repository
public class S3RepositoryImpl implements S3Repository{
    
    private AmazonS3 clienteS3;
    
    @Autowired
    public S3RepositoryImpl(AmazonS3 cliente) {
        this.clienteS3 = cliente;
    }
    
    public String uploadFile(String bucket, String nombreArchivo, File archivo) {
        clienteS3.putObject(new PutObjectRequest(bucket, nombreArchivo, archivo));
        archivo.delete();
        return nombreArchivo;
    }
}
