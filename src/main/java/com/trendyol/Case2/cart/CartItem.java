package com.trendyol.Case2.cart;

import com.trendyol.Case2.product.Product;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
 class CartItem implements Serializable {

    private Long productId;

    private int count;

    private Product product;

}
