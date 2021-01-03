package com.vsu.drunker.filestorage.controller;

import com.vsu.drunker.filestorage.configuration.Bucket;
import com.vsu.drunker.filestorage.controller.api.FileControllerApi;
import com.vsu.drunker.filestorage.exption.StorageMinioException;
import com.vsu.drunker.filestorage.service.FileStorageService;
import io.minio.ObjectStat;
import lombok.AllArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class FileController implements FileControllerApi {

    private final FileStorageService fileStorageService;

    @Override
    public ResponseEntity<String> putObject(@RequestParam MultipartFile file, @RequestParam String bucket, @RequestParam String name) throws IOException {
        try {
            fileStorageService.putObject(file.getBytes(), name, Bucket.valueOf(bucket.toUpperCase()));
            return ResponseEntity.ok().build();
        } catch (StorageMinioException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<String> removeFileFromBucket(@RequestParam String bucket, @RequestParam String name) throws IOException {
        try {
            fileStorageService.removeFileFromBucket(name, Bucket.valueOf(bucket.toUpperCase()));
            return ResponseEntity.ok().build();
        } catch (StorageMinioException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<ObjectStat> getObjectInformation(@RequestParam String bucket, @RequestParam String name) throws IOException {
        try {
            return ResponseEntity.ok(fileStorageService.getObjectInformation(name, Bucket.valueOf(bucket.toUpperCase())));
        } catch (StorageMinioException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<List<String>> getAllObjectNameFromBucket(@PathVariable String bucket) throws IOException {
        try {
            return ResponseEntity.ok(fileStorageService.getAllObjectNameFromBucket(Bucket.valueOf(bucket.toUpperCase())));
        } catch (StorageMinioException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<String> removeListObjects(@RequestParam String bucket, @RequestBody List<String> names) throws IOException {
        try {
            fileStorageService.removeListObjects(names, Bucket.valueOf(bucket.toUpperCase()));
            return ResponseEntity.ok().build();
        } catch (StorageMinioException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Object> getFile(@PathVariable String bucket, @PathVariable String name) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .header(org.apache.http.HttpHeaders.CACHE_CONTROL, CacheControl.noCache().getHeaderValue())
                .body(fileStorageService.getArrBytesFromBucket(name, Bucket.valueOf(bucket.toUpperCase())));
    }
}
