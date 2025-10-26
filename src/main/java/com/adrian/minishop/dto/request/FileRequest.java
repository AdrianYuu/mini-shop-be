package com.adrian.minishop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class FileRequest {

    @NotBlank(message = "Bucket name is required")
    private String bucketName;

    @NotBlank(message = "Object name is required")
    private String objectName;

}
