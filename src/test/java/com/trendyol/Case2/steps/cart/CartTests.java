package com.trendyol.Case2.steps.cart;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.trendyol.Case2.SpringBootTestConfig;
import com.trendyol.Case2.cart.*;
import com.trendyol.Case2.category.Campaign;
import com.trendyol.Case2.category.Category;
import com.trendyol.Case2.category.CategoryRepository;
import com.trendyol.Case2.category.DiscountType;
import com.trendyol.Case2.product.Product;
import com.trendyol.Case2.product.ProductRepository;
import com.trendyol.Case2.product.ProductService;
import com.trendyol.Case2.response.ResponseMessage;
import com.trendyol.Case2.user.User;
import com.trendyol.Case2.user.UserRepository;
import com.trendyol.Case2.user.UserService;
import com.trendyol.Case2.util.*;
import cucumber.api.PendingException;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import gherkin.lexer.Ca;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Ignore;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    Predicate<CartItem> cartItemPredicate;
    Set<Product> products;
    Set<Category> categories;
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

    // TODO: 17.11.2018 parent kategoride indirim var ise bunu childlara da uygula

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
        Set<Category> categories = CategoryGenerator.getAllCategories();

        Set<Product> products = ProductGenerator.generateRandomProducts(20, categories);

        products.forEach(product1 -> product1.getCategories().clear());

        int i = 0;
        for (Product product1 : products) {
            product1.getCategories().add(Category.builder().title("cat " + i % 5).build());
            i++;
        }


        Set<CartItem> cartItems = CartItemGenerator.generateRandomCarts(20, products);

        Set<Cart> carts = CartGenerator.generateRandomCarts(1, cartItems, user.getId());
        cartRepository.saveAll(carts);
        cart = carts.iterator().next();
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
        int numberOfDeliveries = 5;
        int numberOfProducts = cart.getCartItems().size();
        DeliveryCostCalculator deliveryCostCalculator = DeliveryCostCalculator.builder()
                .costPertDelivery(costPertDelivery)
                .costPerProduct(costPerProduct)
                .fixedCost(fixedCost)
                .build();
        List<Category> categoriees = cart.getCartItems()
                .stream()
                .flatMap(cartItem1 -> cartItem1.getProduct().getCategories()
                        .stream())
                .collect(Collectors.toList());
        double expectedDeliveryCost = (costPertDelivery * numberOfDeliveries) + (costPerProduct * numberOfProducts);
        deliveryCostCalculator.calculateFor(cart);

        assertEquals(expectedDeliveryCost, cart.getDeliveryCost(), 0.0001);
    }

    @Diyelimki("^Kullanicinin sepetindeki urunler icin kategori indirimleri hesaplaniyor$")
    public void kullanicininSepetindekiUrunlerIcinKategoriIndirimleriHesaplaniyor() throws Throwable {


        Category category1 = Category.builder().title("category 1").build();
        categories = new HashSet<>();
        categories.addAll(Sets.newHashSet(category1));
        categoryRepository.saveAll(Lists.newArrayList(category1));

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
    Set<Campaign> campaigns;
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
        double totalPriceBeforeDiscounts = 150.0 + 250.0 + 25.0;
        double expectedTotalPriceAfterDiscounts = 120.0 + 125.0 + 10.0;

        cartService.applyDiscounts(cart, campaigns);
        assertEquals(expectedTotalPriceAfterDiscounts, cart.getTotalAmountAfterDiscounts(), 0.001);
    }

    @Diyelimki("^Kullanicinin elinde bir indirim kuponu var$")
    public void kullanicininElindeBirIndirimKuponuVar() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Eğerki("^Kullanici bu kuponu kullanmak isterse$")
    public void kullaniciBuKuponuKullanmakIsterse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Ozaman("^Sistem ilk once kullanici sepeti icin gerekli diger indirimleri hesaplar$")
    public void sistemIlkOnceKullaniciSepetiIcinGerekliDigerIndirimleriHesaplar() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Eğerki("^Hesaplanan deger kuponu kullanmak icin gerekli olan asgari degere esit veya buyuk ise$")
    public void hesaplananDegerKuponuKullanmakIcinGerekliOlanAsgariDegereEsitVeyaBuyukIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Ozaman("^Sistem kullanicinin sepeti icin kupon degeri kadar indirimi uygular$")
    public void sistemKullanicininSepetiIcinKuponDegeriKadarIndirimiUygular() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
