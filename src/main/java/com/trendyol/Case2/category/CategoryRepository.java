package com.trendyol.Case2.category;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByTitleEquals(String categoryTitle);
}
