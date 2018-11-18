package com.trendyol.case2.category;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {

    Optional<Category> findByTitleEquals(String categoryTitle);
}
