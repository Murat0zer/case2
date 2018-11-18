package com.trendyol.case2.steps.category;

import com.trendyol.case2.GeneralException;
import com.trendyol.case2.SpringBootTestConfig;
import com.trendyol.case2.category.Category;
import com.trendyol.case2.category.CategoryRepository;
import com.trendyol.case2.category.CategoryService;
import com.trendyol.case2.response.ResponseMessage;
import com.trendyol.case2.util.CategoryGenerator;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import cucumber.api.java.tr.Ve;
import lombok.RequiredArgsConstructor;
import org.junit.Ignore;
import org.springframework.core.env.Environment;

import java.util.Set;

import static org.junit.Assert.*;

@RequiredArgsConstructor
@Ignore
public class CategoryTests extends SpringBootTestConfig { // NOSONAR

    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;

    private final Environment environment;

    private Category category = Category.builder().build();


    private void init() {
        categoryRepository.deleteAll();

        Set<Category> categories = CategoryGenerator.generateRandomCategories(1);
        category = (Category) categories.toArray()[0];
        this.createParentCategories(category);
    }


    @Diyelimki("^Kategori yoneticisi sisteme yeni bir kategori eklemek istiyor$")
    public void kategoriYoneticisiSistemeYeniBirKategoriEklemekIstiyor() throws Throwable {
        this.init();
    }

    @Eğerki("^Kategori yoneticisi eklenecek kategori icin bir isim girdi ise$")
    public void kategoriYoneticisiEklenecekKategoriIcinBirIsimGirdiIse() throws Throwable {
        assertNotNull(category.getTitle());
    }

    @Ve("^Daha once sistemde olusturulmus ayni isimli bir kategori mevcut degil ise$")
    public void dahaOnceSistemdeOlusturulmusAyniIsimliBirKategoriMevcutDegilIse() throws Throwable {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            categoryService.findByCategoryTitle(category.getTitle());
        } catch (GeneralException e) {
            responseMessage.setMessage(e.getMessage());
        }

        String exceptedReturnMessage = environment.getRequiredProperty("category.not.found.title");
        assertEquals(exceptedReturnMessage, responseMessage.getMessage());
    }

    @Ve("^Kategorinin tanimli ust kategorileri de sistemde mevcut kategoriler ise$")
    public void kategorininTanimliUstKategorileriDeSistemdeMevcutKategorilerIse() throws Throwable {
        try {
            category.getParentCategories().forEach(categoryService::findByCategoryTitle);
        } catch (GeneralException e) {
            fail();
        }
    }

    @Ozaman("^Sistem yeni kategoriyi kaydeder$")
    public void sistemYeniKategoriyiKaydeder() throws Throwable {
        ResponseMessage responseMessage = categoryService.addNewCategory(category);
        String expectedMessage = environment.getRequiredProperty("category.created.success");
        assertEquals(expectedMessage, responseMessage.getMessage());
    }

    @Diyelimki("^Kategori yoneticisi sisteme kategori ismi mevcut olmayan yeni bir kategori eklemek istiyor$")
    public void kategoriYoneticisiSistemeKategoriIsmiMevcutOlmayanYeniBirKategoriEklemekIstiyor() throws Throwable {
        this.init();
        category.setTitle("");
    }

    @Ozaman("^Sistem kategoriyi kaydetmez ve kategori yoneticisine kategori ismi bos birakilamaz gibi bir hata mesaji iletir$")
    public void sistemKategoriyiKaydetmezVeKategoriYoneticisineKategoriIsmiBosBirakilamazGibiBirHataMesajiIletir() throws Throwable {
        ResponseMessage responseMessage = categoryService.addNewCategory(category);
        String expectedMessage = environment.getRequiredProperty("category.title.blank");
        assertEquals(expectedMessage, responseMessage.getMessage());
    }

    @Diyelimki("^Kategori yoneticisi sisteme daha once sistemde mevcut olan bir isimle yeni bir kategori eklemek istiyor$")
    public void kategoriYoneticisiSistemeDahaOnceSistemdeMevcutOlanBirIsimleYeniBirKategoriEklemekIstiyor() throws Throwable {
        this.init();
        categoryService.addNewCategory(category);
    }

    @Ozaman("^Sistem kategoriyi kaydetmez ve kategori yoneticisine kategori zaten sistemde mevcut gibi bir hata mesaji iletir$")
    public void sistemKategoriyiKaydetmezVeKategoriYoneticisineKategoriZatenSistemdeMevcutGibiBirHataMesajiIletir() throws Throwable {
        ResponseMessage responseMessage = categoryService.addNewCategory(category);
        String expectedMessage = environment.getRequiredProperty("category.title.duplication");
        assertEquals(expectedMessage, responseMessage.getMessage());
    }

    @Diyelimki("^Kategori yoneticisi sisteme ust kategorilerinden biri sistemde olmayan yeni bir kategori eklemek istiyor$")
    public void kategoriYoneticisiSistemeUstKategorilerindenBiriSistemdeOlmayanYeniBirKategoriEklemekIstiyor() throws Throwable {
        this.init();
        category.getParentCategories().add("gecersiz kategorii");
    }

    @Ozaman("^Sistem kategoriyi kaydetmez ve kategori yoneticisine gecersiz ust kategori bilgisi bir hata mesaji iletir$")
    public void sistemKategoriyiKaydetmezVeKategoriYoneticisineGecersizUstKategoriBilgisiBirHataMesajiIletir() throws Throwable {
        ResponseMessage responseMessage = categoryService.addNewCategory(category);
        String expectedMessage = environment.getRequiredProperty("category.parent.not.found.title");
        assertEquals(expectedMessage, responseMessage.getMessage());

    }

    private void createParentCategories(Category category) {

        category.getParentCategories().forEach(parentCategoryTitle -> {

            Category pCategory = CategoryGenerator.generateRandomCategories(1)
                    .stream()
                    .findFirst()
                    .orElse(Category.builder().build());
            pCategory.setTitle(parentCategoryTitle);

            if (!categoryRepository.findByTitleEquals(pCategory.getTitle()).isPresent())
                categoryRepository.save(pCategory);

            if (!pCategory.getParentCategories().isEmpty())
                this.createParentCategories(pCategory);
        });
    }
}
