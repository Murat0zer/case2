package com.trendyol.case2.category;

import com.google.common.collect.Lists;
import com.trendyol.case2.GeneralException;
import com.trendyol.case2.GeneralValidator;
import com.trendyol.case2.response.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.groups.Default;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMessageProvider categoryMessageProvider;

    private final GeneralValidator generalValidator;

    @Transactional
    public ResponseMessage addNewCategory(Category category) {

        ResponseMessage responseMessage;

        responseMessage = generalValidator.validate(Lists.newArrayList(category), Default.class);

        responseMessage.setReturnObject(category);

        if (!responseMessage.isStatus())
            return responseMessage;

        if(categoryRepository.findByTitleEquals(category.getTitle()).isPresent()) {
            responseMessage.setMessage(categoryMessageProvider.getMessage("category.title.duplication"));
            return  responseMessage;
        }

        try {
            category.getParentCategories().forEach(this::findByCategoryTitle);
        } catch (GeneralException e) {
            responseMessage.setMessage(categoryMessageProvider.getMessage("category.parent.not.found.title"));
            responseMessage.setStatus(false);
        }



        if (!responseMessage.isStatus())
            return responseMessage;

        categoryRepository.save(category);
        responseMessage.setStatus(true);
        responseMessage.setMessage(categoryMessageProvider.getMessage("category.created.success"));
        return responseMessage;
    }

    public void addCategories(Set<Category> categories) {
        categoryRepository.saveAll(categories);
    }

    @Transactional
    public ResponseMessage findByCategoryId(String id)   {

        ResponseMessage responseMessage = new ResponseMessage();
        Category category = categoryRepository.findById(id).orElseThrow(() -> {
            log.error("category can not found by given id: {}", id);
            String message = categoryMessageProvider.getMessage("category.not.found.id");
            return new GeneralException(message, id);
        });

        responseMessage.setMessage(categoryMessageProvider.getMessage("category.found.id"));
        responseMessage.setStatus(true);
        responseMessage.setReturnObject(Optional.of(category));
        return responseMessage;
    }

    @Transactional
    public ResponseMessage findByCategoryTitle(String title) {
        ResponseMessage responseMessage = new ResponseMessage();
        Category category = categoryRepository.findByTitleEquals(title).orElseThrow(() -> {
            log.error("category can not found by given id: {}", title);
            String message = categoryMessageProvider.getMessage("category.not.found.title");
            return new GeneralException(message, title);
        });

        responseMessage.setMessage(categoryMessageProvider.getMessage("category.found.title"));
        responseMessage.setStatus(true);
        responseMessage.setReturnObject(Optional.of(category));
        return responseMessage;
    }
}
