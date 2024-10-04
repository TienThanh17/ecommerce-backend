package com.bamito.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class Auditable {
    @CreatedDate
    @Column(updatable = false, nullable = false)
    LocalDateTime createDate;

    @LastModifiedDate
    @Column(insertable = false)
    LocalDateTime lastModified;

    @CreatedBy
    @Column(updatable = false, nullable = false)
    String createBy;

    @LastModifiedBy
    @Column(insertable = false)
    String lastModifiedBy;
}
