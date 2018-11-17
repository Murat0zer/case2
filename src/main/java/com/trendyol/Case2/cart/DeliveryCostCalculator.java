package com.trendyol.Case2.cart;

import com.trendyol.Case2.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryCostCalculator {

    private double costPertDelivery;

    private double costPerProduct;

    private double fixedCost;

    public double calculateFor(Cart cart) {

        double deliveryCost;

        double numberOfDeliveries = calculateNumberOfDeliveries(cart);

        double numberOfProducts = cart.getCartItems().size();

        deliveryCost = (costPertDelivery * numberOfDeliveries) + (costPerProduct * numberOfProducts);

        cart.setDeliveryCost(deliveryCost);
        return deliveryCost;
    }

    private int calculateNumberOfDeliveries(Cart cart) {

        Set<String> categories = new HashSet<>();
        cart.getCartItems().forEach(cartItem -> {

            String categoryTitle = cartItem.getProduct().getCategories()
                    .stream()
                    .findFirst()
                    .orElse(Category
                            .builder()
                            .build())
                    .getTitle();
            categories.add(categoryTitle);
        });

        return categories.size();
    }
}
