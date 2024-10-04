package com.bamito.dto.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    String id;
    int totalPrice;
    int status;
    String paymentName;
    String username;
    String phoneNumber;
    String address;
    String voucherId;
    Integer voucherPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime createDate;
    Set<SetProductOrder> products;
}
