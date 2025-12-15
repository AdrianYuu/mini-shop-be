package com.adrian.minishop.service;

import com.adrian.minishop.core.exception.FileStorageException;
import com.adrian.minishop.util.FileUtil;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Getter
public class MinioService {

    private final MinioClient minioClient;

    private final MinioClient publicMinioClient;

    private final FileUtil fileUtil;

    @Value("${minio.buckets.user}")
    private String userBucket;

    @Value("${minio.buckets.product}")
    private String productBucket;

    public MinioService(
            @Qualifier("minioClient") MinioClient minioClient,
            @Qualifier("publicMinioClient") MinioClient publicMinioClient,
            FileUtil fileUtil
    ) {
        this.minioClient = minioClient;
        this.publicMinioClient = publicMinioClient;
        this.fileUtil = fileUtil;
    }

    @PostConstruct
    public void init() {
        ensureBucketExists(userBucket);
        ensureBucketExists(productBucket);
    }

    private void ensureBucketExists(String bucketName) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());

            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }
        } catch (Exception e) {
            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to ensure bucket: " + bucketName);
        }
    }

    public String uploadFile(MultipartFile file, String bucketName) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException(HttpStatus.BAD_REQUEST, "File is required");
        }

        try {
            String extension = fileUtil.getFileExtension(file.getOriginalFilename());
            String objectName = UUID.randomUUID() + extension;

            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(inputStream, file.getSize(), 10 * 1024 * 1024)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            return bucketName + "/" + objectName;
        } catch (IOException e) {
            throw new FileStorageException(HttpStatus.BAD_REQUEST, "Failed to read file");
        } catch (Exception e) {
            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file to bucket: " + bucketName);
        }
    }

    public String getPresignedUrl(String bucketName, String objectName, int expirySeconds) {
        try {
            return publicMinioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expirySeconds, TimeUnit.SECONDS)
                            .build()
            );
        } catch (ErrorResponseException e) {
            throw new FileStorageException(HttpStatus.NOT_FOUND, "Bucket not found: " + bucketName);
        } catch (Exception e) {
            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate presigned URL for: " + objectName);
        }
    }

    public void removeFile(String key) {
        if (key == null || !key.contains("/")) {
            throw new FileStorageException(HttpStatus.BAD_REQUEST, "Invalid key format");
        }

        try {
            String[] parts = key.split("/", 2);
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(parts[0])
                            .object(parts[1])
                            .build()
            );
        } catch (ErrorResponseException e) {
            throw new FileStorageException(HttpStatus.NOT_FOUND, "File not found: " + key);
        } catch (Exception e) {
            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to remove file: " + key);
        }
    }

}
