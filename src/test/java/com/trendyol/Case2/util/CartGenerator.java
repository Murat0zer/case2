package com.trendyol.Case2.util;

import com.devskiller.jfairy.Fairy;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.trendyol.Case2.cart.Cart;
import com.trendyol.Case2.cart.CartItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartGenerator {

    private CartGenerator() {
    }

    public static Set<Cart> generateRandomCarts(int count, Set<CartItem> cartItems, String userId) {

        Set<Cart> carts = new HashSet<>();

        Fairy fairy = Fairy.create();

        for (int i = 0; i < count; i++) {
            List<CartItem> cartItemLists = fairy.baseProducer().randomElements(Lists.newArrayList(cartItems),
                            fairy.baseProducer().randomBetween(1, cartItems.size()));
            Cart cart = Cart.builder()
                    .cartItems(Sets.newHashSet(cartItemLists))
                    .userId(userId)
                    .build();
            carts.add(cart);
        }
        return carts;
    }
}
