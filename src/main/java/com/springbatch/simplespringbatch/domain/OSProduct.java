package com.springbatch.simplespringbatch.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OSProduct extends Product {

    private int taxPercent;
    private String sku;
    private int shippingRate;

    public OSProduct(Integer productId, String productName, String productCategory, Integer productPrice, int taxPercent, String sku, int shippingRate) {
        super(productId, productName, productCategory, productPrice);
        this.taxPercent = taxPercent;
        this.sku = sku;
        this.shippingRate = shippingRate;
    }

    public OSProduct(Product product, int taxPercent, String sku, int shippingRate) {
        this(product.getProductId(), product.getProductName(), product.getProductCategory(), product.getProductPrice(), taxPercent, sku, shippingRate);
    }
}
