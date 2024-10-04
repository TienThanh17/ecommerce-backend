package com.bamito.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherRequest {
    @NotBlank(message = "id must not be blank")
    String id;
    @NotNull(message = "discount must not be blank")
    int discount;
    @NotNull(message = "quantity must not be blank")
    int quantity;
    @NotBlank(message = "startTime must not be blank")
    String startTime;
    @NotBlank(message = "endTime must not be blank")
    String endTime;
    @NotNull(message = "image must not be null")
    MultipartFile image;
}
