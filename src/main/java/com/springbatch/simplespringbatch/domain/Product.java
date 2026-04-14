package com.springbatch.simplespringbatch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private Integer productId;
    private String productName;
    private String productCategory;
    private Integer productPrice;

}
