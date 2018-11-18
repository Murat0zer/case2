package com.trendyol.case2.cart;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

public interface CartRepository extends MongoRepository<Cart, String> {

    Set<Cart> findAllByUserId(Long userId);
}
