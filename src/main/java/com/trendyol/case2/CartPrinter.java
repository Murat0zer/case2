package com.trendyol.case2;

import com.google.common.collect.Lists;
import com.trendyol.case2.cart.*;
import com.trendyol.case2.category.Campaign;
import com.trendyol.case2.category.Category;
import com.trendyol.case2.category.CategoryRepository;
import com.trendyol.case2.category.DiscountType;
import com.trendyol.case2.product.Product;
import com.trendyol.case2.product.ProductRepository;
import com.trendyol.case2.user.User;
import com.trendyol.case2.user.UserRepository;
import com.trendyol.case2.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Profile("dev")
@Component
public class CartPrinter implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    private Set<Product> products;
    private Set<Category> categories;
    private Set<Campaign> campaigns;
    private User user = User.builder().build();

    @Override
    public void run(String... args) throws Exception {

        user = UserGenerator.generateRandomUsers(1).iterator().next();
        userRepository.save(user);

        categories = CategoryGenerator.getAllCategories();
        categoryRepository.saveAll(categories);

        products = ProductGenerator.generateRandomProducts(100, categories);
        productRepository.saveAll(products);

        Set<CartItem> cartItems = CartItemGenerator.generateRandomCartItems(25, products);

        Set<Cart> carts = CartGenerator.generateRandomCarts(10, cartItems, user.getId());
        cartRepository.saveAll(carts);

        categories.forEach(category -> {
            Campaign campaignAmount = Campaign.builder()
                    .categoryId(category.getTitle())
                    .discountType(DiscountType.RATE)
                    .discountValue(35.0)
                    .necessaryQuantity(5)
                    .build();
            Campaign campaignRate1 = Campaign.builder()
                    .categoryId(category.getTitle())
                    .discountType(DiscountType.RATE)
                    .discountValue(20.0)
                    .necessaryQuantity(3)
                    .build();
            Campaign campaignRate2 = Campaign.builder()
                    .categoryId(category.getTitle())
                    .discountType(DiscountType.AMOUNT)
                    .discountValue(125.0)
                    .necessaryQuantity(5)
                    .build();
            campaigns = new HashSet<>(Lists.newArrayList(campaignAmount, campaignRate1, campaignRate2));
            category.setCampaigns(campaigns);
        });

        Coupon coupon = Coupon.builder()
                .discountType(DiscountType.RATE)
                .discountValue(10.0)
                .necessaryValue(100.0)
                .build();

        List<Map<String, List<CartItem>>> cartCategoryProductMapList = new ArrayList<>();

        carts.forEach(cart -> cartCategoryProductMapList.add(new HashMap<>()));

        cartCategoryProductMapList
                .forEach(categoryProductMap -> categories.
                        forEach(category -> categoryProductMap.put(category.getTitle(), new ArrayList<>())));

        DeliveryCostCalculator deliveryCostCalculator = DeliveryCostCalculator.builder()
                .fixedCost(2.99)
                .costPertDelivery(5.75)
                .costPerProduct(2.75)
                .build();
        carts.forEach(cart -> {
            cartService.applyDiscounts(cart, campaigns);
            cartService.applyCoupon(cart, coupon);
            deliveryCostCalculator.calculateFor(cart);

            cart.getCartItems()
                    .forEach(cartItem -> {
                        String categoryTitle = cartItem.getProduct().getCategories().iterator().next().getTitle();
                        cartCategoryProductMapList.iterator().next().get(categoryTitle).add(cartItem);
                    });
        });

        carts.forEach(Cart::print);
    }
}
