package com.vsu.drunker.filestorage.controller;

import com.vsu.drunker.filestorage.configuration.Bucket;
import com.vsu.drunker.filestorage.exption.StorageMinioException;
import com.vsu.drunker.filestorage.service.FileStorageService;
import io.minio.ObjectStat;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/file")
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/putObject")
    public ResponseEntity<String> putObject(@RequestParam MultipartFile file, @RequestParam String bucket, @RequestParam String name) throws IOException {
        try {
            fileStorageService.putObject(file.getBytes(), name, Bucket.valueOf(bucket.toUpperCase()));
            return ResponseEntity.ok().build();
        } catch (StorageMinioException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/removeFileFromBucket")
    public ResponseEntity<String> removeFileFromBucket(@RequestParam String bucket, @RequestParam String name) throws IOException {
        try {
            fileStorageService.removeFileFromBucket(name, Bucket.valueOf(bucket.toUpperCase()));
            return ResponseEntity.ok().build();
        } catch (StorageMinioException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/getObjectInformation")
    public ResponseEntity<ObjectStat> getObjectInformation(@RequestParam String bucket, @RequestParam String name) throws IOException {
        try {
            return ResponseEntity.ok(fileStorageService.getObjectInformation(name, Bucket.valueOf(bucket.toUpperCase())));
        } catch (StorageMinioException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/getAllObjectNameFromBucket")
    public ResponseEntity<List<String>> getAllObjectNameFromBucket(@RequestParam String bucket, @RequestParam String name) throws IOException {
        try {
            return ResponseEntity.ok(fileStorageService.getAllObjectNameFromBucket(Bucket.valueOf(bucket.toUpperCase())));
        } catch (StorageMinioException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/removeListObjects")
    public ResponseEntity<String> removeListObjects(@RequestParam String bucket, @RequestBody List<String> names) throws IOException {
        try {
            fileStorageService.removeListObjects(names, Bucket.valueOf(bucket.toUpperCase()));
            return ResponseEntity.ok().build();
        } catch (StorageMinioException ex){
            return ResponseEntity.badRequest().build();
        }
    }



}
