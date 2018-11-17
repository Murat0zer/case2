package com.trendyol.Case2.steps.cart;

import com.google.common.collect.Sets;
import com.trendyol.Case2.SpringBootTestConfig;
import com.trendyol.Case2.cart.*;
import com.trendyol.Case2.category.Category;
import com.trendyol.Case2.category.CategoryRepository;
import com.trendyol.Case2.category.CategoryService;
import com.trendyol.Case2.product.Product;
import com.trendyol.Case2.product.ProductRepository;
import com.trendyol.Case2.product.ProductService;
import com.trendyol.Case2.response.ResponseMessage;
import com.trendyol.Case2.user.User;
import com.trendyol.Case2.user.UserRepository;
import com.trendyol.Case2.user.UserService;
import com.trendyol.Case2.util.CategoryGenerator;
import com.trendyol.Case2.util.ProductGenerator;
import com.trendyol.Case2.util.UserGenerator;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Ignore;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.*;

@Ignore
@RequiredArgsConstructor
public class CartTests extends SpringBootTestConfig { // NOSONAR

    private final CategoryService categoryService;
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private final Environment environment;

    private final UserService userService;

    private final UserRepository userRepository;

    private final CartService cartService;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private Cart cart = Cart.builder().build();

    private User user = User.builder().build();

    private Product product = Product.builder().build();

    private CartItem cartItem = CartItem.builder().build();

    @Transactional
    public void init() {
        cartRepository.deleteAll();
        cartItemRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        user = UserGenerator.generateRandomUsers(1).stream().findFirst().orElse(User.builder().build());
        cart = Cart.builder()
                .userId(user.getId()).build();

        Set<Category> categorySet = CategoryGenerator.generateRandomCategories(1);
        categoryRepository.saveAll(categorySet);

        product = ProductGenerator
                .generateRandomProducts(1, categorySet)
                .stream()
                .findFirst()
                .orElse(Product.builder().build());

        productService.addNewProduct(product);
    }

    @Diyelimki("^Kullanici bos olan sepetine sectigi bir urunden (\\d+) adet eklemek istiyor$")
    public void kullaniciBosOlanSepetineSectigiBirUrundenAdetEklemekIstiyor(int productQuantity) throws Throwable {
        this.init();
        cartItem = CartItem.builder()
                .quantity(productQuantity)
                .product(product)
                .build();
    }

    @Ozaman("^Sistem kullanici icin bir sepet olusturur ve sepetine ilgili urunden (\\d+) adet ekler$")
    public void sistemKullaniciIcinBirSepetOlustururVeSepetineIlgiliUrundenAdetEkler(int productQuantity) throws Throwable {

        ResponseMessage responseMessage;
        cart = Cart.builder()
                .cartItems(new HashSet<>())
                .userId(user.getId())
                .build();

        cartItemRepository.save(cartItem);
        responseMessage = cartService.addItem(cart, Sets.newHashSet(cartItem));
        String expectedMessage = environment.getRequiredProperty("cart.products.added");
        assertEquals(expectedMessage, responseMessage.getMessage());
    }

    @Diyelimki("^Kullanici icerisinde urunler olan sepetine sectigi bir urunden (\\d+) adet eklemek istiyor$")
    public void kullaniciIcerisindeUrunlerOlanSepetineSectigiBirUrundenAdetEklemekIstiyor(int productQuantity) throws Throwable {

        Category category = CategoryGenerator.generateRandomCategories(1)
                .stream()
                .findFirst()
                .orElse(Category.builder().build());
        categoryRepository.save(category);

        Product product2 = ProductGenerator.generateRandomProducts(1, Sets.newHashSet(category))
                .stream()
                .findFirst()
                .orElse(Product.builder().build());
        productRepository.save(product2);

        cartItem = CartItem.builder()
                .quantity(productQuantity)
                .product(product2)
                .build();
        cartItemRepository.save(cartItem);

        cart = Cart.builder()
                .cartItems(Sets.newHashSet(cartItem))
                .userId(user.getId())
                .build();

        assertThat(cart.getCartItems(), Matchers.not(IsEmptyCollection.emptyCollectionOf(CartItem.class)));
    }

    @Eğerki("^Ilgili urun daha onceden sepete eklenmediyse$")
    public void ilgiliUrunDahaOncedenSepeteEklenmediyse() throws Throwable {

        Predicate<CartItem> cartItemPredicate = cartItem1 -> cartItem1.getProduct().getId().equals(product.getId());
        assertTrue(cart.getCartItems().stream().noneMatch(cartItemPredicate));
    }

    @Ozaman("^Sistem kullanicinin sepetine ilgili urunden (\\d+) adet ekler$")
    public void sistemKullanicininSepetineIlgiliUrundenAdetEkler(int productQuantity) throws Throwable {
        ResponseMessage responseMessage;
        categoryRepository.deleteAll();
        Category category = CategoryGenerator.generateRandomCategories(1)
                .stream()
                .findFirst()
                .orElse(Category.builder().build());
        categoryRepository.save(category);

        Product product2 = ProductGenerator.generateRandomProducts(1, Sets.newHashSet(category))
                .stream()
                .findFirst()
                .orElse(Product.builder().build());
        productRepository.save(product2);

        CartItem cartItemNew = CartItem.builder()
                .product(product)
                .quantity(productQuantity)
                .build();

        cartService.addItem(cart, Sets.newHashSet(cartItemNew));
        responseMessage = cartService.addItem(cart, Sets.newHashSet(cartItem));

        String expectedMessage = environment.getRequiredProperty("cart.products.added");
        assertEquals(expectedMessage, responseMessage.getMessage());
        Cart persistedCart = cartRepository.findById(cart.getId()).orElse(Cart.builder().build());
        assertEquals(persistedCart, cart);
    }

    @Diyelimki("^Kullanici sepetinde zaten olan bir urunden sepetine (\\d+) adet eklemek istiyor$")
    public void kullaniciSepetindeZatenOlanBirUrundenSepetineAdetEklemekIstiyor(int productQuantity) throws Throwable {
        cart = cartRepository.findAll().iterator().next();
        cartItem = CartItem.builder()
                .quantity(productQuantity)
                .product(product)
                .build();

        assertThat(cart.getCartItems(), Matchers.not(IsEmptyCollection.emptyCollectionOf(CartItem.class)));
    }

    @Ozaman("^Sistem kullanicinin sepetindeki ilgili urununun sayisini (\\d+) artirir$")
    public void sistemKullanicininSepetindekiIlgiliUrunununSayisiniArtirir(int productQuantity) throws Throwable {

    }
}
