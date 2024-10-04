package com.bamito.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // chỉ trả ra những trường không null
public class ResponseObject<T> {
    @Builder.Default
    private int code = 0;
    @Builder.Default
    private String message = "success";
    private T data;
}
