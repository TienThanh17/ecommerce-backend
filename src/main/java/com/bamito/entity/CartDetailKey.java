package com.bamito.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailKey implements Serializable {
    String cartId;
    int productId;
    int sizeId;
}
