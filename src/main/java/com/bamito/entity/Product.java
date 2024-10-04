package com.bamito.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Product extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(nullable = false)
    String productId;
    @Column(nullable = false)
    String productName;
    long price;
    int discount;
    int rating;
    String imageURL;
    String imageId;
//    @Lob
    @Column(columnDefinition = "LONGTEXT")
    String descriptionContent;
    @Column(name = "description_HTML", columnDefinition = "LONGTEXT")
    String descriptionHTML;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    Brand brand;

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    ProductCategory productCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    Set<ProductSize> productSizes;

    @OneToMany(mappedBy = "product")
    Set<Feedback> feedbacks;

    @OneToMany(mappedBy = "product")
    Set<CartDetail> cartDetails;

    @ManyToMany(mappedBy = "favoriteProducts")
    Set<User> users = new HashSet<>();
}
