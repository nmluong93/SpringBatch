package com.springbatch.simplespringbatch.domain;

import org.springframework.batch.infrastructure.item.validator.ValidationException;
import org.springframework.batch.infrastructure.item.validator.Validator;

import java.util.List;

public class ProductValidator implements Validator<Product> {

    private static final List<String> VALID_PROD_CATS = List.of("Mobile Phones", "Tablets", "Televisions", "Sports Accessories", "Cameras");

    /**
     * Method used to validate if the value is valid.
     *
     * @param value object to be validated
     * @throws ValidationException if value is not valid.
     */
    @Override
    public void validate(Product value) throws ValidationException {
        if (!VALID_PROD_CATS.contains(value.getProductCategory())) {
            throw new ValidationException(String.format("Product category %s is not valid", value.getProductCategory()));
        }
        if (value.getProductPrice() > 100_000) {
            throw new ValidationException(String.format("Product price (%s) is too high!",  value.getProductPrice()));
        }
    }
}
