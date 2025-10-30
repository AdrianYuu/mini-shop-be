package com.adrian.minishop.controller;

import com.adrian.minishop.dto.request.FileRequest;
import com.adrian.minishop.dto.response.FileResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping(
            path = "/{bucketName}/{objectName}"
    )
    public ResponseEntity<WebResponse<Void>> get(
            @PathVariable("bucketName") String bucketName,
            @PathVariable("objectName") String objectName
    ) {
        FileRequest request = FileRequest.builder()
                .bucketName(bucketName)
                .objectName(objectName)
                .build();

        FileResponse response = fileService.get(request);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", response.getPresignedUrl())
                .build();
    }

}
