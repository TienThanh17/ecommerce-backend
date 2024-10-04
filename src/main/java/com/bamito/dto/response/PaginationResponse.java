package com.bamito.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaginationResponse<T> {
    List<T> content;
    int page;
    int size;
    long totalElements;
    int totalPage;
}
