package com.adrian.minishop.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.url}")
    private String url;

    @Value("${minio.buckets.user}")
    private String userBucket;

    @Value("${minip.buckets.product}")
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

    public String uploadFile(MultipartFile file, String bucketName) throws Exception {
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
    }

    public String getFileUrl(String key) {
        return url + "key";
    }

}
