package com.trendyol.case2.cart;


import com.trendyol.case2.util.CategoryGenerator;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.*;

@Slf4j
@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "carts")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1}")
)
public class Cart implements Serializable, CartResponsibilities {

    @Id
    private String id;

    private String userId;

    private Set<CartItem> cartItems;

    private double deliveryCost;

    private double totalAmountAfterDiscounts;

    private double couponDiscount;

    private double campaignDiscount;

    @Override
    public void print() {
        String separator = "=================================================================================";
        Map<String, List<CartItem>> categoryProductMap = new HashMap<>();
        log.info("Information about Cart: " + this.getId());
        log.info(separator);

        CategoryGenerator.getAllCategories().
                forEach(category -> categoryProductMap.put(category.getTitle(), new ArrayList<>()));

        this.getCartItems()
                .forEach(cartItem -> {
                    String categoryTitle = cartItem.getProduct().getCategories().iterator().next().getTitle();
                    categoryProductMap.get(categoryTitle).add(cartItem);
                });

        categoryProductMap.forEach((categoryTitle, cartItems1) -> {
            if (!cartItems1.isEmpty()) {
                log.info(categoryTitle + " products");
                log.info(separator);


                for (CartItem cartItem : cartItems1) {
                    log.info("Product name: " + cartItem.getProduct().getName());
                    log.info("Product quantity: " + cartItem.getQuantity());
                    log.info("Product price: " + cartItem.getProduct().getPrice());
                    log.info("Product category: " + categoryTitle);
                    log.info("-----------------------------------------------------------------------------------------");
                }
                log.info(separator);
            }
        });
        log.info("Price info about cart");
        log.info(separator);
        log.info("Total price: " + CartUtil.calculateTotalPrice(this));
        log.info("Total discount: " + (this.getCampaignDiscount() + this.getCouponDiscount()));
        log.info("Total amount: " + this.getTotalAmountAfterDiscounts());
        log.info("Delivery cost: " + this.getDeliveryCost());
        log.info(separator);

    }
}
