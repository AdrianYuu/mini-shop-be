package com.adrian.minishop.service;

import com.adrian.minishop.exception.FileStorageException;
import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Getter
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.url}")
    private String url;

    @Value("${minio.buckets.user}")
    private String userBucket;

    @Value("${minio.buckets.product}")
    private String productBucket;

    @PostConstruct
    public void init() {
        try {
            ensureBucket(userBucket);
            ensureBucket(productBucket);
        } catch (Exception e) {
            throw new IllegalStateException("Minio error", e);
        }
    }

    public void ensureBucket(String bucketName) throws Exception {
        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build());

        if (!isBucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }
    }

    public String uploadFile(MultipartFile file, String bucketName) {
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String extension = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf('.'))
                    : "";
            String objectName = UUID.randomUUID() + extension;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return String.format("%s/%s", bucketName, objectName);
        } catch (Exception e) {
            throw new FileStorageException("Upload file failed", e);
        }
    }

    public String getPresignedUrl(String bucketName, String objectName, int expirySeconds) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expirySeconds, TimeUnit.SECONDS)
                            .build()
            );
        } catch (Exception e) {
            throw new FileStorageException("Get presigned url failed", e);
        }
    }

    public void removeFile(String key) {
        try {
            String[] parts = key.split("/", 2);

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(parts[0])
                            .object(parts[1])
                            .build()
            );
        } catch (Exception e) {
            throw new FileStorageException("Remove file failed", e);
        }
    }

}
