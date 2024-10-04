package com.bamito.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
@Embeddable
public class ProductSizeKey implements Serializable {
    int productId;
    int sizeId;

    public ProductSizeKey(int productId, int sizeId) {
        this.productId = productId;
        this.sizeId = sizeId;
    }

    public ProductSizeKey() {

    }
}
