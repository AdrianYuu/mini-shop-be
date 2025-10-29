package com.adrian.minishop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(
            min = 3,
            max = 63,
            message = "Bucket name must be between 3 and 63 characters"
    )
    private String bucketName;

    @NotBlank(message = "Object name is required")
    @Size(
            min = 1,
            max = 100,
            message = "Object name must be between 1 and 100 characters"
    )
    private String objectName;

}
