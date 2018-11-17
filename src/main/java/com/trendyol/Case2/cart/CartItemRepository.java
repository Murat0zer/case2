package com.trendyol.Case2.cart;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface CartItemRepository extends MongoRepository<CartItem, String> {

//    Set<CartItem> findAllByCart(Cart cart);
}
