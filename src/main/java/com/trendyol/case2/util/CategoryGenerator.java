package com.trendyol.case2.util;

import com.devskiller.jfairy.Fairy;
import com.google.common.collect.Lists;
import com.trendyol.case2.category.Category;

import java.util.*;

public class CategoryGenerator {

    private CategoryGenerator() {
    }
    // key is category, value is parent category
    private static Map<String, List<String>> categories;

    private static void generateCategoryNames() {
        categories = new HashMap<>();
        categories.put("food", new ArrayList<>());
        categories.put("tea & coffee", Collections.singletonList("food"));
        categories.put("electronic", new ArrayList<>());
        categories.put("book", new ArrayList<>());
        categories.put("novel", Collections.singletonList("book"));
        categories.put("computers", Collections.singletonList("electronic"));
        categories.put("entertainment", new ArrayList<>());
        categories.put("video games", Arrays.asList("entertainment", "computers"));
    }

    public static Set<Category> generateRandomCategories(int count) {

        generateCategoryNames();
        Fairy fairy = Fairy.create();
        Set<Category> categories = new HashSet<>();
        for (int i = 0; i < count; i++) {
            String title = fairy.baseProducer().randomElement(Lists.newArrayList(CategoryGenerator.categories.keySet()));
            Category category = Category.builder()
                    .title(title)
                    .parentCategories(new HashSet<>(CategoryGenerator.categories.get(title)))
                    .build();

            categories.add(category);
        }
        return categories;
    }

    public static Set<Category> getAllCategories() {
        generateCategoryNames();
        Set<Category> categorySet = new HashSet<>();

        categories.forEach((categoryTitle, parentCategoryList) -> {
            Category category = Category.builder()
                    .title(categoryTitle)
                    .parentCategories(new HashSet<>(CategoryGenerator.categories.get(categoryTitle)))
                    .build();

            categorySet.add(category);

        } );
        return  categorySet;
    }


}
