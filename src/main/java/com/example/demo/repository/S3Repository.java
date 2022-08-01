/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.demo.repository;

import java.io.File;

/**
 *
 * @author raquel
 */
public interface S3Repository {
    
    public String uploadFile(String bucket, String nombreArchivo, File archivo);
}
