package com.bamito.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class CartDetail extends Auditable {
    @EmbeddedId
    CartDetailKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    Cart cart;

    @ManyToOne
    @MapsId("sizeId")
    @JoinColumn(name = "size_id")
    Size size;

    int quantity;
    int totalPrice;
}
