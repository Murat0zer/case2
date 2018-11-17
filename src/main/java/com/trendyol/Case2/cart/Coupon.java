package com.trendyol.Case2.cart;

import com.trendyol.Case2.category.DiscountType;
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

    private int discountValue;

    private int necessaryValue;

    private DiscountType discountType;
}

