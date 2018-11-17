package com.trendyol.Case2.util;

import com.devskiller.jfairy.Fairy;
import com.google.common.collect.Lists;
import com.trendyol.Case2.cart.CartItem;
import com.trendyol.Case2.product.Product;

import java.util.HashSet;
import java.util.Set;

public class CartItemGenerator {

    private CartItemGenerator() {}

    public static Set<CartItem> generateRandomCarts(int count, Set<Product> products) {

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
