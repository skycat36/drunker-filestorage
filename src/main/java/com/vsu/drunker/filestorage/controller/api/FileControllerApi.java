package com.vsu.drunker.filestorage.controller.api;

import io.minio.ObjectStat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/file")
public interface FileControllerApi {

    @PostMapping("/putObject")
    ResponseEntity<String> putObject(@RequestParam MultipartFile file, @RequestParam String bucket, @RequestParam String name) throws IOException;

    @PostMapping("/removeFileFromBucket")
    ResponseEntity<String> removeFileFromBucket(@RequestParam String bucket, @RequestParam String name) throws IOException;

    @PostMapping("/getObjectInformation")
    ResponseEntity<ObjectStat> getObjectInformation(@RequestParam String bucket, @RequestParam String name) throws IOException;

    @GetMapping("/getAllObjectNameFromBucket/{bucket}")
    ResponseEntity<List<String>> getAllObjectNameFromBucket(@PathVariable String bucket) throws IOException;

    @PostMapping("/removeListObjects")
    ResponseEntity<String> removeListObjects(@RequestParam String bucket, @RequestBody List<String> names) throws IOException;

    @GetMapping("/getFile/{backet}/{name}")
    ResponseEntity<Object> getFile(@PathVariable String bucket, @PathVariable String name);
}
