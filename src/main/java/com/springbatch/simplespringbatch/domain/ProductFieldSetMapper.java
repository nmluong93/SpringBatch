package com.springbatch.simplespringbatch.domain;

import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.batch.infrastructure.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class ProductFieldSetMapper implements FieldSetMapper<Product> {
    /**
     * Method used to map data obtained from a {@link FieldSet} into an object.
     *
     * @param fieldSet the {@link FieldSet} to map
     * @return the populated object
     * @throws BindException if there is a problem with the binding
     */
    @Override
    public Product mapFieldSet(FieldSet fieldSet) throws BindException {
        return new Product(fieldSet.readInt("product_id"), fieldSet.readString("product_name"), fieldSet.readString("product_category"), fieldSet.readInt("product_price"));
    }
}
