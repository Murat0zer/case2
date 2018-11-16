package com.trendyol.Case2.util;

import com.devskiller.jfairy.Fairy;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.trendyol.Case2.category.Category;
import com.trendyol.Case2.product.Product;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductGenerator {

    private ProductGenerator() {
    }

    public static Set<Product> generateRandomProducts(int count, Set<Category> categories) {

        Fairy fairy = Fairy.create();
        Set<Product> products = new HashSet<>();
        for (int i = 0; i < count; i++) {

            List<Category> categoryList = fairy.baseProducer()
                    .randomElements(Lists.newArrayList(categories),fairy.baseProducer().randomBetween(1, categories.size()));

            Product product = Product.builder()
                    .categories(Sets.newHashSet(categoryList))
                    .description(fairy.textProducer().latinSentence(fairy.baseProducer().randomBetween(10, 30)))
                    .name(fairy.textProducer().latinWord(fairy.baseProducer().randomBetween(3, 12)))
                    .price(BigDecimal.valueOf(fairy.baseProducer().randomBetween(1, 10000)))
                    .build();

            products.add(product);
        }
        return products;


    }
}
