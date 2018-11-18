package com.trendyol.case2.util;

import com.devskiller.jfairy.Fairy;
import com.google.common.collect.Lists;
import com.trendyol.case2.cart.CartItem;
import com.trendyol.case2.product.Product;

import java.util.HashSet;
import java.util.Set;

public class CartItemGenerator {

    private CartItemGenerator() {}

    public static Set<CartItem> generateRandomCartItems(int count, Set<Product> products) {

        Set<CartItem> cartItems = new HashSet<>();

        Fairy fairy = Fairy.create();

        for (int i = 0; i < count; i++) {
            CartItem cartItem = CartItem.builder()
                    .quantity(fairy.baseProducer().randomBetween(1, 20))
                    .product(fairy.baseProducer().randomElement(Lists.newArrayList(products)))
                    .build();
            cartItems.add(cartItem);
        }

        return cartItems;
    }
}
