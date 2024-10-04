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
public class Cart extends Auditable {
    @Id
    String id;

    long totalPrice;

    @ManyToOne
    User user;

    @OneToMany(mappedBy = "cart")
    Set<CartDetail> cartDetails;
}