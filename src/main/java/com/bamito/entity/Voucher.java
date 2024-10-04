package com.bamito.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Voucher extends Auditable {
    @Id
    String id;
    int discount;
    int quantity;
    String startTime;
    String endTime;
    String imageURL;
    String imageId;

    @OneToMany(mappedBy = "voucher")
    Set<Order> orders;
}
