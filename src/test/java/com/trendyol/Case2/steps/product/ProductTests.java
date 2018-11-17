package com.trendyol.Case2.steps.product;

import com.trendyol.Case2.GeneralException;
import com.trendyol.Case2.SpringBootTestConfig;
import com.trendyol.Case2.category.Category;
import com.trendyol.Case2.category.CategoryRepository;
import com.trendyol.Case2.category.CategoryService;
import com.trendyol.Case2.product.Product;
import com.trendyol.Case2.product.ProductRepository;
import com.trendyol.Case2.product.ProductService;
import com.trendyol.Case2.response.ResponseMessage;
import com.trendyol.Case2.user.User;
import com.trendyol.Case2.util.CategoryGenerator;
import com.trendyol.Case2.util.ProductGenerator;
import com.trendyol.Case2.util.UserGenerator;
import cucumber.api.PendingException;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import cucumber.api.java.tr.Ve;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.*;


@Ignore
@RequiredArgsConstructor
public class ProductTests extends SpringBootTestConfig { // NOSONAR

    private final CategoryService categoryService;
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private final Environment environment;
    private User user = User.builder().build();
    private Product product = Product.builder().build();

    private void init() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        user = UserGenerator.generateRandomUsers(1).stream().findFirst().orElse(null);
        assertNotNull(user);

        Set<Category> categories = CategoryGenerator.generateRandomCategories(1);
        categoryService.addCategories(categories);

        product = ProductGenerator.generateRandomProducts(1, categories).stream().findFirst().orElse(null);
        assertNotNull(product);
    }

    @Diyelimki("^Urun yoneticisi sisteme tum gerekli kosullari saglayan yeni bir urun eklemek istiyor$")
    public void urunYoneticisiSistemeTumGerekliKosullariSaglayanYeniBirUrunEklemekIstiyor() throws Throwable {
        this.init();
    }

    @Eğerki("^Urun yoneticisi eklenecek urun icin gecerli bir fiyat bilgisi girdiyse$")
    public void urunYoneticisiEklenekUrunIcinGecerliBirFiyatBilgisiGirdiyse() throws Throwable {
        BigDecimal price = new BigDecimal(0);
        try {
            price = product.getPrice();
        } catch (NumberFormatException e) {
            fail();
        }
        assertThat(price.intValue(), Matchers.greaterThanOrEqualTo(1));
    }

    @Ve("^Urun yoneticisi eklenek urun icin gecerli bir kategori girdiyse$")
    public void urunYoneticisiEklenekUrunIcinGecerliBirKategoriGirdiyse() throws Throwable {

        try {
            product.getCategories().forEach(category -> categoryService.findByCategoryId(category.getId()));
        } catch (IllegalStateException e) {
            fail();
        }
    }

    @Ozaman("^Sistem yeni urunu kaydeder$")
    public void sistemYeniUrunuKaydeder() throws Throwable {
        ResponseMessage responseMessage = productService.addNewProduct(product);
        String exceptedReturnMessage = environment.getRequiredProperty("product.created.successfully");
        assertEquals(exceptedReturnMessage, responseMessage.getMessage());
    }

    @Diyelimki("^Urun yoneticisi sisteme gecersiz bir fiyat bilgisi ile yeni bir urun eklemek istiyor$")
    public void urunYoneticisiSistemeGecersizBirFiyatBilgisiIleYeniBirUrunEklemekIstiyor() throws Throwable {
        this.init();
        product.setPrice(BigDecimal.valueOf(-1));
    }

    @Eğerki("^Urun yoneticisi eklenecek urun icin gecerli bir fiyat bilgisi girmedi ise$")
    public void urunYoneticisiEklenekUrunIcinGecerliBirFiyatBilgisiGirmediIse() throws Throwable {
        assertThat(product.getPrice().intValue(), Matchers.not(Matchers.greaterThanOrEqualTo(1)));
    }

    @Ozaman("^Sistem urunu kaydetmez ve urun yoneticisine hatali fiyat bilgisi seklinde bir mesaj gonderir$")
    public void sistemUrunuKaydetmezVeUrunYoneticisineHataliFiyatBilgisiSeklindeBirMesajGonderir() throws Throwable {

        ResponseMessage responseMessage = productService.addNewProduct(product);
        String exceptedReturnMessage = environment.getRequiredProperty("product.invalid.price");
        assertEquals(exceptedReturnMessage, responseMessage.getMessage());
        product.setPrice(BigDecimal.valueOf(100));
    }

    @Diyelimki("^Urun yoneticisi sisteme gecersiz bir kategori bilgisine sahip yeni bir urun eklemek istiyor$")
    public void urunYoneticisiSistemeGecersizBirKategoriIleYeniBirUrunEklemekIstiyor() throws Throwable {

        this.init();
        product.getCategories().add(Category.builder().id("1234").title("gecersiz kategori").build());
    }

    @Eğerki("^Urun yoneticisi eklenecek urun icin gecerli bir kategori girmediyse$")
    public void urunYoneticisiEklenekUrunIcinGecerliBirKategoriGirmediyse() throws Throwable {

        String exceptedReturnMessage = environment.getRequiredProperty("category.not.found.title");

        try {
            product.getCategories().forEach(category -> categoryService.findByCategoryTitle(category.getTitle()));
        } catch (GeneralException e) {
            assertEquals(exceptedReturnMessage, e.getMessage());
        }

    }

    @Ozaman("^Sistem urunu kaydetmez ve urun yoneticisine hatali kategori bilgisi seklinde bir mesaj gonderir$")
    public void sistemUrunuKaydetmezVeUrunYoneticisineHataliKategoriBilgisiSeklindeBirMesajGonderir() throws Throwable {

        String exceptedReturnMessage = environment.getRequiredProperty("category.not.found.id");

        ResponseMessage responseMessage = productService.addNewProduct(product);

        assertEquals(exceptedReturnMessage, responseMessage.getMessage());
    }

}
