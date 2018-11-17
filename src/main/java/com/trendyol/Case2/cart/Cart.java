package com.trendyol.Case2.cart;


import com.trendyol.Case2.category.Campaign;
import com.trendyol.Case2.user.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Set;

@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "carts")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1}")
)
public class Cart implements Serializable {

    @Id
    private String id;

    private String userId;

    private Set<CartItem> cartItems;

    private double deliveryCost;

    private double totalAmountAfterDiscounts;

    private double couponDiscount;

    private double campaignDiscount;

}
