package com.vsu.drunker.filestorage.configuration;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.net.UnknownHostException;


@Configuration
@Slf4j
@PropertySource({"classpath:/minio.properties"})
@EntityScan(basePackages = {"com.vsu.drunker.filestorage"})
public class MinioConfig {


    private final MinioClient minioClient;

    @Autowired
    public MinioConfig(Environment minioConf) throws UnknownHostException, InvalidPortException, InvalidEndpointException {
        String url = minioConf.getRequiredProperty("minio.client.url");
        String login = minioConf.getRequiredProperty("minio.client.access.key");
        String password = minioConf.getRequiredProperty("minio.client.secret.key");
        minioClient = new MinioClient(url, login, password);
        checkBuckets();
    }

    @Bean
    public MinioClient getMinioClient() {
        return minioClient;
    }

    private void checkBuckets() {
        for (Bucket bucket : Bucket.values()) {
            try {
                if (!minioClient.bucketExists(bucket.getBucketName())) {
                    minioClient.makeBucket(bucket.getBucketName());
                }
            } catch (Exception e) {
                log.error("Exception while create new bucket: " + bucket, e);
            }
        }
    }
}
