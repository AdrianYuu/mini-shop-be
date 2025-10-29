package com.adrian.minishop.service;

import com.adrian.minishop.dto.request.FileRequest;
import com.adrian.minishop.dto.response.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

    private final ValidationService validationService;

    private final MinioService minioService;

    public FileResponse get(FileRequest request) {
        validationService.validate(request);

        String presignedUrl = minioService.getPresignedUrl(request.getBucketName(), request.getObjectName(), 3600);

        return FileResponse.builder()
                .presignedUrl(presignedUrl)
                .build();
    }

}
