package com.trendyol.case2;

import com.trendyol.case2.response.ResponseMessage;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class GeneralValidator {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public <T> ResponseMessage validate(List<Object> validatingObjects, Class<T> clazz) {

        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage.setStatus(true);
        responseMessage.setMessage("");

        Set<ConstraintViolation<Object>> constraintViolations = new HashSet<>();
        validatingObjects.forEach(object -> constraintViolations.addAll(validator.validate(object, clazz)));

        responseMessage.setStatus(constraintViolations.isEmpty());

        Optional<ConstraintViolation<Object>> optionalObjectConstraintViolation = constraintViolations.stream().findFirst();
        if (optionalObjectConstraintViolation.isPresent()) {

            ConstraintViolation<Object> constraintViolation = optionalObjectConstraintViolation.get();

            responseMessage.setMessage(constraintViolation.getMessage());
            responseMessage.setReturnObject(constraintViolation.getLeafBean());
        }

        return responseMessage;
    }

}
