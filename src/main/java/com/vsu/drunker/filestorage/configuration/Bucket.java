package com.vsu.drunker.filestorage.configuration;

public enum Bucket {

    TEA("tea"),
    DEFAULT("default");

    private final String bucketName;

    Bucket(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
