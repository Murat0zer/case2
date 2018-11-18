package com.trendyol.case2.steps.cart;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.trendyol.case2.SpringBootTestConfig;
import com.trendyol.case2.cart.*;
import com.trendyol.case2.category.Campaign;
import com.trendyol.case2.category.Category;
import com.trendyol.case2.category.CategoryRepository;
import com.trendyol.case2.category.DiscountType;
import com.trendyol.case2.product.Product;
import com.trendyol.case2.product.ProductRepository;
import com.trendyol.case2.product.ProductService;
import com.trendyol.case2.response.ResponseMessage;
import com.trendyol.case2.user.User;
import com.trendyol.case2.user.UserRepository;
import com.trendyol.case2.user.UserService;
import com.trendyol.case2.util.CartItemGenerator;
import com.trendyol.case2.util.CategoryGenerator;
import com.trendyol.case2.util.ProductGenerator;
import com.trendyol.case2.util.UserGenerator;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Ignore;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.*;

@Ignore
@RequiredArgsConstructor
public class CartTests extends SpringBootTestConfig { // NOSONAR

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private final Environment environment;

    private final UserService userService;

    private final UserRepository userRepository;

    private final CartService cartService;

    private final CartRepository cartRepository;
    private Predicate<CartItem> cartItemPredicate;
    private Set<Product> products;
    private Set<Category> categories;
    private Cart cart = Cart.builder().build();
    private User user = User.builder().build();
    private Product product = Product.builder().build();
    private CartItem cartItem = CartItem.builder().build();

    @Transactional
    public void init() {
        cartRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        user = UserGenerator.generateRandomUsers(1).stream().findFirst().orElse(User.builder().build());
        userService.addNewUser(user);

        cart = Cart.builder()
                .cartItems(new HashSet<>())
                .userId(user.getId()).build();

        Set<Category> categorySet = CategoryGenerator.generateRandomCategories(1);
        categoryRepository.saveAll(categorySet);

        product = ProductGenerator
                .generateRandomProducts(1, categorySet)
                .stream()
                .findFirst()
                .orElse(Product.builder().build());

        productService.addNewProduct(product);
        cartService.saveCart(cart);
    }

    @Diyelimki("^Kullanici bos olan sepetine sectigi bir urunden (\\d+) adet eklemek istiyor$")
    public void kullaniciBosOlanSepetineSectigiBirUrundenAdetEklemekIstiyor(int productQuantity) throws Throwable {
        this.init();
        cartItem = CartItem.builder()
                .cartId(cart.getId())
                .quantity(productQuantity)
                .product(product)
                .build();
    }

    @Ozaman("^Sistem kullanici icin bir sepet olusturur ve sepetine ilgili urunden (\\d+) adet ekler$")
    public void sistemKullaniciIcinBirSepetOlustururVeSepetineIlgiliUrundenAdetEkler(int productQuantity) throws Throwable {

        ResponseMessage responseMessage;
        this.init();
        cartItem.setCartId(cart.getId());
        cart.getCartItems().add(cartItem);

        responseMessage = cartService.saveCart(cart);
        String expectedMessage = environment.getRequiredProperty("cart.saved.success");
        assertEquals(expectedMessage, responseMessage.getMessage());
        assertEquals(cart, cartRepository.findById(cartItem.getCartId()).orElseGet(Cart::new));
    }

    @Diyelimki("^Kullanici icerisinde urunler olan sepetine sectigi bir urunden (\\d+) adet eklemek istiyor$")
    public void kullaniciIcerisindeUrunlerOlanSepetineSectigiBirUrundenAdetEklemekIstiyor(int productQuantity) throws Throwable {
        this.init();
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

        responseMessage = cartService.saveCart(cart);

        String expectedMessage = environment.getRequiredProperty("cart.saved.success");
        assertEquals(expectedMessage, responseMessage.getMessage());
        Cart persistedCart = cartRepository.findById(cart.getId()).orElse(Cart.builder().build());
        assertEquals(persistedCart, cart);
    }

    @Diyelimki("^Kullanici sepetinde zaten olan bir urunden sepetine (\\d+) adet eklemek istiyor$")
    public void kullaniciSepetindeZatenOlanBirUrundenSepetineAdetEklemekIstiyor(int productQuantity) throws Throwable {
        this.init();

        cart = cartRepository.findAll().iterator().next();
        cartItem = CartItem.builder()
                .quantity(productQuantity)
                .product(product)
                .cartId(cart.getId())
                .build();
        cart.setCartItems(Sets.newHashSet(cartItem));

        cartRepository.save(cart);

        CartItem cartItemNew = CartItem.builder()
                .quantity(productQuantity)
                .product(product)
                .cartId(cart.getId())
                .build();
        cart.setCartItems(Sets.newHashSet(cartItem));
        assertThat(cart.getCartItems(), Matchers.not(IsEmptyCollection.emptyCollectionOf(CartItem.class)));

        cartItemPredicate = cartItem1 -> cartItem1.getProduct().getId().equals(cartItemNew.getProduct().getId());
        assertTrue(cart.getCartItems().stream().anyMatch(cartItemPredicate));
    }

    // TODO: 17.11.2018 parent kategoride indirim var ise bunu childlara da uygula ??

    @Ozaman("^Sistem kullanicinin sepetindeki ilgili urununun sayisini (\\d+) artirir$")
    public void sistemKullanicininSepetindekiIlgiliUrunununSayisiniArtirir(int productQuantity) throws Throwable {

        int oldQuantity = Objects.requireNonNull(cart.getCartItems()
                .stream()
                .filter(cartItemPredicate)
                .findFirst().orElse(null)).getQuantity();

        cart.getCartItems()
                .stream()
                .filter(cartItemPredicate)
                .forEach(cartItem1 -> cartItem1.setQuantity(cartItem1.getQuantity() + productQuantity));
        cartService.saveCart(cart);

        int newQuantity = cartRepository.findById(cartItem.getCartId())
                .orElse(Cart.builder().build())
                .getCartItems()
                .stream()
                .filter(cartItemPredicate)
                .findFirst()
                .orElse(CartItem.builder().build())
                .getQuantity();

        assertEquals(oldQuantity + productQuantity, newQuantity);
    }

    @Diyelimki("^Kullanicinin sepetindeki urunler icin teslimat ucreti hesaplaniyor$")
    public void kullanicininSepetindekiUrunlerIcinTeslimatUcretiHesaplaniyor() throws Throwable {
        this.init();
        Category category1 = Category.builder().title("category 1").build();
        Category category2 = Category.builder().title("category 2").build();
        Category category3 = Category.builder().title("category 3").build();
        categories = new HashSet<>();
        categories.addAll(Sets.newHashSet(category1, category2, category3));
        categoryRepository.saveAll(Lists.newArrayList(categories));

        products = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            Product product = Product.builder()
                    .price(BigDecimal.valueOf(50.0))
                    .name("product 1")
                    .categories(Sets.newHashSet(Lists.newArrayList(categories).get(i % categories.size())))
                    .build();
            products.add(product);

        }
        productRepository.saveAll(products);
        products.addAll(Lists.newArrayList(products));

        cart = Cart.builder()
                .userId(user.getId())
                .cartItems(new HashSet<>())
                .deliveryCost(0)
                .totalAmountAfterDiscounts(0)
                .campaignDiscount(0)
                .couponDiscount(0)
                .build();
        cartRepository.save(cart);

        Set<CartItem> cartItems = new HashSet<>(Sets.newHashSet());

        for (int i = 0; i < 5; i++) {
            CartItem cartItem1 = CartItem.builder()
                    .product(Lists.newArrayList(products).get( i % products.size()))
                    .quantity(3)
                    .cartId(cart.getId())
                    .build();

            cartItems.add(cartItem1);
        }

        cart.setCartItems(cartItems);
        cartRepository.save(cart);

    }

    @Eğerki("^Kullanicinin sepetinde en az (\\d+) urun var ise$")
    public void kullanicininSepetindeEnAzUrunVarIse(int arg0) throws Throwable {
        assertThat(cart.getCartItems().size(), Matchers.greaterThanOrEqualTo(arg0));
    }

    @Ozaman("^Sistem kullanicinin sepeti icin teslimat ucretini hesaplar$")
    public void sistemKullanicininSepetiIcinTeslimatUcretiniHesaplar() throws Throwable {

        double costPertDelivery = 5.75;
        double costPerProduct = 2.75;
        double fixedCost = 2.99;
        int numberOfDeliveries = 3;
        int numberOfProducts = cart.getCartItems().size();
        DeliveryCostCalculator deliveryCostCalculator = DeliveryCostCalculator.builder()
                .costPertDelivery(costPertDelivery)
                .costPerProduct(costPerProduct)
                .fixedCost(fixedCost)
                .build();

        double expectedDeliveryCost = (costPertDelivery * numberOfDeliveries)
                + (costPerProduct * numberOfProducts) + fixedCost;
        deliveryCostCalculator.calculateFor(cart);

        assertEquals(expectedDeliveryCost, cart.getDeliveryCost(), 0.0001);
    }

    @Diyelimki("^Kullanicinin sepetindeki urunler icin kategori indirimleri hesaplaniyor$")
    public void kullanicininSepetindekiUrunlerIcinKategoriIndirimleriHesaplaniyor() throws Throwable {


        Category category1 = Category.builder().title("category 1").build();
        categories = new HashSet<>();
        categories.add(category1);
        categoryRepository.save(category1);

        products = new HashSet<>();
            Product product1 = Product.builder()
                .price(BigDecimal.valueOf(50.0))
                .name("product 1")
                .categories(Sets.newHashSet(category1))
                .build();

        Product product2 = Product.builder()
                .price(BigDecimal.valueOf(50.0))
                .name("product 2")
                .categories(Sets.newHashSet(category1))
                .build();

        Product product3 = Product.builder()
                .price(BigDecimal.valueOf(5.0))
                .name("product 3")
                .categories(Sets.newHashSet(category1))
                .build();
        productRepository.saveAll(products);
        products.addAll(Lists.newArrayList(product1, product2, product3));

        cart = Cart.builder()
                .userId(user.getId())
                .cartItems(new HashSet<>())
                .deliveryCost(0)
                .totalAmountAfterDiscounts(0)
                .campaignDiscount(0)
                .couponDiscount(0)
                .build();
        cartRepository.save(cart);

        CartItem cartItem1 = CartItem.builder()
                .product(product1)
                .quantity(3)
                .cartId(cart.getId())
                .build();
        CartItem cartItem2 = CartItem.builder()
                .product(product2)
                .quantity(5)
                .cartId(cart.getId())
                .build();

        CartItem cartItem3 = CartItem.builder()
                .product(product3)
                .quantity(5)
                .cartId(cart.getId())
                .build();

        Set<CartItem> cartItems = new HashSet<>(Sets.newHashSet(cartItem1, cartItem2, cartItem3));
        cart.setCartItems(cartItems);
        cartRepository.save(cart);
    }
    private Set<Campaign> campaigns;
    @Eğerki("^Kategori icin birden fazla kampanya tanimli ise$")
    public void kategoriIcinBirdenFazlaKampanyaTanimliIse() throws Throwable {

        String categoryTitle = Lists.newArrayList(categories).get(0).getTitle();
        Campaign campaignAmount = Campaign.builder()
                .categoryId(categoryTitle)
                .discountType(DiscountType.RATE)
                .discountValue(50.0)
                .necessaryQuantity(5)
                .build();
        Campaign campaignRate1 = Campaign.builder()
                .categoryId(categoryTitle)
                .discountType(DiscountType.RATE)
                .discountValue(20.0)
                .necessaryQuantity(3)
                .build();
        Campaign campaignRate2 = Campaign.builder()
                .categoryId(categoryTitle)
                .discountType(DiscountType.AMOUNT)
                .discountValue(15.0)
                .necessaryQuantity(5)
                .build();
        campaigns = new HashSet<>(Lists.newArrayList(campaignAmount, campaignRate1, campaignRate2));
        categories.forEach(category -> category.setCampaigns(campaigns));
        categoryRepository.saveAll(categories);

    }

    @Ozaman("^Sistem kullanicinin sepeti icin mumkun olan en iyi kategori indirimini hesaplar$")
    public void sistemKullanicininSepetiIcinMumkunOlanEnIyiKategoriIndiriminiHesaplar() throws Throwable {
        double expectedTotalPriceAfterDiscounts = 120.0 + 125.0 + 10.0;

        cartService.applyDiscounts(cart, campaigns);
        assertEquals(expectedTotalPriceAfterDiscounts, cart.getTotalAmountAfterDiscounts(), 0.001);
    }


    private Coupon coupon;
    @Diyelimki("^Kullanici indirim kuponu kullanmak istiyor$")
    public void kullaniciIndirimKuponuKullanmakIstiyor() throws Throwable {

        coupon = Coupon.builder()
                .discountType(DiscountType.RATE)
                .discountValue(10.0)
                .necessaryValue(100.0)
                .build();

        categories = CategoryGenerator.getAllCategories();
        products = ProductGenerator.generateRandomProducts(100, categories);
        Set<CartItem> cartItems = CartItemGenerator.generateRandomCartItems(20, products);
        cart = Cart.builder()
                .couponDiscount(0)
                .campaignDiscount(0)
                .totalAmountAfterDiscounts(0)
                .deliveryCost(0)
                .cartItems(cartItems)
                .userId(user.getId())
                .build();
    }


    @Ozaman("^Sistem ilk once kullanicinin sepeti icin gerekli diger indirimleri hesaplar$")
    public void sistemIlkOnceKullanicininSepetiIcinGerekliDigerIndirimleriHesaplar() throws Throwable {
        double expectedTotalPriceAfterDiscounts = CartUtil.calculateTotalPrice(cart);
        campaigns = new HashSet<>();
        cartService.applyDiscounts(cart, campaigns);
        assertEquals(expectedTotalPriceAfterDiscounts, cart.getTotalAmountAfterDiscounts(), 0.001);
    }

    @Eğerki("^Hesaplanan deger kuponu kullanmak icin gerekli olan asgari degere esit veya buyuk ise$")
    public void hesaplananDegerKuponuKullanmakIcinGerekliOlanAsgariDegereEsitVeyaBuyukIse() throws Throwable {
        assertThat(cart.getTotalAmountAfterDiscounts(), Matchers.greaterThanOrEqualTo(coupon.getNecessaryValue()));
    }

    @Ozaman("^Sistem kullanicinin sepeti icin kupon degeri kadar indirimi uygular$")
    public void sistemKullanicininSepetiIcinKuponDegeriKadarIndirimiUygular() throws Throwable {

        double currentTotalAmount = cart.getTotalAmountAfterDiscounts();

        double discount = coupon.getDiscountType() == DiscountType.AMOUNT ? coupon.getDiscountValue()
                : cart.getTotalAmountAfterDiscounts() * coupon.getDiscountValue() / 100.0;

        cartService.applyCoupon(cart, coupon);
        assertEquals(currentTotalAmount - discount, cart.getTotalAmountAfterDiscounts(), 0.001);
    }


}
