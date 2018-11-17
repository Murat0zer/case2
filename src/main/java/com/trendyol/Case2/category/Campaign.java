package com.trendyol.Case2.category;


import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Campaign implements Serializable {

    @Id
    private String id;

    private String categoryId;

    private double discountValue;

    private int necessaryQuantity;

    private DiscountType discountType;
}
