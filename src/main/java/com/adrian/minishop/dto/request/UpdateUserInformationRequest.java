package com.adrian.minishop.dto.request;

import com.adrian.minishop.validation.annotation.ValidFile;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserInformationRequest {

    @Size(
            min = 3,
            max = 100,
            message = "Name must be between 3 and 100 characters"
    )
    private String name;

    @Size(
            max = 255,
            message = "Bio must not exceed 255 characters"
    )
    private String bio;

    @ValidFile(
            maxSize = 2_000_000,
            contentTypes = {"image/jpeg", "image/png"},
            message = "Image must be JPEG or PNG under 2MB",
            required = false
    )
    private MultipartFile image;

}
