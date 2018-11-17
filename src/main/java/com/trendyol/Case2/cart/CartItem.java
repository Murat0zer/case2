package com.trendyol.Case2.cart;

import com.trendyol.Case2.product.Product;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {

    @Id
    private String id;

    private int quantity;

    private Product product;

    private String cartId;


}
