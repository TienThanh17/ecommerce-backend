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
public class ProductCategory extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(nullable = false)
    String categoryId;
    @Column(nullable = false)
    String categoryName;

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL)
    Set<Product> products;

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL)
    Set<Size> sizes;
}