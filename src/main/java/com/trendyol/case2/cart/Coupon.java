package com.trendyol.case2.cart;

import com.trendyol.case2.category.DiscountType;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Coupon implements Serializable {

    @Id
    private String id;

    private double discountValue;

    private double necessaryValue;

    private DiscountType discountType;
}

