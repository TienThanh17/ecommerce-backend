package com.bamito.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(nullable = false)
    String username;
    @Column(nullable = false)
    String password;
    @Column(unique = true, nullable = false)
    String email;
    String phoneNumber;
    LocalDate dateOfBirth;
    String avatarUrl;
    String avatarId;
    String otpCode;
    LocalDateTime otpExpiry;
    @Column(nullable = false)
    Boolean active;

    @ManyToOne
    @JoinColumn(name = "role_name", nullable = false)
    Role role;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "favorite",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    Set<Product> favoriteProducts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<Address> addresses;

    @OneToMany(mappedBy = "user")
    Set<Feedback> feedbacks;

    @OneToMany(mappedBy = "user")
    Set<Cart> carts;

    @OneToMany(mappedBy = "user")
    Set<Order> orders;
}