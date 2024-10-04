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
public class Size extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(nullable = false)
    String sizeId;
    @Column(nullable = false)
    String sizeName;

    @ManyToOne()
    @JoinColumn(name = "product_category_id")
    ProductCategory productCategory;

    @OneToMany(mappedBy = "size", cascade = CascadeType.ALL)
    Set<ProductSize> productSizes;

    @OneToMany(mappedBy = "size")
    Set<CartDetail> cartDetails;
}
