package com.trendyol.case2.product;

import com.trendyol.case2.GeneralException;
import com.trendyol.case2.GeneralValidator;
import com.trendyol.case2.category.CategoryService;
import com.trendyol.case2.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.groups.Default;
import java.util.Collections;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final GeneralValidator generalValidator;

    private final ProductMessageProvider productMessageProvider;

    private final CategoryService categoryService;

    public ResponseMessage addNewProduct(Product product) {
        ResponseMessage responseMessage;

        responseMessage = generalValidator.validate(Collections.singletonList(product), Default.class);

        if (!responseMessage.isStatus())
            return responseMessage;

        try {
            product.getCategories().forEach(category -> categoryService.findByCategoryId(category.getId()));
        } catch (GeneralException e) {
            responseMessage.setMessage(e.getMessage());
            responseMessage.setStatus(false);
            responseMessage.setReturnObject(e.getRejectedValue());
            return responseMessage;
        }
        productRepository.save(product);

        String message = productMessageProvider.getMessage("product.created.successfully");
        return new ResponseMessage(true, message, product);

    }
}
