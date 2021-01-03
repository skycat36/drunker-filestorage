package com.vsu.drunker.filestorage.service;

import com.vsu.drunker.filestorage.configuration.Bucket;
import com.vsu.drunker.filestorage.exption.StorageMinioException;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.messages.Item;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FileStorageService {

    private final MinioClient minioClient;


    public FileStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public byte[] getArrBytesFromBucket(String name, Bucket bucket) {
        try (InputStream stream = minioClient.getObject(bucket.getBucketName(), name)) {
            return IOUtils.toByteArray(stream);
        } catch (Exception e) {
            throw new StorageMinioException("Can't get file \"" + name +"\" from bucket: " + bucket, e);
        }
    }

    public void removeFileFromBucket(String name, Bucket bucket) {
        try {
            minioClient.removeObject(bucket.getBucketName(), name);
        } catch (Exception e) {
            throw new StorageMinioException("Can't remove file \"" + name +"\" from bucket: " + bucket, e);
        }
    }

    public ObjectStat getObjectInformation(String fileName, Bucket bucket) {
        try {
             return minioClient.statObject(bucket.getBucketName(), fileName);
        } catch (Exception e) {
            throw new StorageMinioException("Can't get information about file \"" + fileName +"\" from bucket: " + bucket, e);
        }
    }

    public void putObject(byte[] fileContent, String fileName, Bucket bucket) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(fileContent)) {
            minioClient.putObject(bucket.getBucketName(), fileName, bais, Long.valueOf(bais.available()), null,
                    null, ContentType.DEFAULT_BINARY.toString());
        } catch (Exception e) {
            throw new StorageMinioException("Exception when putting a file in a file storage application", e);
        }
    }

    public List<String> getAllObjectNameFromBucket(Bucket bucket) {
        try {
            return StreamSupport.stream(minioClient.listObjects(bucket.getBucketName()).spliterator(), false)
                    .map(this::getItem).filter(Objects::nonNull).map(Item::objectName).collect(Collectors.toList());
        } catch (Exception e) {
            throw new StorageMinioException("Exception when get all file from bucket: " + bucket, e);
        }
    }

    private Item getItem(Result<Item> result) {
        try {
            return result.get();
        } catch (Exception e) {
            return null;
        }
    }

    public void removeListObjects(List<String> namesFile, Bucket bucket) {
        for (String file : namesFile) {
            try {
                minioClient.removeObject(bucket.getBucketName(), file);
            } catch (Exception ignore) { }
        }
    }
}
