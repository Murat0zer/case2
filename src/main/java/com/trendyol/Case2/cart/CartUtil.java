package com.trendyol.Case2.cart;

public class CartUtil {

    public static double calculateTotalPrice(Cart cart) {
        double totalPrice = 0;

        for (CartItem cartItem : cart.getCartItems()) {
           int price = cartItem.getProduct().getPrice().intValue();
           int quantity = cartItem.getQuantity();
           totalPrice += price *quantity;
        }
        return totalPrice;
    }

}
