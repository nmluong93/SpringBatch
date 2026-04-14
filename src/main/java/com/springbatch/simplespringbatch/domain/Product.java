package com.springbatch.simplespringbatch.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private Integer productId;
    private String productName;
    // this is used in BeanValidatingItemProcessor for validating item
//    @Pattern(regexp = "Mobile Phones|Tablets|Televisions|Sports Accessories|Cameras")
    private String productCategory;

//    @Min(0)
//    @Max(100_000)
    private Integer productPrice;

}
